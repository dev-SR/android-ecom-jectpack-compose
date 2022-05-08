package com.my.ecomr.screens.mains

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.my.ecomr.MainViewModel
import com.example.ecomzapp.navigations.Screens
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.my.ecomr.Response


@Composable
fun AccountScreen(navController: NavController, viewModel: MainViewModel) {
    viewModel.setCurrentScreen(Screens.HomeScreens.Account)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!viewModel.isLoggedIn.value) {
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
        } else {
//            Log.d("AccountScreen", viewModel.isLoggedIn.value.toString())

            AsyncImage(
                model = (viewModel.user.value!!.photoUrl),
                contentDescription = "Profile Picture",
                modifier = Modifier.clip(CircleShape).size(120.dp),
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = (viewModel.user.value!!.displayName),
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = (viewModel.user.value!!.email),
                style = MaterialTheme.typography.caption
            )
            Spacer(modifier = Modifier.height(5.dp))

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
}

