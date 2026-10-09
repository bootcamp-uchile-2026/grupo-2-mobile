package cl.uchile.dcc.mobile.ecotienda.ui.screenstates

import cl.uchile.dcc.mobile.ecotienda.data.Product

data class SearchUiState(
    val query: String = "",
    val searchResults: List<Product> = emptyList(), // <--- ¡Aquí está!
    val isLoading: Boolean = false
)