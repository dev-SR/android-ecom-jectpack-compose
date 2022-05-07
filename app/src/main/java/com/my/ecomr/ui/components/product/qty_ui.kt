package com.my.ecomr.ui.components.product

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.my.ecomr.CartItem
import com.my.ecomr.MainViewModel

@Composable
fun QtyUi(item: CartItem, viewModel: MainViewModel) {
    val qty = viewModel.cartInfo.value?.cartItems?.find { it.product.id == item.product.id }
//    Log.d("debug", qty.toString())

    Card(
        modifier = Modifier
            .padding(5.dp),
        elevation = 4.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isLight = MaterialTheme.colors.isLight
            IconButton(onClick = {
//                viewModel.decreaseQty(product = item.product, user)

            }) {
                Icon(
                    imageVector = Icons.Rounded.Remove,
                    contentDescription = "Remove",
                    modifier = Modifier
                        .border(
                            2.dp, MaterialTheme.colors.primary, RoundedCornerShape(4.dp)
                        ),
//                        .background(),
//                            .backgrojund(),
                    tint = if (isLight) Color.Black else Color.White,
                )
            }

//                Spacer(modifier = Modifier.padding(5.dp))
            Text(text = "${item.qty}")
//                Spacer(modifier = Modifier.padding(5.dp))
            IconButton(onClick = {

//                viewModel.increaseQty(item.product, user)

            }) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "add",
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(4.dp)
                        )
                        .background(MaterialTheme.colors.primary),
                    tint = Color.White
                )
            }
        }
    }
}