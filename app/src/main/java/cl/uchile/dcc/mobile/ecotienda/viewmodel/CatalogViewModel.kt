package cl.uchile.dcc.mobile.ecotienda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.ecotienda.model.DefaultData
import cl.uchile.dcc.mobile.ecotienda.model.Product
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.CatalogCategories
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.CatalogScreenState
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.CatalogUIState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class CatalogViewModel : ViewModel() {

    private val allProducts: List<Product> = DefaultData.Product

    private val _state = MutableStateFlow(CatalogUIState())
    val state: StateFlow<CatalogUIState> = _state.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _state.update { it.copy(catalog = CatalogScreenState.Loading) }
            delay(800.milliseconds)
            applyFilters()
        }
    }

    fun updateSearch(query: String) {
        _state.update { it.copy(form = it.form.copy(search = query)) }
        applyFilters()
    }

    fun selectCategory(category: String) {
        _state.update { it.copy(form = it.form.copy(selectedCategory = category)) }
        applyFilters()
    }

    fun getProductById(id: String): Product? {
        return allProducts.find { it.id == id }
    }

    private fun applyFilters() {
        val form = _state.value.form

        val filtered = allProducts
            .filter { product ->
                form.selectedCategory == CatalogCategories.TODOS ||
                        product.category == form.selectedCategory
            }
            .filter { product ->
                product.productName.contains(form.search, ignoreCase = true) ||
                        product.productProducer.contains(form.search, ignoreCase = true)
            }

        _state.update {
            it.copy(
                catalog = if (filtered.isEmpty()) {
                    CatalogScreenState.Empty
                } else {
                    CatalogScreenState.Success(filtered)
                }
            )
        }
    }
}