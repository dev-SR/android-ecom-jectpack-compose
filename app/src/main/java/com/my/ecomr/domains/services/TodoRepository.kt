package com.my.ecomr.domains.services

import com.my.ecomr.Response
import com.my.ecomr.di.TodoRef
import com.my.ecomr.domains.models.Todo
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class TodoRepository @Inject constructor(
    @TodoRef // todoRef will be injected by hilt
    private val todoRef: CollectionReference
) {

    // private val todoRef = Firebase.firestore.collection("todo_collection")
    suspend fun getTodosFromFirestore() = callbackFlow {
        val snapshotListener = todoRef.addSnapshotListener { snapshot, e ->
            val response = if (snapshot != null) {
                val books = snapshot.toObjects<Todo>()
                Response.Success(books)
            } else {
                Response.Error(e?.message ?: e.toString())
            }
            trySend(response).isSuccess
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

}