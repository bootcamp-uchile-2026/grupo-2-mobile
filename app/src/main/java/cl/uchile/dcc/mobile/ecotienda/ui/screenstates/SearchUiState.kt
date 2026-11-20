package cl.uchile.dcc.mobile.ecotienda.ui.screenstates

import cl.uchile.dcc.mobile.ecotienda.data.database.Product

data class ProductFilter(
    val sortOrder: String = "Ordenar por...",
    val category: String = "Todas las categorías",
    val productType: String = "",
    val sustainableAttributes: Set<String> = emptySet(),
    val gender: String? = null,
    val size: String? = null,
    val colors: Set<String> = emptySet(),
    val material: String? = null,
) {
    val isActive: Boolean
        get() = (sortOrder != "Ordenar por..." && sortOrder.isNotBlank()) ||
                (category != "Todas las categorías" && category.isNotBlank()) ||
                productType.isNotBlank() ||
                sustainableAttributes.isNotEmpty() ||
                gender != null ||
                size != null ||
                colors.isNotEmpty() ||
                material != null
}

data class SearchUiState(
    val query: String = "",
    val searchResults: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val isSearchSubmitted: Boolean = false,
    val filter: ProductFilter = ProductFilter(),
)
