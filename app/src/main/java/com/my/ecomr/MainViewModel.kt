package com.my.ecomr

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.ecomr.models.Todo
import com.my.ecomr.models.services.TodoRepository
import com.example.ecomzapp.navigations.Screens
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Used to communicate between screens.
 */

data class User(var id: String, var name: String)
data class Product(var id: String, var name: String)
data class CartItem(var product: Product, var qty: Int)
data class CartInfo(var userid: String, var cartItems: MutableList<CartItem>)

val user = User("1", "Jhon")

class CartService @Inject constructor() {
    init {
        Log.d("injected", "Injected")
    }
}

sealed class Response<out T> {
    object Loading : Response<Nothing>()

    data class Success<out T>(
        val data: T
    ) : Response<T>()

    data class Error(
        val message: String
    ) : Response<Nothing>()
}


@HiltViewModel
class MainViewModel @Inject constructor(
    private val cartService: CartService,
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    private val _todos = mutableStateOf<Response<List<Todo>>>(Response.Loading)
    val todos: State<Response<List<Todo>>> = _todos

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _cartInfo = mutableStateOf<CartInfo?>(null)
    val cartInfo: State<CartInfo?> = _cartInfo

    private val _value = MutableStateFlow(0)
    val value: StateFlow<Int> = _value

    init {
        viewModelScope.launch {
            delay(500L)
            _loading.emit(false)
        }
        val user = auth.currentUser
        Log.d("LOG", user.toString())
        getBooks()

    }

    private fun getBooks() {
        viewModelScope.launch {
            todoRepository.getTodosFromFirestore().collect { response ->
                _todos.value = response
            }
        }
    }

    private val _currentScreen = MutableStateFlow<Screens>(Screens.HomeScreens.Home)
    val currentScreen: StateFlow<Screens> = _currentScreen.asStateFlow()

    fun setCurrentScreen(screen: Screens) {
        viewModelScope.launch {
            _currentScreen.emit(screen)
        }
    }

    fun addToCart(user: User, product: Product, qty: Int) {
        viewModelScope.launch {
            val existing = cartInfo.value;
            if (existing == null) {
                val cartItem = CartItem(product = product, qty = qty)
                val newCartInfo = CartInfo(user.id, mutableListOf(cartItem))
//                Log.d("debug", newCartInfo.toString())
                _cartInfo.value = newCartInfo
            } else {
                if (existing!!.userid == user.id) {
                    //find if product already exists in cart
                    val cartItem = existing.cartItems.find { it.product.id == product.id }
                    if (cartItem != null) {
                        //update existing cart item
                        val updatedCartItems = existing.cartItems.map {
                            if (it.product.id == product.id) {
                                it.copy(qty = it.qty + qty)
                            } else {
                                it
                            }
                        }
                        val updatedCartInfo =
                            existing.copy(cartItems = updatedCartItems as MutableList<CartItem>)
                        _cartInfo.value = updatedCartInfo
                    } else {
                        //add new cart item
                        val newCartItem = CartItem(product, qty)
                        existing.cartItems.add(newCartItem)
                        _cartInfo.value = existing
                    }
                }
            }
        }
    }

    fun increaseQty(product: Product, user: User) {
        viewModelScope.launch {
            var cartInfo = cartInfo.value

            if (user.id == cartInfo?.userid) {
                var cartItems = cartInfo.cartItems.map {
                    if (it.product.id == product.id) {
//                        WATCH OUT
//                        it.qty += 1
//                        it
                        /**
                         * Mutable state cannot check if the state of your internal object has changed,
                         * it can only check if it is the same object or not.so we need create new `CartItem`
                         * with updated `qty` value
                         *
                         * */
                        CartItem(it.product, it.qty + 1)
                    } else
                        it
                }
                _cartInfo.value = cartInfo.copy(cartItems = cartItems as MutableList<CartItem>)
            }
        }
    }

    fun decreaseQty(product: Product, user: User) {
        viewModelScope.launch {
            var cartInfo = cartInfo.value
            if (user.id == cartInfo?.userid) {
                var cartItems = cartInfo.cartItems.map {
                    if (it.product.id == product.id) {
                        var qty = it.qty
                        if (qty > 0) {
                            qty -= 1
                        }
                        CartItem(it.product, qty = qty)
                    } else
                        it

                }
                _cartInfo.value = cartInfo.copy(cartItems = cartItems as MutableList<CartItem>)
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            _isLoggedIn.emit(true)
        }
    }

    fun logout() {
        viewModelScope.launch {
            _isLoggedIn.emit(false)
        }
    }

    fun signWithGoogleCredential(credential: AuthCredential) {
        viewModelScope.launch {
            try {
//                loadingState.emit(LoadingState.LOADING)
                auth.signInWithCredential(credential).await()
                val user = auth.currentUser
                Log.d("LOG", user.toString())

//                loadingState.emit(LoadingState.LOADED)
            } catch (e: Exception) {
//                loadingState.emit(LoadingState.error(e.localizedMessage))
            }
        }
    }
}