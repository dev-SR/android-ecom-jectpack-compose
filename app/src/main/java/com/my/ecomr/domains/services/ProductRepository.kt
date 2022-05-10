package com.my.ecomr.domains.services

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.my.ecomr.Response
import com.my.ecomr.di.ProductRef
import com.my.ecomr.domains.models.Product
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductRepository @Inject constructor(
    @ProductRef // will be injected by hilt
    private val productRef: CollectionReference
) {

    // private val productRepository = Firebase.firestore.collection("product_collection")
    suspend fun getProductsFromFirestore() = callbackFlow {
        val snapshotListener =
            productRef.orderBy("created_at").limit(10).addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null) {
                    val products = snapshot.toObjects<Product>()
                    Response.Success(products)
                } else {
                    Response.Error(e?.message ?: e.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    suspend fun getTopProducts() = flow {
        emit(Response.Loading)
        val products =
            productRef.orderBy("sold", Query.Direction.DESCENDING).limit(10).get().await()
                .toObjects<Product>()
        emit(Response.Success(products))
    }

    suspend fun getBestSeller() = flow {
        emit(Response.Loading)
        val products = productRef.whereGreaterThan("sold", 50).limit(10).get().await()
            .toObjects<Product>()
        emit(Response.Success(products))
    }

    suspend fun getProduct(productId: String) = flow {
        emit(Response.Loading)
        productRef.document(productId).get().await().toObject<Product>()?.let {
            Log.d("ProductRepository", it.toString())
            emit(Response.Success(it))
        } ?: run {
//            Log.d("ProductRepository", "Product not found")

            emit(Response.Error("Product not found"))
        }

    }

//        emit(Response.Success(product))
}