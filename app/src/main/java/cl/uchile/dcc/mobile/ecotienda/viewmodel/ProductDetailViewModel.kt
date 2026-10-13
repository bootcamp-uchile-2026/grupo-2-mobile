package cl.uchile.dcc.mobile.ecotienda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.ecotienda.data.DefaultData
import cl.uchile.dcc.mobile.ecotienda.data.database.BuenOrigenDatabase
import cl.uchile.dcc.mobile.ecotienda.data.database.Product
import cl.uchile.dcc.mobile.ecotienda.data.repository.ProductRepository
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.ProductDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val app = application as? cl.uchile.dcc.mobile.ecotienda.data.repository.EcoTiendaApp
    private val productRepository = app?.container?.productRepository ?: ProductRepository(
        BuenOrigenDatabase.getInstance(application).productDAO
    )

    private val savedProductId: String? = savedStateHandle["product_id"] ?: savedStateHandle["productId"]

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        savedProductId?.let { id ->
            viewModelScope.launch {
                val product = productRepository.getProductById(id) ?: DefaultData.Product.find { it.id == id }
                _uiState.update { it.copy(product = product) }
            }
        }
    }

    fun selectProduct(product: Product) {
        savedStateHandle["product_id"] = product.id
        _uiState.update { it.copy(product = product) }
    }

    fun addToCart(onAdd: (Product, Int) -> Unit) {
        val product = _uiState.value.product ?: return
        val qty = _uiState.value.selectedQuantity
        viewModelScope.launch {
            _uiState.update { it.copy(isAddingToCart = true) }
            onAdd(product, qty)
            _uiState.update { it.copy(isAddingToCart = false) }
        }
    }
}
