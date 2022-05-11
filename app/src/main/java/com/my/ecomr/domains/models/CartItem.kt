package com.my.ecomr.domains.models

import com.google.firebase.firestore.DocumentId

data class CartItem(
    val productId: String? = "",
    val userId: String? = "",
    val qty: Int? = 0,
    val product: Product? = null,
    @DocumentId var cartId: String? = ""
)