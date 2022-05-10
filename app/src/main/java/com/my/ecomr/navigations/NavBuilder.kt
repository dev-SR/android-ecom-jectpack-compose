@file:OptIn(ExperimentalAnimationApi::class)

package com.my.ecomr.navigations

import android.util.Log
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import com.my.ecomr.MainViewModel
import com.my.ecomr.screens.auths.LoginScreen
import com.my.ecomr.screens.auths.RegisterScreen
import com.example.ecomzapp.screens.mains.*
import com.my.ecomr.screens.orders.CartScreen
import com.my.ecomr.screens.orders.CheckoutScreen
import com.my.ecomr.screens.orders.PaymentScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.my.ecomr.screens.mains.*
import com.my.ecomr.screens.products.DetailsScreen

@Composable
fun BuildNavigation(viewModel: MainViewModel, navController: NavHostController) {
    AnimatedNavHost(
        navController = navController,
        startDestination = MAIN_ROUTE,
    ) {
        authGraph(navController = navController, viewModel = viewModel)
        homeGraph(navController = navController, viewModel = viewModel)
        productGraph(navController = navController, viewModel = viewModel)
        orderGraph(navController = navController, viewModel = viewModel)
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.homeGraph(navController: NavHostController, viewModel: MainViewModel) {
    navigation(startDestination = Screens.HomeScreens.Home.route, route = MAIN_ROUTE) {
        composable(
            route = Screens.HomeScreens.Home.route,
            enterTransition = {
                when (initialState.destination.route) {//this.initialState
                    //if coming form any of Auth route slide in from left
                    Screens.ProductScreens.Details.route,
                    Screens.HomeScreens.WishList.route, Screens.HomeScreens.Cart.route, Screens.HomeScreens.Account.route,
                    Screens.AuthScreens.Login.route, Screens.AuthScreens.Register.route -> slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(700)
                    )
                    else -> null
                }
            },
            exitTransition = { ExitTransition.None }

//            exitTransition = {
//                when (targetState.destination.route) {
//                    Screens.HomeScreens.WishList.route, Screens.HomeScreens.Cart.route, Screens.HomeScreens.Account.route ->
//                        slideOutOfContainer(
//                            AnimatedContentScope.SlideDirection.Left,
//                            animationSpec = tween(700)
//                        )
//                    else -> null
//                }
//            }

        ) { back ->
//            Log.d("route_g", "Home: " + back.arguments.toString())
            HomeScreen(navController = navController, viewModel = viewModel)


//            HomeScreenLocal(
//                heroProductLocal = heroProduct,
//                popularsNow = listOfProduct,
//                topProductLocals = listOfProduct,
//                categoryLocalList = listOfCategory,
//            )
//            HomeScreenOld(navController = navController, viewModel = viewModel)
        }
        composable(
            route = Screens.HomeScreens.WishList.route,
            enterTransition = {
                when (initialState.destination.route) {//this.initialState
                    //if coming form any of Auth route slide in from left
                    Screens.HomeScreens.Home.route -> slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                    Screens.HomeScreens.Cart.route, Screens.HomeScreens.Account.route -> slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(700)
                    )
                    else -> null
                }
            },
            exitTransition = { ExitTransition.None }

        ) {
            WishlistScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = Screens.HomeScreens.Cart.route,
            exitTransition = { ExitTransition.None },
            enterTransition = {
                when (initialState.destination.route) {//this.initialState
                    //if coming form any of Auth route slide in from left
                    Screens.ProductScreens.Details.route,
                    Screens.HomeScreens.Home.route, Screens.HomeScreens.WishList.route -> slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                    Screens.HomeScreens.Account.route -> slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(700)
                    )
                    else -> null
                }
            },
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("newProduct")
            if (productId == null) {
                Log.d("productId", "Cart [null]" + productId.toString())
            }
            Log.d("route_g", "Cart-> " + productId.toString())

            CartScreen(navController = navController, viewModel = viewModel, productId)
        }

        composable(
            route = Screens.HomeScreens.Account.route,
            exitTransition = { ExitTransition.None },
            enterTransition = {
                when (initialState.destination.route) {//this.initialState
                    //if coming form any of Auth route slide in from left
                    Screens.HomeScreens.Home.route, Screens.HomeScreens.WishList.route, Screens.HomeScreens.Cart.route -> slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                    else -> null
                }
            },
        ) {
            AccountScreen(navController = navController, viewModel = viewModel)
        }
    }
}

fun NavGraphBuilder.productGraph(navController: NavHostController, viewModel: MainViewModel) {
    navigation(startDestination = Screens.ProductScreens.Details.route, route = PRODUCT_ROUTE) {
        composable(
            route = Screens.ProductScreens.Details.route,
            enterTransition = {
                when (initialState.destination.route) {//this.initialState
                    //if coming form any of Auth route slide in from left
                    Screens.HomeScreens.Home.route -> slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                    else -> null
                }
            },
            exitTransition = { ExitTransition.None },
            popExitTransition = { ExitTransition.None }
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            DetailsScreen(navController = navController, viewModel = viewModel, productId)
        }

    }
}

fun NavGraphBuilder.authGraph(navController: NavHostController, viewModel: MainViewModel) {
    navigation(startDestination = Screens.AuthScreens.Login.route, route = AUTH_ROUTE) {
        composable(
            route = Screens.AuthScreens.Login.route,
            arguments = listOf(
                navArgument("page") { defaultValue = "account" },
                navArgument("pid") { defaultValue = "p" }),
            exitTransition = { ExitTransition.None },
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Down,
                    animationSpec = tween(500)
                )
            }

        ) { backStackEntry ->
            val redirect = backStackEntry.arguments?.getString("page")
            var productId = backStackEntry.arguments?.getString("pid")
            if (productId.equals("null")) {
                Log.d("route_g", "login:" + productId.toString())
                productId = null
            }
            LoginScreen(navController = navController, viewModel = viewModel, redirect, productId)
        }
        composable(route = Screens.AuthScreens.Register.route,
            arguments = listOf(
                navArgument("page") { defaultValue = "account" },
                navArgument("pid") { defaultValue = "p" }),
            exitTransition = { ExitTransition.None },
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Down,
                    animationSpec = tween(500)
                )
            }

        ) {

                backStackEntry ->
            val redirect = backStackEntry.arguments?.getString("page")
            val productId = backStackEntry.arguments?.getString("pid")
//            Log.d("route_g", backStackEntry.arguments.toString())
            RegisterScreen(
                navController = navController,
                viewModel = viewModel,
                redirect,
                productId
            )
        }
    }
}


fun NavGraphBuilder.orderGraph(navController: NavHostController, viewModel: MainViewModel) {
    navigation(startDestination = Screens.OrderScreens.Checkout.route, route = ORDER_ROUTE) {
        composable(
            route = Screens.OrderScreens.Checkout.route,
            exitTransition = { ExitTransition.None },
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            }

        ) {
            CheckoutScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = Screens.OrderScreens.Payment.route,
            exitTransition = { ExitTransition.None },
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            }
        ) {
            PaymentScreen(navController = navController, viewModel = viewModel)
        }
    }
}