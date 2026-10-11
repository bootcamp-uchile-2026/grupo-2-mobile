package cl.uchile.dcc.mobile.ecotienda.ui.screenstates

import cl.uchile.dcc.mobile.ecotienda.data.Product

data class SearchUiState(
    val query: String = "",
    val searchResults: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    // Nos indica si se presiona el boton de búsqueda en la pantalla de busqueda
    val isSearchSubmitted: Boolean = false
)