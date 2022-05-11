@file:OptIn(ExperimentalMaterialApi::class)

package com.my.ecomr.screens.mains

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
//import coil.compose.AsyncImage
import com.my.ecomr.R
import com.my.ecomr.ui.theme.*

class ProductLocal(
    val id: String,
    val name: String,
    val price: Int,
    val qty: Int,
    val description: String,
    val image_url: Int
)

val heroProduct = ProductLocal(
    "id6",
    "IPhone",
    100,
    10,
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    R.drawable.id1
)

val listOfProduct = listOf<ProductLocal>(
    ProductLocal(
        "id1",
        "IPhone 14 Pro Max 256GB",
        100,
        10,
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        R.drawable.id1

    ),
    ProductLocal(
        "id2",
        "IPhone",
        100,
        10,
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        R.drawable.id2

    ),
    ProductLocal(
        "id3",
        "IPhone",
        100,
        10,
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        R.drawable.id3


    ),
    ProductLocal(
        "id4",
        "IPhone",
        100,
        10,
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        R.drawable.id4
    ),
    ProductLocal(
        "id5",
        "IPhone",
        100,
        10,
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        R.drawable.id5
    ),

    ProductLocal(
        "id6",
        "IPhone",
        100,
        10,
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        R.drawable.id6
    )
)

class CategoryLocal(val name: String, val isSelected: Boolean = false)

val listOfCategory = listOf<CategoryLocal>(
    CategoryLocal("All", isSelected = true),
    CategoryLocal("Clothing"),
    CategoryLocal("Cell Phones"),
    CategoryLocal("Laptop"),
    CategoryLocal("Accessory"),
    CategoryLocal("Books")
)

@Composable
fun HomeScreenLocal(
    heroProductLocal: ProductLocal,
    popularsNow: List<ProductLocal>,
    topProductLocals: List<ProductLocal>,
    categoryLocalList: List<CategoryLocal>,
    goToCategory: () -> Unit = {},
    goToDetails: () -> Unit = {},
    seeAllPopular: () -> Unit = {},
    seeAllTop: () -> Unit = {},
    addToCart: () -> Unit = {},
) {


    LazyColumn {
        item {
            Spacer(modifier = Modifier.height(60.dp))
            HeroProductLocal(heroProductLocal)
        }
        item { CategoriesLocal(categoryLocalList) }
        item {
            TagLocal(s = "Popular Now")
        }
        item { TopProductsLocal(topProductLocals) }
        item {
            TagLocal(s = "Top Products")
        }
        item { TopProductsLocal(topProductLocals) }
        item {
            TagLocal(s = "Best Sellers")
        }
        item {
            TopProductsLocal(topProductLocals)
            Spacer(modifier = Modifier.height(100.dp))
        }
    }

}

@Composable
fun TagLocal(s: String, seeAllPopular: () -> Unit = {}) {
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
fun HeroProductLocal(
    heroProductLocal: ProductLocal,
    goToDetails: () -> Unit = {},
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 0.dp)
    ) {
        Card(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(top = 40.dp),
            backgroundColor = Color.Transparent,
            onClick = {
                goToDetails()
                Toast.makeText(context, "Hero Card Clicked", Toast.LENGTH_SHORT).show()
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
                                goToDetails()
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
fun CategoriesLocal(
    categoryLocalList: List<CategoryLocal>,
    goToCategory: () -> Unit = {},
) {
//    val configuration = LocalConfiguration.current
//    val leftAndRightPads = 40
//    val innerSpace = 20
//    val availableWidth = configuration.screenWidthDp - (leftAndRightPads+innerSpace)
////  convert to int
//    val itemWidth = (availableWidth / 2)
//    Log.d("itemWidth", itemWidth.toString())


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
fun PopularNowLocal(
    popularsNow: List<ProductLocal>,
    goToDetails: () -> Unit = {},

    ) {
    Text(text = "")
}

@Composable
fun TopProductsLocal(
    topProductLocals: List<ProductLocal>,
    goToDetails: () -> Unit = {},

    ) {
    val context = LocalContext.current
    val shuffledItems = remember(topProductLocals) {
        topProductLocals.shuffled()
    }

    LazyRow(modifier = Modifier.padding(top = 10.dp, start = 10.dp, bottom = 10.dp)) {
        items(shuffledItems) { product ->
            ProductCardLocal(product, goToDetails)
        }
        item {
            ProductMoreCard()
        }
    }
}

@Composable
fun ProductMoreCard(seeAll: () -> Unit = {}) {
    val isLight = MaterialTheme.colors.isLight
    val color_30p = if (isLight) color_30p_light else color_30p_dark
    Card(
        modifier = Modifier
            .size(width = 188.dp, height = 190.dp)
            .padding(horizontal = 10.dp),
        elevation = 5.dp,
        shape = RoundedCornerShape(10.dp),
//                backgroundColor = Color.Transparent,
        onClick = {
            seeAll()
        }
    ) {
        Box(
            Modifier
                .background(color_30p)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

                ) {
                Text(
                    text = "See More",
                    style = MaterialTheme.typography.body1,
                    color = Color.Gray
                )
            }
        }
    }
}


@Composable
fun ProductCardLocal(
    product: ProductLocal,
    goToDetails: () -> Unit = {},
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
//                backgroundColor = Color.Transparent
        onClick = {
            Toast.makeText(context, "Go to Details", Toast.LENGTH_SHORT).show()
        }
    ) {
        Box(
            Modifier
                .background(color_30p),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
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


                Image(
                    painter = painterResource(id = product.image_url),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imgSizeInDp)
                        .padding(start = 10.dp, end = 10.dp, top = 5.dp),
                    contentScale = ContentScale.Fit,
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp, end = 10.dp, top = 5.dp)
                        .background(color_30p),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = product.name,
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
//                                goToDetails()
                        Toast
                            .makeText(
                                context,
                                "Add To Cart", Toast.LENGTH_SHORT
                            )
                            .show()
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


@Preview(name = "Light Mode", showBackground = true)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun HomePreviewLocal() {


    EcomTheme {
        val isLight = MaterialTheme.colors.isLight
        val color_60p = if (isLight) color_60p_light else color_60p_dark
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = color_60p
        ) {
            HomeScreenLocal(
                heroProductLocal = heroProduct,
                popularsNow = listOfProduct,
                topProductLocals = listOfProduct,
                categoryLocalList = listOfCategory,
            )
        }
    }
}