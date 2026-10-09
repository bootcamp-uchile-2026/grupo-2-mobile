package cl.uchile.dcc.mobile.ecotienda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.ecotienda.data.database.ProductRepository
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.SearchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val productRepository = ProductRepository(application)

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    fun searchProducts(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Llamamos al método SQLite que vimos antes
            val results = productRepository.searchProducts(query)

            // Guardamos los resultados en el estado
            _uiState.update {
                it.copy(
                    query = query,
                    searchResults = results,
                    isLoading = false
                )
            }
        }
    }
}