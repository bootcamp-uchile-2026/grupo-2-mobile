package cl.uchile.dcc.mobile.ecotienda.ui.screenstates

import cl.uchile.dcc.mobile.ecotienda.model.Product

object CatalogCategories {
    const val TODOS = "Todos"
    val all = listOf(
        TODOS,
        "Cuidado Personal",
        "Moda Natura",
        "Cuidado del Hogar"
    )
}

data class CatalogFormState(
    val search: String = "",
    val selectedCategory: String = CatalogCategories.TODOS
)

sealed class CatalogScreenState {
    data object Loading : CatalogScreenState()
    data object Empty : CatalogScreenState()
    data class Success(val products: List<Product>) : CatalogScreenState()
}

data class CatalogUIState(
    val form: CatalogFormState = CatalogFormState(),
    val catalog: CatalogScreenState = CatalogScreenState.Loading,
    val categories: List<String> = CatalogCategories.all
)