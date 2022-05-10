package com.my.ecomr.ui.components.navs

import android.util.Log
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.my.ecomr.navigations.screensInHomeFromBottomNav
import com.my.ecomr.ui.theme.color_60p_dark
import com.my.ecomr.ui.theme.color_60p_light
import com.my.ecomr.ui.theme.grad1_10p

@Composable
fun BottomBar(navController: NavController) {
    val isLight = MaterialTheme.colors.isLight
    val color_60p = if (isLight) color_60p_light else color_60p_dark


    BottomNavigation(backgroundColor = color_60p) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        screensInHomeFromBottomNav.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            val icon  = if(isSelected) screen.iconSelected else screen.iconUnSelected

            val color = if (isSelected)
                grad1_10p
            else
                Color.DarkGray
//                grad1_10p.copy(alpha = 0.5f)
//                MaterialTheme.colors.surface

            BottomNavigationItem(
                icon = { Icon(imageVector = icon, contentDescription = "", tint = color) },
                label = { Text(screen.title) },
                selected = isSelected,
//                unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
                onClick = {
                    navController.navigate(screen.route) {
                        Log.d("route_g", "Bottom: " + screen.route)
//                        popUpTo(Screens.HomeScreens.Home.route) {
//                            saveState = true
//                        }
//                        // Avoid multiple copies of the same destination when
//                        // reelecting the same item
//                        launchSingleTop = true
//                        // Restore state when reselecting a previously selected item
//                        restoreState = true
                    }
                }
            )
        }
    }
}
