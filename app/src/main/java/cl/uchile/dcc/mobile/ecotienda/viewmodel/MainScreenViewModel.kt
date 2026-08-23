package cl.uchile.dcc.mobile.ecotienda.viewmodel

import androidx.lifecycle.ViewModel
import cl.uchile.dcc.mobile.ecotienda.model.Producer
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.ProductScreenState
import cl.uchile.dcc.mobile.ecotienda.ui.screen.ScreenRoutes
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.ProductEvenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class MainScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProductScreenState())
    val uiState: StateFlow<ProductScreenState> = _uiState

}