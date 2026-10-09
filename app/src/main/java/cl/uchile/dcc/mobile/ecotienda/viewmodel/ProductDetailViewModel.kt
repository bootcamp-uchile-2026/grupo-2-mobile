package cl.uchile.dcc.mobile.ecotienda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.ecotienda.data.DefaultData
import cl.uchile.dcc.mobile.ecotienda.data.Product
import cl.uchile.dcc.mobile.ecotienda.data.database.ProductRepository
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.ProductDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
class ProductDetailViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    // Instanciamos el repositorio usando el contexto de la aplicación
    private val productRepository = ProductRepository(application)

    private val savedProductId: String? = savedStateHandle["product_id"] ?: savedStateHandle["productId"]

    // Estrategia híbrida (BD Local + Mocks mientras llega la API):
    // 1. Intentamos buscar en SQLite.
    // 2. Si no está en SQLite, buscamos en tus productos mock (DefaultData).
    private val restoredProduct: Product? = savedProductId?.let { id ->
        productRepository.getProductById(id) ?: DefaultData.Product.find { it.id == id }
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
            _uiState.update {
                it.copy(isAddingToCart = true) }
            onAdd(product, qty)
            _uiState.update {
                it.copy(isAddingToCart = false) }
        }
    }

}