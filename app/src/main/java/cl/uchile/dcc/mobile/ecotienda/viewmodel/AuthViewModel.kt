package cl.uchile.dcc.mobile.ecotienda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.AuthUIState
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.LoginScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import kotlin.time.Duration.Companion.milliseconds

class AuthViewModel : ViewModel() {

    private val _state = MutableStateFlow(AuthUIState())
    val state: StateFlow<AuthUIState> = _state.asStateFlow()

    fun updateEmail(email: String) {
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

    fun requestLoginSheet() {
        _state.update { it.copy(showLoginSheet = true) }
    }

    fun dismissLoginSheet() {
        _state.update { it.copy(showLoginSheet = false) }
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
            _state.update { it.copy(login = LoginScreenState.Loading) }
            delay(1500.milliseconds) // simulación, igual que tu addPerson()

            // login ficticio: acepta cualquier email/password válidos
            _state.update {
                it.copy(
                    login = LoginScreenState.Success(form.email),
                    isLoggedIn = true,
                    showLoginSheet = false
                )
            }
        }
    }

    fun logout() {
        _state.update {
            AuthUIState() // reset
        }
    }
}