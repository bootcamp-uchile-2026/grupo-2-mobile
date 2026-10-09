package cl.uchile.dcc.mobile.ecotienda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.ecotienda.data.repository.BuenOrigenDataRepository
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.ProductScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class MainScreenViewModel(
    private val configRepo: BuenOrigenDataRepository
) : ViewModel() {
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