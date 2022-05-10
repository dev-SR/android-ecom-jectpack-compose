package com.my.ecomr.screens.auths

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.my.ecomr.MainViewModel
import com.my.ecomr.navigations.MAIN_ROUTE
import com.my.ecomr.navigations.Screens

@Composable
fun RegisterScreen(
    navController: NavController, viewModel: MainViewModel,
    redirectTo: String?,
    productId: String?
) {
    viewModel.setCurrentScreen(Screens.AuthScreens.Register)
    Row(modifier = Modifier.fillMaxWidth()) {
        IconButton(onClick = {
            navController.navigate(MAIN_ROUTE){
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
        Text(text = "RegisterPage", style = MaterialTheme.typography.h2)
        Button(onClick = {
//            viewModel.login()
            when (redirectTo) {
                "checkout" -> {
                    navController.navigate(route = Screens.OrderScreens.Checkout.route) {
                        popUpTo(Screens.AuthScreens.Register.route) { inclusive = true }
                    }
                }
                "cart" -> {
                    navController.navigate(route = Screens.HomeScreens.Cart.route) {
                        popUpTo(Screens.AuthScreens.Register.route) { inclusive = true }
                    }
                }
                else -> {
                    navController.navigate(route = MAIN_ROUTE) {
                        popUpTo(Screens.AuthScreens.Register.route) { inclusive = true }
                    }
                }
            }
        }) {
            Text(text = "Register")
        }
        Spacer(modifier = Modifier.size(10.dp))
        Row {
            Text(text = "Already have an Account? ", style = MaterialTheme.typography.subtitle1)
            Text(
                text = "Sign In",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.clickable {
                    navController.popBackStack()
                    navController.navigate(route = Screens.AuthScreens.Login.route)
                },
            )
        }
    }
}

