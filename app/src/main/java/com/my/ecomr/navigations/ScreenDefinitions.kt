package com.example.ecomzapp.navigations

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

const val AUTH_ROUTE = "auth"
const val MAIN_ROUTE = "main"
const val ORDER_ROUTE = "order"
const val PRODUCT_ROUTE = "product"

//https://fonts.google.com/icons?icon.style=Filled
//    implementation "androidx.compose.material:material-icons-extended:$compose_version"
sealed class Screens(val route: String, val title: String) {
    sealed class AuthScreens(route: String, title: String) : Screens(route, title) {
        object Login : AuthScreens("/login?redirect={page}&productId={pid}", "login_screen") {
            fun reroute(page: String, productId: String?) =
                "/login?redirect=$page&productId=$productId"
        }

        object Register : AuthScreens("/register?redirect={page}&productId={pid}", "register_screen"){
            fun reroute(page: String, productId: String) =
                "/register?redirect=$page&productId=$productId"
        }
    }

    sealed class HomeScreens(route: String, title: String, val iconSelected: ImageVector,val iconUnSelected: ImageVector) : Screens(
        route,
        title
    ) {
        object Home : HomeScreens("/home", "Home", Icons.Rounded.Home, Icons.Outlined.Home)
        object WishList : HomeScreens("/wishlist", "WishList", Icons.Rounded.Favorite,Icons.Outlined.FavoriteBorder)
        object Cart : HomeScreens("/cart?add={newProduct}", "Cart", Icons.Rounded.ShoppingCart,Icons.Outlined.ShoppingCart){
            fun addNewProductToCart(productId: String?) = "/cart?add=$productId"
        }
        object Account : HomeScreens("/account", "Account", Icons.Rounded.AccountCircle,Icons.Outlined.AccountCircle)
    }

    sealed class ProductScreens(route: String, title: String) : Screens(
        route,
        title
    ) {
        object Details : ProductScreens("/details_screen/{productId}", "Detail") {
            fun routeToDetailsOf(productId: String) = "/details_screen/$productId"
        }

    }

    sealed class OrderScreens(route: String, title: String) : Screens(
        route,
        title
    ) {
        object Checkout : OrderScreens("/checkout", "Checkout")
        object Address : OrderScreens("/address", "Address")
        object Payment : OrderScreens("/payment", "Payment")
        object MyOrder : OrderScreens("/myOrder", "MyOrder")
        object ToShip : OrderScreens("/toShip", "ToShip")
        object ToReceive : OrderScreens("/toReceive", "ToReceive")
    }

    sealed class ServiceScreens(route: String, title: String, val icon: ImageVector) : Screens(
        route,
        title
    ) {
        object Messages : ServiceScreens("/messages", "Messages", Icons.Filled.Favorite)
        sealed class Review(
            route: String,
            title: String,
            icon: ImageVector
        ) : ServiceScreens(route, title, icon) {
            object ToReview : ServiceScreens("/to_review", "ToReview", Icons.Filled.Notifications)
            object ReviewHistory :
                ServiceScreens("/review_history", "ReviewHistory", Icons.Filled.Notifications)
        }

    }

}

val screensInHomeFromBottomNav = listOf(
    Screens.HomeScreens.Home,
    Screens.HomeScreens.WishList,
    Screens.HomeScreens.Cart,
    Screens.HomeScreens.Account,
)

