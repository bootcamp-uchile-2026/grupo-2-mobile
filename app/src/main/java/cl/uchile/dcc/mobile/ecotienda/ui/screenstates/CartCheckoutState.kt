package cl.uchile.dcc.mobile.ecotienda.ui.screenstates

import cl.uchile.dcc.mobile.ecotienda.data.address.ChileCommunes

// ScreenState del carrito
// Revisión del estado del Chekout
enum class CheckoutStep {
    CART, SHIPPING, PAYMENT
}

// Revisión de estado del chequeo de la dirección
enum class AddressCheckStatus {
    IDLE, CHECKING, FOUND, NOT_FOUND, UNAVAILABLE
}

// Formulario de envio
data class ShippingFormState(
    val commune: String = "",
    val street: String = "",
    val number: String = "",
    val isNoNumber: Boolean = false,
    val apartment: String = "",
    val zipCode: String = "",
    val deliveryInstructions: String = "",
    val deliveryType: String = DELIVERY,
) {
    fun isComplete(): Boolean {
        if (deliveryType == PICKUP) return true
        val hasNumber = isNoNumber || number.isNotBlank()
        return ChileCommunes.canonical(commune) != null && street.isNotBlank() && hasNumber
    }

    fun summaryLine(): String {
        if (deliveryType == PICKUP) return "Retiro en tienda"
        val numberPart = when {
            isNoNumber -> "s/n"
            else -> number.trim()
        }
        return listOf(street, numberPart, apartment, commune)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .joinToString(", ")
    }

    companion object {
        const val DELIVERY = "delivery"
        const val PICKUP = "pickup"
    }
}

data class CartCheckoutState(
    val currentStep: CheckoutStep = CheckoutStep.CART,
    val shippingForm: ShippingFormState = ShippingFormState(),
    val paymentMethod: String = "",
    val couponCode: String = "",
    val discountAmount: Int = 0,
    val couponFeedback: String = "",
    val orderPlaced: Boolean = false,
    val addressCheck: AddressCheckStatus = AddressCheckStatus.IDLE,
    val addressCheckMessage: String = "",
)

const val HOME_DELIVERY_COST = 3_990

fun shippingCostFor(subtotal: Int, deliveryType: String): Int {
    if (subtotal <= 0) return 0
    if (deliveryType == ShippingFormState.PICKUP) return 0
    return HOME_DELIVERY_COST
}
