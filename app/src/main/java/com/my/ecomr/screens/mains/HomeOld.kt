package com.my.ecomr.screens.mains

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.my.ecomr.MainViewModel
import com.example.ecomzapp.navigations.Screens
import com.google.accompanist.systemuicontroller.rememberSystemUiController

val lists = listOf("A", "B", "C", "D")

@Composable
fun HomeScreenOld(navController: NavController, viewModel: MainViewModel) {
    viewModel.setCurrentScreen(Screens.HomeScreens.Home)
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Black)

    LazyColumn {
        items(lists) { i ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .clickable {
                        navController.navigate(
                            Screens.ProductScreens.Details.routeToDetailsOf(i
//                                lists
//                                    .indexOf(i)
//                                    .toString()
                            )
                        )
                    }, elevation = 4.dp
            ) {
                Text(text = "$i", modifier = Modifier.padding(10.dp))
            }
        }
    }
}
