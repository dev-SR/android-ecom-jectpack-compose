package com.my.ecomr.screens.auths

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.my.ecomr.navigations.MAIN_ROUTE
import com.my.ecomr.MainViewModel
import com.my.ecomr.navigations.Screens
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.my.ecomr.Response
import com.my.ecomr.domains.services.getGoogleSignInClient

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: MainViewModel,
    redirectTo: String?,
    productId: String?
) {
    viewModel.setCurrentScreen(Screens.AuthScreens.Login)

    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                viewModel.signWithGoogleCredential(credential)
            } catch (e: ApiException) {
                Log.w("TAG", "Google sign in failed", e)
            }
        }

    when (viewModel.authStatus.value) {
        is Response.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
        is Response.Success -> {
            Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
        }
        is Response.Error -> {
            Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
        }
        else -> {
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = {
                    navController.navigate(Screens.HomeScreens.Home.route) {
//                popUpTo(Screens.AuthScreens.Login.route) { inclusive = true }
                    }
                }) {
                    Icon(Icons.Filled.Close, contentDescription = "")
                }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "LoginPage", style = MaterialTheme.typography.h2)
                Button(
                    onClick = {
                        val googleSignInClient = getGoogleSignInClient(context)
                        launcher.launch(googleSignInClient.signInIntent)

                    }
                )
                {
                    Text(text = "Login")
                }
                Spacer(modifier = Modifier.size(10.dp))
                Row {
                    Text(text = "Not registered yet? ", style = MaterialTheme.typography.subtitle1)
                    Text(
                        text = "Create New Account",
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                            navController.navigate(
                                route = Screens.AuthScreens.Register.reroute(
                                    redirectTo!!,
                                    productId!!
                                )
                            )
                        },
                    )
                }
            }
        }

    }
    LaunchedEffect(key1 = viewModel.isLoggedIn.value) {
        if (viewModel.isLoggedIn.value) {
            when (redirectTo) {
                "checkout" -> {
                    navController.navigate(route = Screens.OrderScreens.Checkout.route) {
                        popUpTo(Screens.AuthScreens.Login.route) { inclusive = true }
                    }
                }
                "cart" -> {
                    if (productId == null) {
                        Log.d("route_g", "null-> " + productId.toString())
                        navController.navigate(route = Screens.HomeScreens.Cart.route) {
                            popUpTo(Screens.AuthScreens.Login.route) { inclusive = true }
                        }
                    }
                    productId?.let {
                        Log.d("route_g", "not null -> $productId")
                        navController.navigate(
                            route = Screens.HomeScreens.Cart.addNewProductToCart(
                                productId = productId!!
                            )
                        ) {
                            popUpTo(Screens.AuthScreens.Login.route) { inclusive = true }
                        }
                    }
                }
                else -> {
                    navController.navigate(route = MAIN_ROUTE) {
                        popUpTo(Screens.AuthScreens.Login.route) { inclusive = true }
                    }
                }
            }
        }
    }
}