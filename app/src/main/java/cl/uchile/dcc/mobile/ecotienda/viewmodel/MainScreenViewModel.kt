package cl.uchile.dcc.mobile.ecotienda.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.ecotienda.data.database.Producer
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

    private val _selectedProducer = MutableStateFlow<Producer?>(null)
    val selectedProducer: StateFlow<Producer?> = _selectedProducer

    fun selectProducer(producer: Producer) {
        _selectedProducer.value = producer
    }

    val appTheme: StateFlow<String> = configRepo.theme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = "Auto"
        )

    fun changeTheme() {
        var theme = appTheme.value
        theme = when (theme) {
            "Auto" -> "Claro"
            "Light", "Claro" -> "Oscuro"
            "Dark", "Oscuro" -> "Claro"
            else -> "Claro"
        }
        viewModelScope.launch {
            configRepo.setTheme(theme)
        }
    }
}
