package com.my.ecomr.domains.models

import com.google.firebase.firestore.DocumentId

data class User(
    val email: String,
    val displayName: String,
    @DocumentId var userId: String? = "",
)