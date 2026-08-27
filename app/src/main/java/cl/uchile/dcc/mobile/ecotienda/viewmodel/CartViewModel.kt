package cl.uchile.dcc.mobile.ecotienda.viewmodel

import androidx.lifecycle.ViewModel
import cl.uchile.dcc.mobile.ecotienda.model.Cart
import cl.uchile.dcc.mobile.ecotienda.model.Product
import cl.uchile.dcc.mobile.ecotienda.model.toCartItem
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.CartCheckoutState
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.CheckoutStep
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.ShippingFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartViewModel : ViewModel() {
    private val _cart = MutableStateFlow(Cart())
    val cart = _cart.asStateFlow()

    private val _checkoutState = MutableStateFlow(CartCheckoutState())
    val checkoutState = _checkoutState.asStateFlow()

    fun addToCart(product: Product, quantity: Int) {
        _cart.update { currentCart ->
            val existingItem = currentCart.items.find { it.productId == product.id }
            val updatedItems = if (existingItem != null) {
                currentCart.items.map {
                    if (it.productId == product.id) it.copy(quantity = it.quantity + quantity)
                    else it
                }
            } else {
                currentCart.items + product.toCartItem(quantity)
            }
            currentCart.copy(items = updatedItems)
        }
    }

    fun incrementQuantity(productId: String) {
        _cart.update { currentCart ->
            val updatedItems = currentCart.items.map {
                if (it.productId == productId) it.copy(quantity = it.quantity + 1)
                else it
            }
            currentCart.copy(items = updatedItems)
        }
    }

    fun decrementQuantity(productId: String) {
        _cart.update { currentCart ->
            val updatedItems = currentCart.items.mapNotNull {
                if (it.productId == productId) {
                    if (it.quantity > 1) it.copy(quantity = it.quantity - 1)
                    else null
                } else it
            }
            currentCart.copy(items = updatedItems)
        }
    }

    fun removeFromCart(productId: String) {
        _cart.update { currentCart ->
            currentCart.copy(items = currentCart.items.filter { it.productId != productId })
        }
    }

    fun nextStep() {
        _checkoutState.update { 
            val nextStep = when(it.currentStep) {
                CheckoutStep.CART -> CheckoutStep.SHIPPING
                CheckoutStep.SHIPPING -> CheckoutStep.PAYMENT
                CheckoutStep.PAYMENT -> CheckoutStep.PAYMENT
            }
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
        _cart.value = Cart()
        _checkoutState.value = CartCheckoutState()
    }
}
