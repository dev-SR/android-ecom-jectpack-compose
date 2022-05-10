package com.my.ecomr.screens.products

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.my.ecomr.MainViewModel
import com.my.ecomr.R
import com.my.ecomr.Response
import com.my.ecomr.domains.models.Product
import com.my.ecomr.navigations.Screens
import com.my.ecomr.ui.components.product.RatingBar
import com.my.ecomr.ui.theme.*
import com.skydoves.landscapist.coil.CoilImage
import kotlin.math.max
import kotlin.math.min

val AppBarCollapsedHeight = 56.dp
val AppBarExpendedHeight = 400.dp

@Composable
fun DetailsScreen(
    navController: NavController,
    viewModel: MainViewModel,
    productId: String? = null
) {
    val checkoutOnClick: (productID: String) -> Unit = { productID ->
        if (!viewModel.isLoggedIn.value) {
            navController.navigate(
                route = Screens.AuthScreens.Login.reroute(
                    "checkout",
                    productId!!
                )
            )
        } else {
            navController.navigate(route = Screens.OrderScreens.Checkout.route)
        }
    }
    val cartOnClick: (productID: String) -> Unit = { productID ->
        if (!viewModel.isLoggedIn.value) {
            navController.navigate(
                route = Screens.AuthScreens.Login.reroute(
                    "cart",
                    productId = productID
                )
            )
        } else {
            navController.navigate(route = Screens.HomeScreens.Cart.addNewProductToCart(productID))
        }
    }
    val goBackOnClick: () -> Unit = {
        navController.popBackStack()

    }
    LaunchedEffect(Unit) {
        viewModel.getProduct(productId!!)
    }
    viewModel.setCurrentScreen(Screens.ProductScreens.Details)

    val scope = rememberCoroutineScope()

    when (viewModel.product.value) {
        is Response.Loading -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is Response.Success -> {
            val product = (viewModel.product.value as Response.Success<Product>).data
            Log.d("product", product.toString())
            CollapseToolBar(
                product = product,
                goBackOnClick = goBackOnClick,
                checkoutOnClick = checkoutOnClick,
                cartOnClick = cartOnClick
            )
        }
        else -> {}
    }
}

@Composable
fun CollapseToolBar(
    product: Product,
    goBackOnClick: () -> Unit,
    checkoutOnClick: (productID: String) -> Unit,
    cartOnClick: (productID: String) -> Unit
) {
    val scrollState = rememberLazyListState()
    val isLight = MaterialTheme.colors.isLight
    val color_60p = if (isLight) color_60p_light else color_60p_dark
    Box(modifier = Modifier.fillMaxSize()) {
        Content(scrollState, product)
        ProfileToolBar(scrollState, product, goBackOnClick)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(color = color_60p)
                .padding(5.dp),
        ) {
            BottomToolBar(
                product,
                checkoutOnClick,
                cartOnClick
            )
        }

    }
}

@Composable
fun BottomToolBar(
    product: Product,
    checkoutOnClick: (productID: String) -> Unit,
    cartOnClick: (productID: String) -> Unit
) {
    val isLight = MaterialTheme.colors.isLight
    var textColor = if (isLight) Color.Black else Color.White
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = {
                cartOnClick(product.productId!!)
            },
            colors = buttonColors(Color.Transparent),
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(5.dp)
                    )
                    .background(color_10p)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "Add to Cart", color = Color.White)
            }
        }
        Button(
            onClick = {
                checkoutOnClick(product.productId!!)
            },
            colors = buttonColors(Color.Transparent),
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(5.dp)
                    )
                    .background(color_10p)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "Checkout", color = Color.White)
            }
        }
    }

}

