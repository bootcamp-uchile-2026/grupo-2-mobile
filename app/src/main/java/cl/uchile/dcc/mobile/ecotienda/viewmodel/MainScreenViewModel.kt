package cl.uchile.dcc.mobile.ecotienda.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cl.uchile.dcc.mobile.ecotienda.ui.screen.ProductEvenState
import cl.uchile.dcc.mobile.ecotienda.ui.screen.ProductScreenState
import cl.uchile.dcc.mobile.ecotienda.ui.screen.Routes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import okhttp3.Route


class MainScreenViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProductScreenState())
    val uiState: StateFlow<ProductScreenState> = _uiState

    private val _currentRoute = MutableStateFlow(Routes.HOME)
    val currentRoute: StateFlow<Routes> = _currentRoute

    fun navigateTo(route: String){
        _currentRoute.update { Routes.valueOf(route) }
    }
}