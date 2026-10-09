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


class MainScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as? cl.uchile.dcc.mobile.ecotienda.data.repository.EcoTiendaApp
    private val configRepo = app?.container?.buenOrigenRepository ?: BuenOrigenDataRepository(
        application.userPreferencesDataStore,
        application.getSharedPreferences("ecotienda", Context.MODE_PRIVATE),
        application
    )

    private val _uiState = MutableStateFlow(ProductScreenState())
    val uiState: StateFlow<ProductScreenState> = _uiState

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

}