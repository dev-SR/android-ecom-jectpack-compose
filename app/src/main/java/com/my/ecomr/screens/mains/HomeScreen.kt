@file:OptIn(ExperimentalMaterialApi::class)

package com.my.ecomr.screens.mains

import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
//import coil.compose.*
//import coil.request.ImageRequest
//import coil.transform.Transformation
import com.my.ecomr.MainViewModel
import com.my.ecomr.R
import com.my.ecomr.Response
import com.my.ecomr.domains.models.Product
import com.my.ecomr.navigations.Screens
import com.my.ecomr.ui.theme.*
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun HomeScreen(
    navController: NavController, viewModel: MainViewModel
) {
    viewModel.setCurrentScreen(Screens.HomeScreens.Home)
    val goToProductDetail: (Product) -> Unit = { product: Product ->
        navController.navigate(
            Screens.ProductScreens.Details.routeToDetailsOf(product.productId!!)
        )
    }
    val addToCart: (String) -> Unit = { productId ->
//        viewModel.addToCart(productId)
        if (!viewModel.isLoggedIn.value) {
            navController.navigate(
                route = Screens.AuthScreens.Login.reroute(
                    "cart",
                    productId = productId
                )
            )
        } else {
            navController.navigate(route = Screens.HomeScreens.Cart.addNewProductToCart(productId))
        }

    }
    Home(
        viewModel.newArrivalProducts.value,
        viewModel.topProducts.value,
        viewModel.bestSellerProducts.value,
        goToProductDetail,
        addToCart
    )

}

//val data:Response<List<Product>> = Response.Success(listOf( ))