@Composable
fun ProfileToolBar(
    scrollState: LazyListState, product: Product,
    goBackOnClick: () -> Unit

) {
    val imageHeight = AppBarExpendedHeight - AppBarCollapsedHeight
    val maxOffset = with(LocalDensity.current) {
        imageHeight.roundToPx()
    } - LocalWindowInsets.current.systemBars.layoutInsets.top
    val offset = min(scrollState.firstVisibleItemScrollOffset, maxOffset)
    val offsetProgress = max(0f, offset * 3f - 2f * maxOffset) / maxOffset
    val isLight = MaterialTheme.colors.isLight
    val color_60p = if (isLight) color_60p_light else color_60p_dark
    var textColor = if (isLight) Color.Black else Color.White
    TopAppBar(
        contentPadding = PaddingValues(),
        backgroundColor = color_60p,
        modifier = Modifier
            .height(
                AppBarExpendedHeight
            )
            .offset {
                IntOffset(x = 0, y = -offset)
            },
        elevation = if (offset == maxOffset) 4.dp else 0.dp
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .graphicsLayer {
                        alpha = 1f - offsetProgress
                    }
                    .background(color_60p)

            ) {
                var imgSize by remember { mutableStateOf(0f) }
                val imgSizeInDp by animateFloatAsState(
                    targetValue = imgSize,
                    animationSpec = tween(
                        durationMillis = 1000,
                        easing = FastOutSlowInEasing
                    )
                )
                LaunchedEffect(Unit) {
                    imgSize = 1f
                }
                CoilImage(
                    imageModel = product.img!!,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(imgSizeInDp)
                        .padding(10.dp),
                    contentScale = ContentScale.Fit,
                    // shows a shimmering effect when loading an image.
//                    shimmerParams = ShimmerParams(
//                        baseColor = color_30p,
//                        highlightColor = Color.Gray,
//                        durationMillis = 350,
//                        dropOff = 0.65f,
//                        tilt = 20f
//                    ),
                    // shows an error text message when request failed.
                    failure = {
                        Image(
                            painter = painterResource(id = R.drawable.not_found),
                            contentDescription = "error",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(imgSizeInDp)
                                .padding(5.dp)
                        )
                    },
                    loading = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppBarCollapsedHeight),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = product.name!!,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier
                        .padding(horizontal = (16 + 28 * offsetProgress).dp)
                        .scale(1f - 0.25f * offsetProgress)
                )
            }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(AppBarCollapsedHeight)
            .padding(horizontal = 16.dp)
    ) {
        Button(
            onClick = { goBackOnClick() },
            contentPadding = PaddingValues(),
            shape = Shapes.small,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = color_60p,
                contentColor = textColor
            ),
            elevation = ButtonDefaults.elevation(),
            modifier = Modifier
                .width(38.dp)
                .height(38.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back Arrow",
                modifier = Modifier
                    .width(25.dp)
                    .height(25.dp),
                tint = textColor
            )
        }
    }
}

@Composable
fun Content(scrollState: LazyListState, product: Product) {
//    list of int upto 20
    val list = listOf(1..20).toList()
    val isLight = MaterialTheme.colors.isLight
    val color_60p = if (isLight) color_60p_light else color_60p_dark
    var textColor = if (isLight) Color.Black else Color.White

    LazyColumn(
        contentPadding = PaddingValues(top = AppBarExpendedHeight),
        state = scrollState
    ) {
        item {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "$${product.price}",
                    fontWeight = FontWeight.W300,
                    color = grad1_10p
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Description:",
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = product.description!!,
                    color = textColor,
                    style = MaterialTheme.typography.body1,
                )
                //reviews
                Text(
                    text = "Reviews:",
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(top = 16.dp)
                )
//                for (i in reviews) {
                Review(review = review)
                Review(review = review)
                Review(review = review)
                Spacer(modifier = Modifier.height(50.dp))

//                }
            }
        }
    }
}


data class Review(
    val title: String,
    val description: String,
    val rating: Float,
    val userId: String,
    val userPicture: String,
    val productId: String?
)

//generate list of user reviews
val review = Review(
    "ornare, lectus ante dictum mi, ac mattis",
    "ultrices iaculis odio. Nam interdum enim non nisi. Aenean eget metus. In nec orci" +
            ". Donec nibh. Quisque nonummy ipsum non arcu. V" +
            "ivamus sit amet risus. Donec egestas. Aliqua" +
            "m nec enim. Nunc ut erat. Sed nunc est, moll" +
            "is non, cursus non, egestas a, dui. Cras pe" +
            "llentesque. Sed dictum. Proin eget odio. Al" +
            "iquam vulputate ullamcorper magna. Sed eu eros.",
    4.5f,
    "user9",
    "https://i.pravatar.cc/300",
    "product9"
)


@Composable
fun Review(review: Review) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(end = 16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CoilImage(
                    imageModel = review.userPicture,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Fit,
                    failure = {
                        Image(
                            painter = painterResource(id = R.drawable.not_found),
                            contentDescription = "error",
                            contentScale = ContentScale.Fit,
                        )
                    },
                    loading = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()

                        }
                    }
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = review.userId,
                    fontWeight = FontWeight.W300,
                    color = grad1_10p,
                    style = MaterialTheme.typography.caption,
                    overflow = TextOverflow.Ellipsis,
                )
            }

        }
        Row() {

            Column {
                Text(
                    text = review.title,
                    color = grad1_10p,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = review.description,
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.body2,
                )
                Spacer(modifier = Modifier.height(16.dp))
                RatingBar(
                    rating = review.rating.toFloat(),
                    modifier = Modifier.height(20.dp)
                )
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
        Surface {
            Review(review = review)
        }
    }


}