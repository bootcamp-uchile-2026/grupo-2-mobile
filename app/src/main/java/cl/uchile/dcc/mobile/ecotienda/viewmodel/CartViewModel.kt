package cl.uchile.dcc.mobile.ecotienda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.ecotienda.data.database.BuenOrigenDatabase
import cl.uchile.dcc.mobile.ecotienda.data.database.Cart
import cl.uchile.dcc.mobile.ecotienda.data.database.Product
import cl.uchile.dcc.mobile.ecotienda.data.database.toCartItem
import cl.uchile.dcc.mobile.ecotienda.data.repository.CartRepository
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.CartCheckoutState
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.CheckoutStep
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.ShippingFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val app = application as? cl.uchile.dcc.mobile.ecotienda.data.repository.EcoTiendaApp
    private val cartRepository = app?.container?.cartRepository ?: CartRepository(
        BuenOrigenDatabase.getInstance(application).cartItemDAO
    )

    private val _cart = MutableStateFlow(Cart())
    val cart = _cart.asStateFlow()

    private val savedStepName: String? = savedStateHandle["checkout_step"]
    private val restoredStep: CheckoutStep = savedStepName?.let {
        runCatching { CheckoutStep.valueOf(it) }.getOrNull()
    } ?: CheckoutStep.CART

    private val _checkoutState = MutableStateFlow(CartCheckoutState(currentStep = restoredStep))
    val checkoutState = _checkoutState.asStateFlow()

    init {
        loadCart()
    }

    private fun loadCart() {
        viewModelScope.launch {
            val items = cartRepository.getCartItems()
            _cart.update { Cart(items = items) }
        }
    }

    fun addToCart(product: Product, quantity: Int) {
        viewModelScope.launch {
            val cartItem = product.toCartItem(quantity)
            cartRepository.addCartItem(cartItem)
            _cart.update { Cart(items = cartRepository.getCartItems()) }
        }
    }

    fun incrementQuantity(productId: String) {
        viewModelScope.launch {
            val currentItem = _cart.value.items.find { it.productId == productId }
            if (currentItem != null) {
                val newQty = currentItem.quantity + 1
                cartRepository.updateQuantity(productId, newQty)
                _cart.update { Cart(items = cartRepository.getCartItems()) }
            }
        }
    }

    fun decrementQuantity(productId: String) {
        viewModelScope.launch {
            val currentItem = _cart.value.items.find { it.productId == productId }
            if (currentItem != null) {
                val newQty = currentItem.quantity - 1
                cartRepository.updateQuantity(productId, newQty)
                _cart.update { Cart(items = cartRepository.getCartItems()) }
            }
        }
    }

    fun removeFromCart(productId: String) {
        viewModelScope.launch {
            cartRepository.removeCartItem(productId)
            _cart.update { Cart(items = cartRepository.getCartItems()) }
        }
    }

    fun nextStep() {
        _checkoutState.update { 
            val nextStep = when(it.currentStep) {
                CheckoutStep.CART -> CheckoutStep.SHIPPING
                CheckoutStep.SHIPPING -> CheckoutStep.PAYMENT
                CheckoutStep.PAYMENT -> CheckoutStep.PAYMENT
            }
            savedStateHandle["checkout_step"] = nextStep.name
            it.copy(currentStep = nextStep)
        }
    }

    fun previousStep() {
        _checkoutState.update { 
            val prevStep = when(it.currentStep) {
                CheckoutStep.CART -> CheckoutStep.CART
                CheckoutStep.SHIPPING -> CheckoutStep.CART
                CheckoutStep.PAYMENT -> CheckoutStep.SHIPPING
            }
            savedStateHandle["checkout_step"] = prevStep.name
            it.copy(currentStep = prevStep)
        }
    }

    fun updateShippingForm(update: (ShippingFormState) -> ShippingFormState) {
        _checkoutState.update { 
            it.copy(shippingForm = update(it.shippingForm))
        }
    }

    fun selectPaymentMethod(method: String) {
        _checkoutState.update { it.copy(paymentMethod = method) }
    }

    fun clearCart() {
        savedStateHandle["checkout_step"] = CheckoutStep.CART.name
        viewModelScope.launch {
            cartRepository.clearCart()
            _cart.value = Cart()
            _checkoutState.value = CartCheckoutState()
        }
    }
}
