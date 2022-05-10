package com.my.ecomr.screens.orders

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.my.ecomr.MainViewModel
import com.my.ecomr.navigations.Screens

@Composable
fun CheckoutScreen(navController: NavController, viewModel: MainViewModel) {
    viewModel.setCurrentScreen(Screens.OrderScreens.Checkout)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "CheckoutScreen", style = MaterialTheme.typography.h3)
        Button(
            onClick = {
                navController.navigate(
                    route = Screens.OrderScreens.Payment.route
                )
            }) {
            Text(text = "Payment")
        }
    }
}
