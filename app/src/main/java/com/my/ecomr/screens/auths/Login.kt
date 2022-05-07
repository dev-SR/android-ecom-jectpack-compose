package com.my.ecomr.screens.auths

import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.my.ecomr.MainViewModel
import com.example.ecomzapp.navigations.Screens
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.tasks.await

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: MainViewModel,
    redirectTo: String?,
    productId: String?
) {
    viewModel.setCurrentScreen(Screens.AuthScreens.Login)

    val context = LocalContext.current



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
        val scope = rememberCoroutineScope()
        Button(
            onClick = {

//                viewModel.login()
//                when (redirectTo) {
//                    "checkout" -> {
//                        navController.navigate(route = Screens.OrderScreens.Checkout.route) {
//                            popUpTo(Screens.AuthScreens.Login.route) { inclusive = true }
//                        }
//                    }
//                    "cart" -> {
//                        if (productId == null) {
//                            Log.d("route_g", "null-> " + productId.toString())
//                            navController.navigate(route = Screens.HomeScreens.Cart.route) {
//                                popUpTo(Screens.AuthScreens.Login.route) { inclusive = true }
//                            }
//                        }
//                        productId?.let {
//                            Log.d("route_g", "not null -> $productId")
//                            navController.navigate(
//                                route = Screens.HomeScreens.Cart.addNewProductToCart(
//                                    productId = productId!!
//                                )
//                            ) {
//                                popUpTo(Screens.AuthScreens.Login.route) { inclusive = true }
//                            }
//                        }
//                    }
//                    else -> {
//                        navController.navigate(route = MAIN_ROUTE) {
//                            popUpTo(Screens.AuthScreens.Login.route) { inclusive = true }
//                        }
//                    }
//                }
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


suspend fun signIn(
    context: Context,
    launcher: ActivityResultLauncher<IntentSenderRequest>
) {
    val oneTapClient = Identity.getSignInClient(context)
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                // Your server's client ID, not your Android client ID.
                .setServerClientId("1043395628249-cun49dsv0ft58s36neuvae8souqtrs8d.apps.googleusercontent.com")
                // Only show accounts previously used to sign in.
                .setFilterByAuthorizedAccounts(true)
                .build()
        )
        // Automatically sign in when exactly one credential is retrieved.
        .setAutoSelectEnabled(true)
        .build()

    try {
        // Use await() from https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-play-services
        // Instead of listeners that aren't cleaned up automatically
        val result = oneTapClient.beginSignIn(signInRequest).await()

        // Now construct the IntentSenderRequest the launcher requires
        val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
        launcher.launch(intentSenderRequest)
    } catch (e: Exception) {
        // No saved credentials found. Launch the One Tap sign-up flow, or
        // do nothing and continue presenting the signed-out UI.
        Log.d("LOG", e.message.toString())
    }
}