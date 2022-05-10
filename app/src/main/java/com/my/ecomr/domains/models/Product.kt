package com.my.ecomr.domains.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import java.util.*

data class Product(
    var name: String? = "",
    var price: Int? = 0,
    var description: String? = "",
    var img: String? = "",
    var category: String? = "",
    var sold: Int? = 0,
    var qty: Int? = 0,
    var created_at: Date? = Date(),
    @DocumentId var productId: String? = "",
)