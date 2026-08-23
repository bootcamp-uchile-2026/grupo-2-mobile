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



    // Seleccion de producer en viewModel
    private val _selectedProducer = MutableStateFlow<Producer?>(null)

    val selectedProducer: StateFlow<Producer?> = _selectedProducer

    // Selección de productor
    fun selectProducer(producer: Producer) {
        _selectedProducer.value = producer
    }

//    private val _currentRoute = MutableStateFlow(ScreenRoutes.HOME)
//    val currentRoute: StateFlow<ScreenRoutes> = _currentRoute
//    // Creación de navegación Navbar
//    private val _navStack = MutableStateFlow(listOf(ScreenRoutes.HOME))
//    val navStack: StateFlow<List<ScreenRoutes>> = _navStack.asStateFlow()
//
//    // Cambio de pantallas con NavigateTo
//    fun navigateTo(route: String){
//        val screenRoute: ScreenRoutes = ScreenRoutes.values().find { it.route == route } ?: ScreenRoutes.HOME
//
//        _navStack.update { it + screenRoute }
//        _currentRoute.update { screenRoute }
//    }
//
//    // Creación de backstack o pila de pantallas
//    fun goBack() {
//        // Si solo hay 1 pantalla no se puede retornar
//        if (_navStack.value.size <= 1) return
//        _navStack.update { it.dropLast(n = 1)}
//        _currentRoute.update { _navStack.value.last() }
//    }

}