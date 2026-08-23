package cl.uchile.dcc.mobile.ecotienda.ui.screenstates

import cl.uchile.dcc.mobile.ecotienda.model.Producer
import cl.uchile.dcc.mobile.ecotienda.model.Product

data class ProductDetailUiState(
    val product: Product? = null,
    val producer: Producer? = null,
    val selectedQuantity: Int = 1,
    val isLoading: Boolean = false,
    val isAddingToCart: Boolean = false,
    val error: String? = null
)