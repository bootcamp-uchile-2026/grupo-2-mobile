package cl.uchile.dcc.mobile.ecotienda.viewmodel

import androidx.lifecycle.ViewModel
import cl.uchile.dcc.mobile.ecotienda.model.Cart
import cl.uchile.dcc.mobile.ecotienda.model.Product
import cl.uchile.dcc.mobile.ecotienda.model.toCartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartViewModel : ViewModel() {
    private val _cart = MutableStateFlow(Cart())
    val cart = _cart.asStateFlow()

    fun addToCart(product: Product, quantity: Int) {
        _cart.update { currentCart ->
            val newItem = product.toCartItem(quantity)
            val updatedItems = currentCart.items + newItem
            currentCart.copy(items = updatedItems)
        }
    }

    fun clearCart() {
        _cart.value = Cart()
    }
}