@Composable
fun Home(
    products: Response<List<Product>>,
    topProducts: Response<List<Product>>,
    bestSellerProducts: Response<List<Product>>,
    goToProductDetail: (Product) -> Unit,
    addToCart: (String) -> Unit

) {
    LazyColumn {
        item {
            TopBarHome()
        }
        item {
            when (products) {
                is Response.Loading -> {
                    HeroProduct(Product("New Arrival"))
                }
                is Response.Success -> {
                    HeroProduct(products.data[0], goToProductDetail)
                }
                is Response.Error -> {
                    HeroProduct(Product("New Arrival"))
                }
            }

        }
        item { CategoriesLocal(listOfCategory) }
        item {
            TagLocal(s = "New Arrival")
        }
        item {
            when (products) {
                is Response.Loading -> {
                    Column(
                        modifier = Modifier.height(200.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Response.Success -> {
                    TopProducts(products.data, goToProductDetail, addToCart)
                }
                is Response.Error -> {
                    Text("Couldn't fetching data")
                }
            }
        }
        item {
            TagLocal(s = "Top Products")
        }
        item {
            when (topProducts) {
                is Response.Loading -> {
                    Column(
                        modifier = Modifier.height(200.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Response.Success -> {
                    TopProducts(topProducts.data, goToProductDetail, addToCart)
                }
                is Response.Error -> {
                    Text("Couldn't fetching data")
                }
            }
        }
        item {
            TagLocal(s = "Best Sellers")
        }
        item {
            when (bestSellerProducts) {
                is Response.Loading -> {
                    Column(
                        modifier = Modifier.height(200.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Response.Success -> {
                    TopProducts(bestSellerProducts.data, goToProductDetail, addToCart)
                }
                is Response.Error -> {
                    Text("Couldn't fetching data")
                }
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }

}

@Composable
fun TopBarHome() {
    val isLight = MaterialTheme.colors.isLight
    val color_60p = if (isLight) color_60p_light else color_60p_dark
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 0.dp, start = 20.dp, end = 20.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 0.dp,
        backgroundColor = color_60p
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Search Products...", modifier = Modifier.padding(horizontal = 20.dp),
                style = MaterialTheme.typography.body2,
                color = Color.Gray
            )
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(end = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search, contentDescription = "",
                    tint = Color.Gray
                )
            }
        }

    }
}


@Composable
fun Tag(s: String, seeAllPopular: () -> Unit = {}) {
    val isLight = MaterialTheme.colors.isLight
    var textColor = if (isLight) Color.Black else Color.White

    var sellAllColor = Color.Gray
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = s,
            fontWeight = FontWeight.Bold,
            color = textColor,
            style = MaterialTheme.typography.h2
        )
        Text(text = "see all", style = MaterialTheme.typography.body2, color = sellAllColor)
    }


}

@Composable
fun HeroProduct(
    heroProduct: Product,
    goToProductDetail: (Product) -> Unit = {}
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .padding(top = 0.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
    ) {
        Card(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(top = 40.dp),
            backgroundColor = Color.Transparent,
            onClick = {
                goToProductDetail(heroProduct)
            }
        ) {
            Box(
                Modifier
                    .background(brush = color_10p),
            ) {
                var dSize by remember { mutableStateOf(0f) }
                val dSizeInF by animateFloatAsState(
                    targetValue = dSize,
                    animationSpec = tween(
                        durationMillis = 1500,
                        easing = FastOutSlowInEasing
                    )
                )
                LaunchedEffect(Unit) {
                    dSize = 1f
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth(.7f)
                        .fillMaxHeight(dSizeInF)
                        .padding(top = 20.dp, start = 25.dp, end = 20.dp, bottom = 10.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {

                        Text(
                            "New Arrival",
                            fontWeight = FontWeight.W700,
                            style = MaterialTheme.typography.caption
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            "Nike Air Max 270",
                            fontWeight = FontWeight.W700,
                            style = MaterialTheme.typography.h1,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            color = Color.White
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(0.dp)

                    ) {
                        Card(
                            modifier = Modifier
                                .clickable(
                                    onClick = {

                                    }),
                            backgroundColor = grad2_10p,
                            contentColor = Color.DarkGray,
                            elevation = 0.dp,
                            shape = RoundedCornerShape(7.dp),
                            border = BorderStroke(1.dp, Color.White),
                            onClick = {
//                                goToDetails()
                                Toast.makeText(context, "Buy Now Clicked", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        ) {
                            Column(
//                                horizontalAlignment = Alignment.CenterHorizontally,
//                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    "Buy Now",
                                    style = MaterialTheme.typography.caption,
                                    color = Color.White,
                                    modifier = Modifier.padding(
                                        horizontal = 20.dp,
                                        vertical = 10.dp
                                    )
                                )
                            }
                        }
                    }


                }
            }

        }
        var imgSize by remember { mutableStateOf(100.dp) }
        val imgSizeInDp by animateDpAsState(
            targetValue = imgSize,
            animationSpec = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            )
        )
        LaunchedEffect(Unit) {
            imgSize = 170.dp
        }
        Image(
            painter = painterResource(id = R.drawable.id1),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .size(imgSizeInDp)
                .align(alignment = Alignment.CenterEnd)
                .offset(x = (20).dp)
        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Categories(
    categoryLocalList: List<CategoryLocal>,
    goToCategory: () -> Unit = {},
) {
    val context = LocalContext.current

    LazyRow(modifier = Modifier.padding(top = 10.dp, start = 10.dp, bottom = 10.dp)) {
        items(categoryLocalList) { cat ->
            val isLight = MaterialTheme.colors.isLight
            var cardBackgroundColor: Color = color_60p_light
            var borderStrokeColor: Color = Color.Black
            var textColor: Color = Color.Black
            if (isLight) {
                if (cat.isSelected) {
                    cardBackgroundColor = color_60p_light
                    borderStrokeColor = grad1_10p
                    textColor = grad1_10p
                } else {
                    cardBackgroundColor = color_60p_light
                    borderStrokeColor = Color.LightGray
                    textColor = Color.LightGray
                }
            } else {
                if (cat.isSelected) {
                    cardBackgroundColor = color_60p_dark
                    borderStrokeColor = grad1_10p
                    textColor = grad1_10p
                } else {
                    cardBackgroundColor = color_60p_dark
                    borderStrokeColor = Color.DarkGray
                    textColor = Color.DarkGray
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),

                backgroundColor = cardBackgroundColor,
                elevation = 0.dp,
                shape = RoundedCornerShape(7.dp),
                border = BorderStroke(1.dp, borderStrokeColor),
                onClick = {
                    Toast.makeText(context, "Car Clicked", Toast.LENGTH_SHORT).show()
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 30.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = cat.name, color = textColor,
                        style = MaterialTheme.typography.body2
                    )
                }
            }

        }
    }

}

@Composable
fun PopularNow(
    popularsNow: List<Product>,
    goToDetails: () -> Unit = {},

    ) {
    Text(text = "")
}

@Composable
fun TopProducts(
    topProducts: List<Product>,
    goToProductDetail: (Product) -> Unit,
    addToCart: (String) -> Unit


) {
    val context = LocalContext.current
    LazyRow(modifier = Modifier.padding(top = 10.dp, start = 10.dp, bottom = 10.dp)) {
        items(topProducts) { product ->
            ProductCard(product, goToProductDetail, addToCart)
        }
        item {
            ProductMoreCard()
        }
    }
}


@Composable
fun ProductCard(
    product: Product,
    goToProductDetail: (Product) -> Unit,
    addToCart: (String) -> Unit
) {
    val context = LocalContext.current
    val isLight = MaterialTheme.colors.isLight
    val color_30p = if (isLight) color_30p_light else color_30p_dark
    Card(
        modifier = Modifier
            .size(width = 188.dp, height = 190.dp)
            .padding(horizontal = 10.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(10.dp),
        onClick = {
            goToProductDetail(product)
        }
    ) {
        Box(
            Modifier
                .background(color_30p),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
            ) {

                var imgSize by remember { mutableStateOf(0.dp) }
                val imgSizeInDp by animateDpAsState(
                    targetValue = imgSize,
                    animationSpec = tween(
                        durationMillis = 1000,
                        easing = FastOutSlowInEasing
                    )
                )
                LaunchedEffect(Unit) {
                    imgSize = 120.dp
                }
                CoilImage(
                    imageModel = product.img!!,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imgSizeInDp)
                        .padding(5.dp),
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
                                .height(imgSizeInDp)
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



                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp, end = 10.dp, top = 5.dp)
                        .background(color_30p),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = product.name!!,
                        style = MaterialTheme.typography.body1,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,

                        )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "$${product.price}",
                        fontWeight = FontWeight.W300,
                        color = grad1_10p
                    )
                }


            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clip(
                        RoundedCornerShape(
                            topStart = 15.dp,
                            topEnd = 0.dp,
                            bottomEnd = 10.dp,
                            bottomStart = 0.dp
                        )
                    )
                    .background(color_10p)
                    .clickable {
                        addToCart(product.productId!!)
                    },
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "add",
                    modifier = Modifier.padding(5.dp),
                    tint = Color.White,
                )
            }
        }
    }
}

//@Composable
//fun NetworkImage(
//    url: String,
//    contentDescription: String = "",
//    modifier: Modifier,
//    alignment: Alignment = Alignment.Center,
//    contentScale: ContentScale = ContentScale.Fit,
//    alpha: Float = DefaultAlpha,
//    colorFilter: ColorFilter? = null,
//    placeholderDrawableRes: Int? = null,
//    crossFade: Int? = null,
//    transformations: List<Transformation>? = null,
//    onLoading: @Composable () -> Unit = {},
//    onError: @Composable () -> Unit = {}
//) {
//    Box(
//        modifier = modifier
//    ) {
//        val painter = rememberAsyncImagePainter(
//            ImageRequest.Builder(LocalContext.current).data(data = url)
//                .apply(block = fun ImageRequest.Builder.() {
//                    placeholderDrawableRes?.let {
//                        placeholder(R.drawable.placeholder)
//                    }
//                    error(
//                        R.drawable.not_found
//                    )
//                    crossFade?.let {
//                        crossfade(durationMillis = it)
//                    }
////                    transformations?.let {
////                        transformations(transformations = it)
////                    }
//                }).build()
//        )
//        val imageState = painter.state
//
//        if (imageState is AsyncImagePainter.State.Loading) {
////            onLoading()
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight(),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        }
//        if (imageState is AsyncImagePainter.State.Error) {
////            onError()
//            Image(
//                painter = painter,
//                contentDescription = contentDescription,
//                contentScale = contentScale,
//                modifier = modifier
//            )
//        }
////        Image(
////            painter = painter,
////            contentDescription = contentDescription,
////            contentScale = contentScale,
////            alignment = alignment,
////            alpha = alpha,
////            colorFilter = colorFilter,
////            modifier = modifier
////        )
//
//
//
//    }
//}