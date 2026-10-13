package cl.uchile.dcc.mobile.ecotienda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.ecotienda.data.database.BuenOrigenDatabase
import cl.uchile.dcc.mobile.ecotienda.data.repository.ProductRepository
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.SearchUiState
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

    fun onQueryChange(newQuery: String) {
        _uiState.update { it.copy(query = newQuery, isSearchSubmitted = false) }
        searchProducts(newQuery)
    }

    fun onSearchSubmitted(query: String) {
        _uiState.update {
            it.copy(
                query = query,
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
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val results = productRepository.searchProducts(query)
            _uiState.update {
                it.copy(
                    query = query,
                    searchResults = results,
                    isLoading = false,
                )
            }
        }
    }
}
