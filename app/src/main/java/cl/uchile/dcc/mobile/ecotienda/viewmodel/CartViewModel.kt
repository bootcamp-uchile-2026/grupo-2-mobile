package cl.uchile.dcc.mobile.ecotienda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.ecotienda.data.address.AddressLookupResult
import cl.uchile.dcc.mobile.ecotienda.data.address.ChileCommunes
import cl.uchile.dcc.mobile.ecotienda.data.address.NominatimAddressLookup
import cl.uchile.dcc.mobile.ecotienda.data.database.BuenOrigenDatabase
import cl.uchile.dcc.mobile.ecotienda.data.database.Cart
import cl.uchile.dcc.mobile.ecotienda.data.database.Product
import cl.uchile.dcc.mobile.ecotienda.data.database.toCartItem
import cl.uchile.dcc.mobile.ecotienda.data.repository.CartRepository
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.AddressCheckStatus
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


    private val addressLookup = NominatimAddressLookup()

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

    // Guerda el formulario de envio
    fun updateShippingForm(update: (ShippingFormState) -> ShippingFormState) {
        _checkoutState.update {
            val nextForm = update(it.shippingForm)
            if (nextForm == it.shippingForm) {
                it
            } else {
                it.copy(
                    shippingForm = nextForm,
                    addressCheck = AddressCheckStatus.IDLE,
                    addressCheckMessage = "",
                )
            }
        }
    }

    // Se ultiliza cuando se pasa del chequeo de la direccion a la pantalla de pago
    // Revisa la dirección vs las comunas y el servicio OpenStreetMap
    // Retiro en tienda pasa sin chequeo
    fun submitShipping() {
        val state = _checkoutState.value
        val form = state.shippingForm
        if (!form.isComplete()) return
        if (form.deliveryType == ShippingFormState.PICKUP) {
            nextStep()
            return
        }
        when (state.addressCheck) {
            AddressCheckStatus.CHECKING -> return
            AddressCheckStatus.FOUND,
            AddressCheckStatus.NOT_FOUND,
            AddressCheckStatus.UNAVAILABLE -> nextStep()
            AddressCheckStatus.IDLE -> {
                val commune = ChileCommunes.canonical(form.commune) ?: return
                startAddressLookup(form.copy(commune = commune))
            }
        }
    }

    // Marca el chequeo de la calle
    private fun startAddressLookup(form: ShippingFormState) {
        _checkoutState.update {
            it.copy(
                shippingForm = form,
                addressCheck = AddressCheckStatus.CHECKING,
                addressCheckMessage = "",
            )
        }
        viewModelScope.launch {
            val result = addressLookup.lookup(
                street = form.street,
                number = form.number,
                noNumber = form.isNoNumber,
                commune = form.commune,
            )
            _checkoutState.update { current ->
                if (current.shippingForm != form || current.addressCheck != AddressCheckStatus.CHECKING) {
                    current
                } else {
                    when (result) {
                        is AddressLookupResult.Found -> {
                            savedStateHandle["checkout_step"] = CheckoutStep.PAYMENT.name
                            current.copy(
                                addressCheck = AddressCheckStatus.FOUND,
                                addressCheckMessage = result.label,
                                currentStep = CheckoutStep.PAYMENT,
                            )
                        }
                        AddressLookupResult.NotFound -> current.copy(
                            addressCheck = AddressCheckStatus.NOT_FOUND,
                            addressCheckMessage = "No encontramos esa calle en ${form.commune}. Puedes continuar de todos modos.",
                        )
                        AddressLookupResult.Unavailable -> current.copy(
                            addressCheck = AddressCheckStatus.UNAVAILABLE,
                            addressCheckMessage = "No pudimos revisar la dirección. Puedes continuar de todos modos.",
                        )
                    }
                }
            }
        }
    }

    fun selectPaymentMethod(method: String) {
        _checkoutState.update { it.copy(paymentMethod = method) }
    }

    fun setQuantity(productId: String, quantity: Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(productId, quantity)
            _cart.update { Cart(items = cartRepository.getCartItems()) }
        }
    }

    fun updateCouponCode(code: String) {
        _checkoutState.update {
            it.copy(couponCode = code, discountAmount = 0, couponFeedback = "")
        }
    }

    fun applyCoupon(subtotal: Int) {
        val code = _checkoutState.value.couponCode.trim()
        when (code.uppercase()) {
            "VERDE10" -> {
                val discount = (subtotal * 10) / 100
                _checkoutState.update {
                    it.copy(
                        discountAmount = discount,
                        couponFeedback = "Cupón aplicado: 10% de descuento",
                    )
                }
            }
            "ECO5000" -> {
                val discount = minOf(5_000, subtotal)
                _checkoutState.update {
                    it.copy(
                        discountAmount = discount,
                        couponFeedback = "Cupón aplicado",
                    )
                }
            }
            "" -> _checkoutState.update {
                it.copy(discountAmount = 0, couponFeedback = "Ingresa un número de cupón")
            }
            else -> _checkoutState.update {
                it.copy(discountAmount = 0, couponFeedback = "Cupón no válido")
            }
        }
    }

    fun confirmPurchase() {
        _checkoutState.update { it.copy(orderPlaced = true) }
        viewModelScope.launch {
            cartRepository.clearCart()
            _cart.value = Cart()
        }
    }

    fun dismissConfirmedOrder() {
        savedStateHandle["checkout_step"] = CheckoutStep.CART.name
        _checkoutState.value = CartCheckoutState()
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
