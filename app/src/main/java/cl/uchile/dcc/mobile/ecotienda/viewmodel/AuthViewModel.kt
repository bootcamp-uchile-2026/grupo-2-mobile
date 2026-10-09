package cl.uchile.dcc.mobile.ecotienda.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.ecotienda.data.repository.BuenOrigenDataRepository
import cl.uchile.dcc.mobile.ecotienda.data.repository.userPreferencesDataStore
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.AuthUIState
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.LoginFormState
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.LoginScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class AuthViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val app = application as? cl.uchile.dcc.mobile.ecotienda.data.repository.EcoTiendaApp
    private val configRepo = app?.container?.buenOrigenRepository ?: BuenOrigenDataRepository(
        application.userPreferencesDataStore,
        application.getSharedPreferences("ecotienda", Context.MODE_PRIVATE),
        application
    )

    private val savedEmail: String = savedStateHandle["user_email"] ?: ""
    private val savedIsLoggedIn: Boolean = savedStateHandle["is_logged_in"] ?: false
    private val savedIsGuest: Boolean = savedStateHandle["is_guest"] ?: false

    // Uso de AuthUiState para login
    private val _state = MutableStateFlow(
        AuthUIState(
            isLoggedIn = savedIsLoggedIn,
            isGuest = savedIsGuest,
            userEmail = if (savedIsLoggedIn) savedEmail else null,
            form = LoginFormState(email = savedEmail)
        )
    )
    val state: StateFlow<AuthUIState> = _state.asStateFlow()

    // formato de texto para email
    fun updateEmail(email: String) {
        savedStateHandle["user_email"] = email
        _state.update {
            it.copy(
                form = it.form.copy(
                    email = email,
                    emailError = when {
                        email.isBlank() -> "El email no puede estar vacío"
                        !email.contains("@") -> "Email inválido"
                        else -> null
                    }
                )
            )
        }
    }

    // formato de texto para contraseña
    fun updatePassword(password: String) {
        _state.update {
            it.copy(
                form = it.form.copy(
                    password = password,
                    passwordError = when {
                        password.isBlank() -> "La contraseña no puede estar vacía"
                        password.length < 4 -> "Mínimo 4 caracteres"
                        else -> null
                    }
                )
            )
        }
    }

    // Coroutine de repository
    val userName: StateFlow<String> = configRepo.name
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "Invitado"
        )

    fun requestLoginSheet() {
        _state.update { it.copy(showLoginSheet = true) }
    }

    fun dismissLoginSheet() {
        _state.update { it.copy(showLoginSheet = false) }
    }

    fun continueAsGuest() {
        savedStateHandle["is_guest"] = true
        _state.update { it.copy(isGuest = true, showLoginSheet = false) }
    }

    fun login() {
        val form = _state.value.form
        val emailError = when {
            form.email.isBlank() -> "El email no puede estar vacío"
            !form.email.contains("@") -> "Email inválido"
            else -> null
        }
        val passwordError = when {
            form.password.isBlank() -> "La contraseña no puede estar vacía"
            form.password.length < 4 -> "Mínimo 4 caracteres"
            else -> null
        }

        if (emailError != null || passwordError != null) {
            _state.update {
                it.copy(
                    form = it.form.copy(
                        emailError = emailError,
                        passwordError = passwordError
                    )
                )
            }
            return
        }

        viewModelScope.launch {
            configRepo.setName(form.email)
            _state.update { it.copy(login = LoginScreenState.Loading) }
            delay(1500.milliseconds) // simulación de tiempo de logeo

            savedStateHandle["user_email"] = form.email
            savedStateHandle["is_logged_in"] = true
            savedStateHandle["is_guest"] = false

            // login ficticio: acepta cualquier email/password válidos
            _state.update {
                it.copy(
                    login = LoginScreenState.Success(form.email),
                    isLoggedIn = true,
                    isGuest = false,
                    userEmail = form.email,
                    showLoginSheet = false
                )
            }
        }
    }

    fun logout() {
        savedStateHandle.remove<String>("user_email")
        savedStateHandle["is_logged_in"] = false
        savedStateHandle["is_guest"] = false
        _state.update {
            AuthUIState(showLoginSheet = false)
        }
        viewModelScope.launch {
            configRepo.setName("Invitado")
        }
    }

    // Eliminar snackbarhostate de homre una vez logeado o invitado
    fun markWelcomeShown() {
        _state.update { it.copy(welcomeMessageShown = true) }
    }
}