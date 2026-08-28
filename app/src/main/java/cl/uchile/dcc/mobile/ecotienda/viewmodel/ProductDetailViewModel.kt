package cl.uchile.dcc.mobile.ecotienda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.ecotienda.model.toCartItem
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.ProductDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailViewModel: ViewModel() {

    // 2. Definir el estado inicial usando ProductDetailUiState
    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun selectProduct(product: cl.uchile.dcc.mobile.ecotienda.model.Product) {
        _uiState.update { it.copy(product = product) }
    }

    fun addToCart(onAdd: (cl.uchile.dcc.mobile.ecotienda.model.Product, Int) -> Unit) {
        val product = _uiState.value.product ?: return
        val qty = _uiState.value.selectedQuantity
        viewModelScope.launch {
            _uiState.update { it.copy(isAddingToCart = true) }
            // Simulamos guardado
            onAdd(product, qty)
            _uiState.update { it.copy(isAddingToCart = false) }
        }
    }

}