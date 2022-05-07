package com.my.ecomr.ui.components.navs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.my.ecomr.R

@Composable
fun Drawer(
    modifier: Modifier = Modifier,
    onDestinationClicked: (route: String) -> Unit
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 48.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "App icon"
        )
//        screensFromDrawer.forEach { screen ->
//            Spacer(Modifier.height(24.dp))
//            Text(
//                text = screen.title,
//                style = MaterialTheme.typography.h4,
//                modifier = Modifier.fillMaxWidth().clickable {
//                    onDestinationClicked(screen.route)
//                }
//            )
//        }
    }
}