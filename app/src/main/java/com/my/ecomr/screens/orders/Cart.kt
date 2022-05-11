package com.my.ecomr.screens.orders

import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.my.ecomr.MainViewModel
import com.my.ecomr.Response
import com.my.ecomr.domains.models.CartItem
import com.my.ecomr.navigations.Screens
import com.my.ecomr.screens.products.Review
import com.my.ecomr.screens.products.review
import com.my.ecomr.ui.theme.*

@Composable
fun CartScreen(
    navController: NavController, viewModel: MainViewModel, productId: String? = null
) {
    viewModel.setCurrentScreen(Screens.HomeScreens.Cart)
    val isLoggedIn = viewModel.isLoggedIn.value
    val selectionCount = viewModel.selectedCarts.value.size
    val selectedCartIds = viewModel.selectedCarts.value
    Log.d("trace:select", selectedCartIds.toString())


    val goToDetails: (productId: String) -> Unit = { productId ->
        navController.navigate(
            Screens.ProductScreens.Details.routeToDetailsOf(productId)
        )
    }

    val checkOut: () -> Unit = {
        navController.navigate(
            route = Screens.OrderScreens.Checkout.route
        )
    }
    val increaseQty: (CartItem) -> Unit = { cartItem ->
        viewModel.increaseQty(cartItem)
    }

    val decreaseQty: (CartItem) -> Unit = { cartItem ->
        viewModel.decreaseQty(cartItem)
    }

    val selectOrDeselect: (cartId: String) -> Unit = {
        viewModel.selectOrDeselect(it)
    }

    LaunchedEffect(key1 = true) {
        if (!isLoggedIn) {
            navController.navigate(Screens.AuthScreens.Login.reroute("cart", null)) {
                popUpTo(Screens.HomeScreens.Home.route)
            }
        }
        viewModel.getCartInfo()
        productId?.let {
            viewModel.addToCart(productId)
        }
    }
    when (viewModel.cartInfo.value) {
        is Response.Loading -> {
            if(!viewModel.empty.value){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = grad1_10p)

                }
            }
        }
        is Response.Success -> {
            val cart = (viewModel.cartInfo.value as Response.Success<List<CartItem>>).data
            CartInfoScreen(
                cart,
                goToDetails,
                checkOut,
                increaseQty,
                decreaseQty,
                selectOrDeselect,
                selectionCount,
                selectedCartIds,
            )
        }
        else -> {}
    }

}

@Composable
fun CartInfoScreen(
    cartInfo: List<CartItem> = emptyList(),
    goToDetails: (productId: String) -> Unit,
    checkOut: () -> Unit,
    increaseQty: (CartItem) -> Unit,
    decreaseQty: (CartItem) -> Unit,
    selectOrDeselect: (cartId: String) -> Unit,
    selectionCount: Int = 0,
    selectedCartIds: List<String> = emptyList(),
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (cartInfo.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "CartScreen", style = MaterialTheme.typography.h2)
                Text(
                    text = "Cart is Empty",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.primary
                )
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = "Cart",
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.weight(1f)
                )
                if (selectionCount > 0) {
                    Text(
                        text = "Delete(${selectionCount})",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.clickable {


                        },
                        color = MaterialTheme.colors.primary
                    )
                }
            }
            LazyColumn() {
                items(cartInfo) { cartItem ->
                    val id = cartItem.cartId
                    val isSelected = selectedCartIds.contains(id)
                    CartItemCard(
                        cartItem,
                        goToDetails,
                        increaseQty,
                        decreaseQty,
                        isSelected,
                        selectOrDeselect
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        checkOut()
                    }) {
                    Text(text = "Checkout")
                }
            }
        }

    }
}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    goToDetails: (productId: String) -> Unit,
    increaseQty: (CartItem) -> Unit,
    decreaseQty: (CartItem) -> Unit,
    isSelected: Boolean,
    selectOrDeselect: (cartId: String) -> Unit,
) {
    val isLight = MaterialTheme.colors.isLight
    val color_60p = if (isLight) color_60p_light else color_60p_dark
    var textColor = if (isLight) Color.Black else Color.White

    val color = if (isSelected)
        grad2_10p else grad1_10p
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                goToDetails(cartItem.productId!!)
            },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(3.dp, color),
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = isSelected, onCheckedChange = {
                selectOrDeselect(cartItem.cartId!!)
            })
            Text(
                text = "${cartItem.product!!.name}",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 5.dp),
                color = textColor,
                overflow = TextOverflow.Ellipsis,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        decreaseQty(cartItem)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Remove,
                        contentDescription = "minus",
                        tint = if(cartItem.qty!! <= 1) Color.Gray else color_10p_light,
                        modifier = Modifier.border(1.dp, color_10p_light, shape = RoundedCornerShape(5.dp)),
                        )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "${cartItem.qty!!}",
                    color = color_10p_light
                )
                IconButton(onClick = {
                    increaseQty(cartItem)
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "add",
                        modifier = Modifier.background(
                            brush = color_10p,
                            RoundedCornerShape(5.dp)
                        ),
                    )
                }
            }

        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun Preview() {
    EcomTheme {
        Surface() {
            val isLight = MaterialTheme.colors.isLight
            val color_60p = if (isLight) color_60p_light else color_60p_dark
            IconButton(
                onClick = {
                },

                ) {
                Icon(
                    imageVector = Icons.Rounded.Remove,
                    contentDescription = "minus",
                    modifier = Modifier.background(MaterialTheme.colors.primary, RoundedCornerShape(5.dp)),
                )
            }
        }
    }
}