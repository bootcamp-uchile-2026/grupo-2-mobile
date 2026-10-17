package cl.uchile.dcc.mobile.ecotienda.ui.screenstates

// ScreenState del carrito
enum class CheckoutStep {
    CART, SHIPPING, PAYMENT
}

data class ShippingFormState(
    val commune: String = "",
    val street: String = "",
    val number: String = "",
    val isNoNumber: Boolean = false,
    val apartment: String = "",
    val zipCode: String = "",
    val deliveryInstructions: String = ""
)

data class CartCheckoutState(
    val currentStep: CheckoutStep = CheckoutStep.CART,
    val shippingForm: ShippingFormState = ShippingFormState(),
    val paymentMethod: String = "",
    val couponCode: String = "",
    val discountAmount: Int = 0
)
