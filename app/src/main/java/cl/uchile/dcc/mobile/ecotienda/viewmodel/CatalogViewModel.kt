package cl.uchile.dcc.mobile.ecotienda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.ecotienda.data.database.BuenOrigenDatabase
import cl.uchile.dcc.mobile.ecotienda.data.database.Product
import cl.uchile.dcc.mobile.ecotienda.data.repository.ProductRepository
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.CatalogCategories
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.CatalogFormState
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.CatalogScreenState
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.CatalogUIState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class CatalogViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val app = application as? cl.uchile.dcc.mobile.ecotienda.data.repository.EcoTiendaApp
    private val productRepository = app?.container?.productRepository ?: ProductRepository(
        BuenOrigenDatabase.getInstance(application).productDAO
    )

    private val initialSearch: String = savedStateHandle["search_query"] ?: ""
    private val initialCategory: String = savedStateHandle["selected_category"] ?: CatalogCategories.TODOS

    private val _state = MutableStateFlow(
        CatalogUIState(
            categories = listOf(CatalogCategories.TODOS),
            form = CatalogFormState(
                search = initialSearch,
                selectedCategory = initialCategory
            )
        )
    )
    val state: StateFlow<CatalogUIState> = _state.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _state.update { it.copy(catalog = CatalogScreenState.Loading) }
            delay(400.milliseconds)
            val products = productRepository.getAllProduct()
            val categories = listOf(CatalogCategories.TODOS) + products.map { it.category }.distinct()
            _state.update { it.copy(categories = categories) }
            applyFilters(products)
        }
    }

    fun updateSearch(query: String) {
        savedStateHandle["search_query"] = query
        _state.update { it.copy(form = it.form.copy(search = query)) }
        viewModelScope.launch {
            applyFilters(productRepository.getAllProduct())
        }
    }

    fun selectCategory(category: String) {
        savedStateHandle["selected_category"] = category
        _state.update { it.copy(form = it.form.copy(selectedCategory = category)) }
        viewModelScope.launch {
            applyFilters(productRepository.getAllProduct())
        }
    }

    private fun applyFilters(products: List<Product>) {
        val form = _state.value.form

        val filtered = products
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
