package cl.uchile.dcc.mobile.ecotienda.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.ecotienda.data.DefaultData
import cl.uchile.dcc.mobile.ecotienda.data.Product
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.ProductDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val savedProductId: String? = savedStateHandle["product_id"] ?: savedStateHandle["productId"]
    private val restoredProduct: Product? = savedProductId?.let { id ->
        DefaultData.Product.find { it.id == id }
    }

    private val _uiState = MutableStateFlow(ProductDetailUiState(product = restoredProduct))
    val uiState = _uiState.asStateFlow()

    fun selectProduct(product: Product) {
        savedStateHandle["product_id"] = product.id
        _uiState.update { it.copy(product = product) }
    }

    fun addToCart(onAdd: (Product, Int) -> Unit) {
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