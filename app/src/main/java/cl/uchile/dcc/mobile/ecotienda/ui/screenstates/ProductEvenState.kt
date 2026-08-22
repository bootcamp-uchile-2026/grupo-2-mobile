package cl.uchile.dcc.mobile.ecotienda.ui.screenstates

sealed class ProductEvenState {
    object Loading: ProductEvenState()
    object Empty: ProductEvenState()
    data class Error(val message: String): ProductEvenState()

}