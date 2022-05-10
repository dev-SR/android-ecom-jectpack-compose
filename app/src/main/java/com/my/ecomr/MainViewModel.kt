package com.my.ecomr

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.ecomr.domains.models.Todo
import com.my.ecomr.domains.services.TodoRepository
import com.my.ecomr.navigations.Screens
import com.google.firebase.auth.AuthCredential
import com.my.ecomr.domains.models.Product
import com.my.ecomr.domains.models.User
import com.my.ecomr.domains.services.AuthRepository
import com.my.ecomr.domains.services.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Used to communicate between screens.
 */

//data class User(var id: String, var name: String)
//data class Product(var id: String, var name: String)
//data class CartItem(var product: Product, var qty: Int)
//data class CartInfo(var userid: String, var cartItems: MutableList<CartItem>)

//val user = User("1", "Jhon")

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
//    private val cartService: CartService,
    private val todoRepository: TodoRepository,
    private val authRepository: AuthRepository,
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn
    private val _authStatus = mutableStateOf<Response<User>?>(null)
    val authStatus: State<Response<User>?> = _authStatus

    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user

    private val _newArrivalProducts = mutableStateOf<Response<List<Product>>>(Response.Loading)
    val newArrivalProducts: State<Response<List<Product>>> = _newArrivalProducts

    private val _topProducts = mutableStateOf<Response<List<Product>>>(Response.Loading)
    val topProducts: State<Response<List<Product>>> = _topProducts

    private val _bestSellerProducts = mutableStateOf<Response<List<Product>>>(Response.Loading)
    val bestSellerProducts: State<Response<List<Product>>> = _bestSellerProducts

    private val _product = mutableStateOf<Response<Product>>(Response.Loading)
    val product: State<Response<Product>> = _product

    private val _todos = mutableStateOf<Response<List<Todo>>>(Response.Loading)
    val todos: State<Response<List<Todo>>> = _todos

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()


//    private val _cartInfo = mutableStateOf<CartInfo?>(null)
//    val cartInfo: State<CartInfo?> = _cartInfo

    private val _value = MutableStateFlow(0)
    val value: StateFlow<Int> = _value

    init {
        viewModelScope.launch {
            delay(500L)
            _loading.emit(false)
        }
        val user = authRepository.getCurrentUser()
        if (user != null) {
            _isLoggedIn.value = true
            _user.value = user
        }
        getProducts()
        getTopProducts()
        getBestSellers()
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logOut()
            _isLoggedIn.value = false
        }
    }

    fun signWithGoogleCredential(credential: AuthCredential) = viewModelScope.launch {
        authRepository.signWithGoogleCredential(credential).collect { response ->
            _authStatus.value = response
            when (response) {
                is Response.Success -> {
                    _isLoggedIn.value = true
                }
                else -> null
            }
        }
    }

    private fun getProducts() {
        viewModelScope.launch {
            productRepository.getProductsFromFirestore().collect { response ->
                _newArrivalProducts.value = response
            }
        }
    }

    private fun getTopProducts() {
        viewModelScope.launch {
            productRepository.getTopProducts().collect { response ->
                _topProducts.value = response
            }
        }
    }

    private fun getBestSellers() {
        viewModelScope.launch {
            productRepository.getBestSeller().collect { response ->
                _bestSellerProducts.value = response
            }
        }
    }

    fun getProduct(productId: String) {
        viewModelScope.launch {
            productRepository.getProduct(productId).collect { response ->
                _product.value = response
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


//    fun addToCart(user: User, product: Product, qty: Int) {
//        viewModelScope.launch {
//            val existing = cartInfo.value;
//            if (existing == null) {
//                val cartItem = CartItem(product = product, qty = qty)
//                val newCartInfo = CartInfo(user.id, mutableListOf(cartItem))
////                Log.d("debug", newCartInfo.toString())
//                _cartInfo.value = newCartInfo
//            } else {
//                if (existing!!.userid == user.id) {
//                    //find if product already exists in cart
//                    val cartItem = existing.cartItems.find { it.product.id == product.id }
//                    if (cartItem != null) {
//                        //update existing cart item
//                        val updatedCartItems = existing.cartItems.map {
//                            if (it.product.id == product.id) {
//                                it.copy(qty = it.qty + qty)
//                            } else {
//                                it
//                            }
//                        }
//                        val updatedCartInfo =
//                            existing.copy(cartItems = updatedCartItems as MutableList<CartItem>)
//                        _cartInfo.value = updatedCartInfo
//                    } else {
//                        //add new cart item
//                        val newCartItem = CartItem(product, qty)
//                        existing.cartItems.add(newCartItem)
//                        _cartInfo.value = existing
//                    }
//                }
//            }
//        }
//    }
//
//    fun increaseQty(product: Product, user: User) {
//        viewModelScope.launch {
//            var cartInfo = cartInfo.value
//
//            if (user.id == cartInfo?.userid) {
//                var cartItems = cartInfo.cartItems.map {
//                    if (it.product.id == product.id) {
////                        WATCH OUT
////                        it.qty += 1
////                        it
//                        /**
//                         * Mutable state cannot check if the state of your internal object has changed,
//                         * it can only check if it is the same object or not.so we need create new `CartItem`
//                         * with updated `qty` value
//                         *
//                         * */
//                        CartItem(it.product, it.qty + 1)
//                    } else
//                        it
//                }
//                _cartInfo.value = cartInfo.copy(cartItems = cartItems as MutableList<CartItem>)
//            }
//        }
//    }
//
//    fun decreaseQty(product: Product, user: User) {
//        viewModelScope.launch {
//            var cartInfo = cartInfo.value
//            if (user.id == cartInfo?.userid) {
//                var cartItems = cartInfo.cartItems.map {
//                    if (it.product.id == product.id) {
//                        var qty = it.qty
//                        if (qty > 0) {
//                            qty -= 1
//                        }
//                        CartItem(it.product, qty = qty)
//                    } else
//                        it
//
//                }
//                _cartInfo.value = cartInfo.copy(cartItems = cartItems as MutableList<CartItem>)
//            }
//        }
//    }

}