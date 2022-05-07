package com.my.ecomr.screens.mains

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.my.ecomr.MainViewModel
import com.example.ecomzapp.navigations.Screens
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun AccountScreen(navController: NavController, viewModel: MainViewModel) {
    viewModel.setCurrentScreen(Screens.HomeScreens.Account)
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Black)
    val isLoggedIn = viewModel.isLoggedIn.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "AccountScreen", style = MaterialTheme.typography.h3)
        if (!isLoggedIn.value) {
            Button(
                onClick = {
                    viewModel.logout()
                    navController.navigate(
                        route = Screens.AuthScreens.Login.route
                    ) {
                        popUpTo(0)
                    }
                }) {
                Text(text = "Login")
            }
        } else
            Button(
                onClick = {
                    viewModel.logout()
                    navController.navigate(
                        route = Screens.HomeScreens.Home.route
                    ) {
                        popUpTo(0)
                    }
                }) {
                Text(text = "Logout")
            }
    }
}

