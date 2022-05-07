package com.my.ecomr.domains.models

import com.google.firebase.firestore.DocumentId

data class Todo(
    var title: String? = "",
    @DocumentId var todoId: String? = "",
)