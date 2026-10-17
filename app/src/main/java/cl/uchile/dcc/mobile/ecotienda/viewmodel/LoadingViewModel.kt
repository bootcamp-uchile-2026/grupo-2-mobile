package cl.uchile.dcc.mobile.ecotienda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.LoadingState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class LoadingViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
): AndroidViewModel(application)  {

    private val _state = MutableStateFlow(LoadingState())
    val state: StateFlow<LoadingState> = _state

    fun longProcess() {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch {
            delay(2000.milliseconds) // Simula proceso login
            _state.update { it.copy(loading = true) }
        }
    }
}