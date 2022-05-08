package com.example.ecomzapp.screens.mains


import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.my.ecomr.MainViewModel
import com.example.ecomzapp.navigations.Screens
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun WishlistScreen(navController: NavController, viewModel: MainViewModel) {
    viewModel.setCurrentScreen(Screens.HomeScreens.WishList)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "WishlistScreen", style = MaterialTheme.typography.h3)
        Button(
            onClick = {
                if (!viewModel.isLoggedIn.value) {
                    navController.navigate(
                        route = Screens.AuthScreens.Login.reroute(
                            "checkout",
                            "123"
                        )
                    )
                } else {
                    navController.navigate(route = Screens.OrderScreens.Checkout.route)
                }
            }) {
            Text(text = "Buy Now")
        }
        Spacer(modifier = Modifier.size(10.dp))
        Button(
            onClick = {
                if (!viewModel.isLoggedIn.value) {
                    navController.navigate(
                        route = Screens.AuthScreens.Login.reroute(
                            "cart",
                            "123"
                        )
                    )
                } else {
                    navController.navigate(route = Screens.HomeScreens.Cart.route)
                }
            }) {
            Text(text = "Add To Cart")
        }
    }
}
