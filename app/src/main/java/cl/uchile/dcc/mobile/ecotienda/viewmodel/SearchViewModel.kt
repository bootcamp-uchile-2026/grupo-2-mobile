package cl.uchile.dcc.mobile.ecotienda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.ecotienda.data.database.BuenOrigenDatabase
import cl.uchile.dcc.mobile.ecotienda.data.database.Product
import cl.uchile.dcc.mobile.ecotienda.data.repository.ProductRepository
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.ProductFilter
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.SearchUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val app = application as? cl.uchile.dcc.mobile.ecotienda.data.repository.EcoTiendaApp
    private val productRepository = app?.container?.productRepository ?: ProductRepository(
        BuenOrigenDatabase.getInstance(application).productDAO
    )

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    // Limpiar la búsqueda y filtros
    fun clearSearch() {
        searchJob?.cancel()
        _uiState.update {
            it.copy(
                query = "",
                searchResults = emptyList(),
                isLoading = false,
                isSearchSubmitted = false,
                filter = ProductFilter()
            )
        }
    }

    // Búsqueda en vivo al cambiar el texto
    fun onQueryChange(newQuery: String) {
        _uiState.update {
            it.copy(
                query = newQuery,
                isSearchSubmitted = false
            )
        }

        searchJob?.cancel()

        if (newQuery.isBlank() && !_uiState.value.filter.isActive) {
            _uiState.update {
                it.copy(
                    searchResults = emptyList(),
                    isLoading = false
                )
            }
        } else {
            searchJob = viewModelScope.launch {
                delay(250) // Debounce
                executeSearchAndFilter(newQuery, _uiState.value.filter)
            }
        }
    }

    fun onSearchSubmitted(query: String) {
        onQueryChange(query)
    }

    fun updateFilter(filter: ProductFilter) {
        _uiState.update { it.copy(filter = filter) }
        viewModelScope.launch {
            executeSearchAndFilter(_uiState.value.query, filter)
        }
    }

    fun clearFilter() {
        _uiState.update { it.copy(filter = ProductFilter()) }
        viewModelScope.launch {
            executeSearchAndFilter(_uiState.value.query, ProductFilter())
        }
    }

    private suspend fun executeSearchAndFilter(query: String, filter: ProductFilter) {
        _uiState.update { it.copy(isLoading = true) }

        val rawList = if (query.isNotBlank()) {
            productRepository.searchProducts(query)
        } else {
            productRepository.getAllProduct()
        }

        val filtered = applyFilterRules(rawList, query, filter)

        _uiState.update {
            it.copy(
                searchResults = filtered,
                isLoading = false
            )
        }
    }

    private fun applyFilterRules(
        products: List<Product>,
        query: String,
        filter: ProductFilter
    ): List<Product> {
        var list = products

        // Filtrado por consulta de texto (si no fue filtrado en BD o para coincidencia local)
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.productName.lowercase().contains(q) ||
                it.description.lowercase().contains(q) ||
                it.category.lowercase().contains(q) ||
                it.productProducer.lowercase().contains(q)
            }
        }

        // Categoría
        if (filter.category != "Todas las categorías" && filter.category.isNotBlank()) {
            val cat = filter.category.trim().lowercase()
            list = list.filter {
                it.category.lowercase().contains(cat) ||
                (cat.contains("ropa") && (it.category.lowercase().contains("ropa") || it.category.lowercase().contains("moda"))) ||
                (cat.contains("artesan") && (it.category.lowercase().contains("artesan") || it.category.lowercase().contains("decoraci"))) ||
                (cat.contains("bienestar") && it.category.lowercase().contains("bienestar")) ||
                (cat.contains("huerto") && it.category.lowercase().contains("huerto")) ||
                (cat.contains("limpieza") && it.category.lowercase().contains("limpieza")) ||
                (cat.contains("mascota") && it.category.lowercase().contains("mascota")) ||
                (cat.contains("alimento") && it.category.lowercase().contains("alimento"))
            }
        }

        // Tipo de producto
        if (filter.productType.isNotBlank() && filter.productType != "Tipo de producto") {
            val type = filter.productType.trim().lowercase()
            val stem = when {
                type.endsWith("es") -> type.dropLast(2)
                type.endsWith("s") -> type.dropLast(1)
                else -> type
            }
            list = list.filter {
                it.productName.lowercase().contains(stem) ||
                it.description.lowercase().contains(stem) ||
                it.category.lowercase().contains(stem)
            }
        }

        // Atributos sustentables (checkboxes)
        if (filter.sustainableAttributes.isNotEmpty()) {
            list = list.filter { prod ->
                filter.sustainableAttributes.any { attr ->
                    val a = attr.lowercase()
                    prod.description.lowercase().contains(a) ||
                    prod.productName.lowercase().contains(a) ||
                    (a == "orgánico" && prod.greenPoints > 10) ||
                    (a == "comercio justo" && prod.producerID.isNotBlank()) ||
                    (a == "reciclado" && prod.co2Avoided > 1.0) ||
                    (a == "biodegradable" && prod.greenPoints > 5) ||
                    (a == "vegano" && prod.category.lowercase().contains("cuidado")) ||
                    (a == "cruelty-free" && prod.category.lowercase().contains("cuidado")) ||
                    (a == "envase reciclable" && prod.co2Avoided > 0.5) ||
                    (a == "compostable" && prod.greenPoints > 8)
                }
            }
        }

        // Género (ropa)
        if (filter.gender != null) {
            val g = filter.gender.lowercase()
            list = list.filter {
                it.description.lowercase().contains(g) ||
                it.productName.lowercase().contains(g) ||
                g == "neutro"
            }
        }

        // Color (ropa)
        if (filter.colors.isNotEmpty()) {
            list = list.filter { prod ->
                filter.colors.any { color ->
                    val c = color.lowercase()
                    prod.productName.lowercase().contains(c) ||
                    prod.description.lowercase().contains(c)
                }
            }
        }

        // Material (ropa)
        if (filter.material != null) {
            val m = filter.material.lowercase()
            list = list.filter {
                it.description.lowercase().contains(m) ||
                it.productName.lowercase().contains(m)
            }
        }

        // Ordenar por...
        list = when (filter.sortOrder) {
            "Menor precio" -> list.sortedBy { it.price }
            "Mayor precio" -> list.sortedByDescending { it.price }
            "Más recientes" -> list.reversed()
            "Mayor calificación" -> list.sortedByDescending { it.stock }
            "Más sustentable" -> list.sortedByDescending { it.co2Avoided + it.greenPoints }
            else -> list
        }

        return list
    }
}
