package com.my.ecomr.ui.components.common

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.my.ecomr.navigations.Screens

@Composable
fun TopBar(currentScreens: Screens, navController: NavHostController) {
    when (currentScreens) {
//        Screens.HomeScreens.Home -> {
//            BuildTopBar(title = "Home")
//        }
//        Screens.HomeScreens.Cart -> {
//            BuildTopBar(title = "MyCart")
//        }
        Screens.OrderScreens.Checkout ->{
            BuildTopBar(title = "Checkout", buttonIcon = Icons.Filled.ArrowBack, onButtonClicked = {
                navController.popBackStack()
            })

        }
//        Screens.ProductScreens.Details ->{
//            BuildTopBar(title = "Checkout", buttonIcon = Icons.Filled.ArrowBack, onButtonClicked = {
//                navController.popBackStack()
//            })
//
//        }
        Screens.OrderScreens.Payment ->{
            BuildTopBar(title = "Payment", buttonIcon = Icons.Filled.ArrowBack, onButtonClicked = {
                navController.popBackStack()
            })
        }
        else -> null
    }

}

@Composable
fun BuildTopBar(
    title: String = "",
    onButtonClicked: () -> Unit = {},
    buttonIcon: ImageVector? = null
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = Color.White
            )
        },
        navigationIcon = {
            buttonIcon?.let {
                IconButton(onClick = { onButtonClicked() }) {
                    Icon(buttonIcon, contentDescription = "")
                }
            }

        },
        backgroundColor = MaterialTheme.colors.background
    )
}