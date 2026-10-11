package cl.uchile.dcc.mobile.ecotienda.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.ecotienda.data.repository.BuenOrigenDataRepository
import cl.uchile.dcc.mobile.ecotienda.data.repository.userPreferencesDataStore
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.ProductScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


<<<<<<< HEAD
class MainScreenViewModel : ViewModel() {
=======
class MainScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as? cl.uchile.dcc.mobile.ecotienda.data.repository.EcoTiendaApp
    private val configRepo = app?.container?.buenOrigenRepository ?: BuenOrigenDataRepository(
        application.userPreferencesDataStore,
        application.getSharedPreferences("ecotienda", Context.MODE_PRIVATE),
        application
    )
>>>>>>> feature/data-sqlite

    private val _uiState = MutableStateFlow(ProductScreenState())
    val uiState: StateFlow<ProductScreenState> = _uiState

<<<<<<< HEAD


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

=======
    // SharedPreferences de cambio de tema

    private val _appTheme = MutableStateFlow(configRepo.theme)
    val appTheme: StateFlow<String> = configRepo.theme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = "Auto"
        )

    suspend fun changeTheme() {
        var theme =appTheme.value
        when(theme) {
            "Auto" -> theme = "Claro"
            "Light" -> theme = "Oscuro"
            "Dark" -> theme = "Claro"
        }
        viewModelScope.launch {
            configRepo.setTheme(theme) }
    }

>>>>>>> feature/data-sqlite
}