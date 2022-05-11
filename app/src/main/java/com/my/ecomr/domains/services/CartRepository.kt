package com.my.ecomr.domains.services

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.toObjects
import com.my.ecomr.Response
import com.my.ecomr.di.CartRef
import com.my.ecomr.di.ProductRef
import com.my.ecomr.domains.models.CartItem
import com.my.ecomr.domains.models.Product
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CartRepository @Inject constructor(
    @CartRef // will be injected by hilt
    private val cartRef: CollectionReference,
    @ProductRef // will be injected by hilt
    private val productRef: CollectionReference
) {
    suspend fun getCartInfo(userId: String) = callbackFlow {
        val snapshotListener =
            cartRef.whereEqualTo("userId", userId).addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null) {
                    val carts = snapshot.toObjects<CartItem>()
                    Response.Success(carts)
                } else {
                    Response.Error(e?.message ?: e.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    fun increaseQty(cart: CartItem) {
        cartRef.document(cart.cartId!!).update("qty", (cart.qty?.plus(1)))
    }

    fun decreaseQty(cart: CartItem) {
        val qty = cart.qty!!
        if (qty > 1)
            cartRef.document(cart.cartId!!).update("qty", (cart.qty.minus(1)))
    }

    fun removeOneFromCart(cart: CartItem) {
        cartRef.document(cart.cartId!!).delete()
    }

    private fun removeOneFromCartById(cartId: String) {
        cartRef.document(cartId).delete()
    }

    fun removeSelectedFromCart(ids: List<String>) {
        ids.forEach {
            removeOneFromCartById(it)
        }
    }

    suspend fun addToCart(productId: String, userId: String, qty: Int = 1) = flow {
        try {
            emit(Response.Loading)
            val cart = CartItem(productId = productId, userId = userId, qty = qty)
            cartRef.add(cart).await()
            emit(Response.Success(cart))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: e.toString()))
        }
    }
}