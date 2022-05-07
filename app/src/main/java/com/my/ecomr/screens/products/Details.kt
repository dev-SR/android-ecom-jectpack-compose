package com.my.ecomr.screens.products

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.my.ecomr.MainViewModel
import com.example.ecomzapp.navigations.Screens


@Composable
fun DetailsScreen(
    navController: NavController,
    viewModel: MainViewModel,
    productId: String? = null
) {
    viewModel.setCurrentScreen(Screens.ProductScreens.Details)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "DetailsScreen", style = MaterialTheme.typography.h2)
        Spacer(modifier = Modifier.size(10.dp))
        if (productId != null) {
            Text(text = productId, style = MaterialTheme.typography.body1)
        }
        Button(
            onClick = {
                if (!viewModel.isLoggedIn.value) {
                    navController.navigate(
                        route = Screens.AuthScreens.Login.reroute(
                            "checkout",
                            productId!!
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
                            productId = productId!!
                        )
                    )
                } else {
                    navController.navigate(route = Screens.HomeScreens.Cart.addNewProductToCart(productId))
                }
            }) {
            Text(text = "Add To Cart")
        }
    }
}