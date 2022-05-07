@file:OptIn(ExperimentalAnimationApi::class)

package com.my.ecomr

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.my.ecomr.domains.models.Todo
import com.example.ecomzapp.navigations.BuildNavigation
import com.example.ecomzapp.navigations.Screens
import com.my.ecomr.ui.components.common.TopBar
import com.my.ecomr.ui.components.navs.BottomBar
import com.my.ecomr.ui.theme.EcomTheme
import com.my.ecomr.ui.theme.color_60p_dark
import com.my.ecomr.ui.theme.color_60p_light
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    val WEB_CLIENT_ID = BuildConfig.WEB_CLIENT_ID


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Only Show Splash Screen for the first time running the app...and
        // prevent showing again on screen rotation.
        // savedInstanceState == `null` for the first line
        // on screen rotation it will not be `null`
//        Log.d("rotation", savedInstanceState.toString())

        if (savedInstanceState == null) {
            val splashScreen = installSplashScreen()
            splashScreen.apply {
                setKeepOnScreenCondition {
                    viewModel.loading.value
                }
                setOnExitAnimationListener { splashScreenProvider ->
                    val splashScreenView = splashScreenProvider.view
                    val anim = ObjectAnimator.ofFloat(
                        splashScreenView,
                        View.TRANSLATION_Y,
                        0f,
                        splashScreenView.height.toFloat()
                    )
//                anim.interpolator = BounceInterpolator()
                    anim.interpolator = AnticipateInterpolator()
                    anim.duration = 0L
                    anim.doOnEnd { splashScreenProvider.remove() }
                    anim.start()
                }
            }
        }
        setContent {
            EcomTheme {
                val isLight = MaterialTheme.colors.isLight
                val color_60p = if (isLight) color_60p_light else color_60p_dark
                val systemUiController = rememberSystemUiController()
                systemUiController.setStatusBarColor(color_60p)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = color_60p
                ) {
//                    val vm: HomeScreenViewModel by viewModels()
                    MyApp(viewModel)
                }
            }

        }
    }
}

@Composable
fun MyApp(viewModel: MainViewModel) {
    when (viewModel.todos.value) {
        is Response.Loading -> CircularProgressIndicator()
        is Response.Success -> Log.d(
            "firestore_log",
            (viewModel.todos.value as Response.Success<List<Todo>>).data.toString()
        )
    }


    val navController = rememberAnimatedNavController()
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val bottomBar: @Composable () -> Unit = {
        when (currentScreen) {
            Screens.HomeScreens.Home, Screens.HomeScreens.WishList, Screens.HomeScreens.Cart, Screens.HomeScreens.Account -> {
                BottomBar(
                    navController = navController,
                )
            }
            else -> null
        }
//        if (currentScreen != Screens.AuthScreen.Login && currentScreen != Screens.AuthScreen.Register) {
//
//        }
    }

    Scaffold(
        topBar = {
            TopBar(currentScreen, navController)
        },

        bottomBar = {
            bottomBar()
        },
        scaffoldState = scaffoldState,
//        drawerContent = {
//
//        },
//        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
    ) {
        BuildNavigation(viewModel = viewModel, navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EcomTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Text("Hello World")
        }
    }
}