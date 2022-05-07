package com.my.ecomr.screens.orders

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.my.ecomr.MainViewModel
import com.my.ecomr.Product
import com.example.ecomzapp.navigations.Screens
import com.my.ecomr.ui.components.product.QtyUi
import com.my.ecomr.user
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun CartScreen(
    navController: NavController, viewModel: MainViewModel, productId: String? = null
) {
    viewModel.setCurrentScreen(Screens.HomeScreens.Cart)
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Black)
    val isLoggedIn = viewModel.isLoggedIn.collectAsState()
    val cartInfo = viewModel.cartInfo
    val cartItem = viewModel.cartInfo.value?.cartItems
    val value = viewModel.value.collectAsState()
//    Log.d("debug", value.value.toString())
    Log.d("debug", cartItem.toString())

    LaunchedEffect(key1 = true) {
        if (!isLoggedIn.value) {
            navController.navigate(Screens.AuthScreens.Login.reroute("cart", null)) {
                popUpTo(Screens.HomeScreens.Home.route)
            }
        }
        productId?.let {
            viewModel.addToCart(user = user, Product(productId!!, productId), qty = 1)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (cartInfo.value == null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "CartScreen", style = MaterialTheme.typography.h2)
                Text(
                    text = "Cart is Empty",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.primary
                )
            }
        } else {
            Text(text = "CartScreen", style = MaterialTheme.typography.h2)
            if (cartItem!= null) {
                LazyColumn() {
                    items(cartItem!!) { i ->
                        Log.d("debug", "lc->: $i")

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .clickable {
                                    navController.navigate(
                                        Screens.ProductScreens.Details.routeToDetailsOf(i.product.id)
                                    )
                                }, elevation = 4.dp
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = i.product.name, modifier = Modifier
                                        .padding(10.dp)
                                        .weight(1f)
                                )
                                QtyUi(
                                    i,
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            navController.navigate(
                                route = Screens.OrderScreens.Checkout.route
                            )
                        }) {
                        Text(text = "Checkout")
                    }
                }
            }
        }
    }
}

