package com.my.ecomr.screens.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.my.ecomr.MainViewModel
import com.example.ecomzapp.navigations.Screens
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun PaymentScreen(navController: NavController, viewModel: MainViewModel) {
    viewModel.setCurrentScreen(Screens.OrderScreens.Payment)
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Black)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Payment", style = MaterialTheme.typography.h2)
        Button(
            onClick = {
                navController.navigate(
                    route = Screens.HomeScreens.Home.route
                ) {
                    popUpTo(Screens.OrderScreens.Payment.route) {
                        inclusive = true
                    }
                }
            }) {
            Text(text = "Done")
        }
    }
}
