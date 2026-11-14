package cl.uchile.dcc.mobile.ecotienda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.ecotienda.data.database.BuenOrigenDatabase
import cl.uchile.dcc.mobile.ecotienda.data.repository.ProductRepository
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

    // Limpiar la búsqueda
    fun clearSearch() {
        searchJob?.cancel()
        _uiState.update {
            it.copy(
                query = "",
                searchResults = emptyList(),
                isLoading = false,
                isSearchSubmitted = false
            )
        }
    }

    // Función de busqueda
    fun onQueryChange(newQuery: String) {
        _uiState.update {
            it.copy(
                query = newQuery,
                isSearchSubmitted = false,
                searchResults = if (newQuery.isEmpty()) emptyList() else it.searchResults
            )
        }

        searchJob?.cancel()

        if (newQuery.isBlank()) {
            _uiState.update {
                it.copy(
                    searchResults = emptyList(),
                    isLoading = false
                )
            }
        } else {
            searchJob = viewModelScope.launch {
                delay(300) // Debounce de 300ms
                _uiState.update { it.copy(isLoading = true) }
                val results = productRepository.searchProducts(newQuery)

                // SOLO actualizar si el usuario sigue en la misma búsqueda actual
                if (_uiState.value.query == newQuery) {
                    _uiState.update {
                        it.copy(
                            searchResults = results,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onSearchSubmitted(query: String) {
        if (query.isBlank()) {
            _uiState.update {
                it.copy(
                    query = query,
                    searchResults = emptyList(),
                    isLoading = false,
                    isSearchSubmitted = true
                )
            }
            return
        }
        _uiState.update {
            it.copy(
                query = query,
                searchResults = emptyList(),
                isLoading = true,
                isSearchSubmitted = true
            )
        }
        viewModelScope.launch {
            val results = productRepository.searchProducts(query)
            _uiState.update {
                it.copy(
                    searchResults = results,
                    isLoading = false
                )
            }
        }
    }

    fun searchProducts(query: String) {
        if (query.isBlank()) {
            _uiState.update {
                it.copy(
                    searchResults = emptyList(),
                    isLoading = false
                )
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, searchResults = emptyList()) }
            val results = productRepository.searchProducts(query)
            _uiState.update {
                it.copy(
                    searchResults = results,
                    isLoading = false,
                )
            }
        }
    }
}
