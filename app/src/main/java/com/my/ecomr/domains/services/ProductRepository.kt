package com.my.ecomr.domains.services

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
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


    suspend fun getProductsByIds(ids: List<String>) = flow{
        emit(Response.Loading)
        val products = productRef.whereIn(FieldPath.documentId(), ids).get().await().toObjects<Product>()
        emit(Response.Success(products))
    }

    suspend fun getProductsByCondition(conditions: MutableMap<String, MutableMap<String, Any>>) = flow {
        emit(Response.Loading)
//      Compound queries
        var query = productRef
//        query = query.whereEqualTo("category", "Electronics") as CollectionReference

        //        var conditionType = conditions.keys
        conditions.keys.forEach {
            when (it) {
                "=" -> {
                    conditions[it]?.forEach {
                        val field = it.key
                        val value = it.value
                        query.whereEqualTo(field, value)
                    }
                }
                "in" -> {
                    conditions[it]?.forEach {
                        val field = it.key
                        val value = it.value as List<*>
                        query.whereIn(field, value)
                    }
                }
            }
        }

        val products = query.get().await().toObjects<Product>()
        emit(Response.Success(products))
    }
}

/*

val condition: MutableMap<String, MutableMap<String, Any>> = mutableMapOf(
    "=" to mutableMapOf(
        "name" to "soikat",
        "age" to 20
    ),
    "in" to mutableMapOf(
        "name" to listOf("soikat", "soikat2"),
    ),
    ">" to mutableMapOf(
        "age" to 20
    ),
    "<" to mutableMapOf(
        "age" to 30
    ),
    ">=" to mutableMapOf(
        "age" to 30
    ),
    "<=" to mutableMapOf(
        "age" to 20
    ),
    "!=" to mutableMapOf(
        "name" to "soikat2"
    ),
    "!in" to mutableMapOf(
        "name" to listOf("soikat2", "soikat3")
    ),
)


fun main() {
    println("Hello, world!")
    var conditionType = condition.keys
    println(conditionType)
    condition.keys.forEach {
        when (it) {
            "=" -> {
                condition[it]?.forEach {
                    val field = it.key
                    val value = it.value
                    println("$field = $value")
                }
            }
            "in" -> {
                condition[it]?.forEach {
                    val field = it.key
                    val value = it.value
                    println("$field in $value")
                }
            }
            ">" -> {
                condition[it]?.forEach {
                    val field = it.key
                    val value = it.value
                    println("$field > $value")
                }
            }
            "<" -> {
                condition[it]?.forEach {
                    val field = it.key
                    val value = it.value
                    println("$field < $value")
                }
            }
            ">=" -> {
                condition[it]?.forEach {
                    val field = it.key
                    val value = it.value
                    println("$field >= $value")
                }
            }
            "<=" -> {
                condition[it]?.forEach {
                    val field = it.key
                    val value = it.value
                    println("$field <= $value")
                }
            }
            "!=" -> {
                condition[it]?.forEach {
                    val field = it.key
                    val value = it.value
                    println("$field != $value")
                }
            }
            "!in" -> {
                condition[it]?.forEach {
                    val field = it.key
                    val value = it.value
                    println("$field !in $value")
                }
            }
        }
    }
}

*/