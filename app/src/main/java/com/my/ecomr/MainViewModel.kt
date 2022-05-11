package com.my.ecomr

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.ecomr.domains.models.Todo
import com.my.ecomr.domains.services.TodoRepository
import com.my.ecomr.navigations.Screens
import com.google.firebase.auth.AuthCredential
import com.my.ecomr.domains.models.CartItem
import com.my.ecomr.domains.models.Product
import com.my.ecomr.domains.models.User
import com.my.ecomr.domains.services.AuthRepository
import com.my.ecomr.domains.services.CartRepository
import com.my.ecomr.domains.services.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Used to communicate between screens.
 */

//data class User(var id: String, var name: String)
//data class Product(var id: String, var name: String)
//data class CartItem(var productId: Product, var qty: Int)
//data class CartInfo(var userid: String, var cartItems: MutableList<CartItem>)

//val user = User("1", "Jhon")



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
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository

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

    private val _selectedCarts = mutableStateOf<MutableList<String>>(mutableListOf())
    val selectedCarts: State<List<String>> = _selectedCarts

//    private val _selectedCarts2 = mutableStateListOf<String>()
//    val selectedCarts2: SnapshotStateList<String> = _selectedCarts2


    private val _cartInfo = mutableStateOf<Response<List<CartItem>>>(Response.Success(emptyList()))
    val cartInfo: State<Response<List<CartItem>>> = _cartInfo
    private val _cartInfoIds = mutableStateOf<MutableList<String>>(
        mutableListOf()
    )
    val cartInfoIds: State<List<String>> = _cartInfoIds


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
            Log.d("trace:user", user.toString())
        }
        getNewArrivalProducts()
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

    private fun getNewArrivalProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.getProductsFromFirestore().collect { response ->
                withContext(Dispatchers.Main) {
                    _newArrivalProducts.value = response
                }
            }
        }
    }

    private fun getTopProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.getTopProducts().collect { response ->
                withContext(Dispatchers.Main) {
                    _topProducts.value = response
                }
            }
        }
    }

    private fun getBestSellers() {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.getBestSeller().collect { response ->
                withContext(Dispatchers.Main) {
                    _bestSellerProducts.value = response
                }
            }
        }
    }

    fun getProduct(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.getProduct(productId).collect { response ->
                withContext(Dispatchers.Main) {
                    _product.value = response
                }
            }
        }
    }

    private fun getProductsByIds(ids: List<String>) {
        viewModelScope.launch {
            Log.d("trace:ids", ids.toString())
            productRepository.getProductsByIds(ids).collect { response ->
                Log.d("trace:response", response.toString())
                when (response) {
                    is Response.Success -> {
                        response.data.forEach {
                            CartItem()
                        }
                    }
                    else -> {
                        Log.d("trace:products", "error")
                    }
                }
            }
        }
    }

    fun getCartInfo() {
        viewModelScope.launch {
            val user = authRepository.getCurrentUser()
            cartRepository.getCartInfo(user?.userId!!).collect { response ->
                when (response) {
                    is Response.Success -> {
                        val carts = response.data
                        val ids = mutableListOf<String>()
                        response.data.forEach {
                            it.productId?.let { it1 -> ids.add(it1) }
                        }
                        val cartInfo = mutableListOf<CartItem>()
                        productRepository.getProductsByIds(ids).collect { response ->
                            when (response) {
                                is Response.Success -> {
                                    response.data.forEach { product ->
                                        //find cart item with product id = it.productId
                                        val cartItem =
                                            carts.find { it.productId == product.productId }
                                        val newCartItem = CartItem(
                                            cartId = cartItem?.cartId,
                                            productId = cartItem?.productId,
                                            product = product,
                                            qty = cartItem?.qty,
                                            userId = cartItem?.userId
                                        )
                                        cartInfo.add(newCartItem)
                                    }
                                    _cartInfo.value = Response.Success(cartInfo)
                                }
                                else -> {
                                    Log.d("trace:products", "error")
                                }
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun addToCart(productId: String, qty: Int = 1) {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUser()?.userId!!
            cartRepository.addToCart(productId, userId = userId, qty = qty).collect { response ->

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

    fun increaseQty(cart: CartItem) {
        cartRepository.increaseQty(cart)
    }

    fun decreaseQty(cart: CartItem) {
        cartRepository.decreaseQty(cart)
    }

    fun removeSelectedFromCart() {
        cartRepository.removeSelectedFromCart(selectedCarts.value)
    }

    fun selectOrDeselect(cartId: String) {
            val selected = _selectedCarts.value
            val alreadySelected = selected.contains(cartId)
            if (!alreadySelected) {
//                selected.add(cartId)
                _selectedCarts.value = (selected + mutableListOf(cartId)) as MutableList<String>
            } else {
//                selected.remove(cartId)
                _selectedCarts.value = selected.toMutableList().also { it.remove(cartId) }
            }
    }
}