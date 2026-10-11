package cl.uchile.dcc.mobile.ecotienda.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.ecotienda.data.User
import cl.uchile.dcc.mobile.ecotienda.data.database.UserRepository
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
import kotlinx.coroutines.flow.combine
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

    private val userRepository = UserRepository(application)

    private val savedEmail: String = savedStateHandle["user_email"] ?: ""
    private val savedIsLoggedIn: Boolean = savedStateHandle["is_logged_in"] ?: false
    private val savedIsGuest: Boolean = savedStateHandle["is_guest"] ?: false


    init {
        // Al iniciar el ViewModel, leemos la sesión guardada en DataStore
        viewModelScope.launch {
            combine(
                configRepo.isLoggedIn,
                configRepo.isGuest,
                configRepo.userEmail
            ) { isLoggedIn, isGuest, userEmail ->
                Triple(isLoggedIn, isGuest, userEmail)
            }.collect { (isLoggedIn, isGuest, userEmail) ->
                _state.update { current ->
                    current.copy(
                        isLoggedIn = isLoggedIn,
                        isGuest = isGuest,
                        userEmail = userEmail,
                        form = if (!userEmail.isNullOrBlank()) current.form.copy(email = userEmail) else current.form
                    )
                }
            }
        }
    }

<<<<<<< HEAD
    private val _state = MutableStateFlow(AuthUIState())
=======
    // Uso de AuthUiState para login
    private val _state = MutableStateFlow(
        AuthUIState(
            isLoggedIn = savedIsLoggedIn,
            isGuest = savedIsGuest,
            userEmail = if (savedIsLoggedIn) savedEmail else null,
            form = LoginFormState(email = savedEmail)
        )
    )
>>>>>>> feature/data-sqlite
    val state: StateFlow<AuthUIState> = _state.asStateFlow()

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

    // Sesión de invitado
    fun continueAsGuest() {
        viewModelScope.launch {
            configRepo.saveSession(
                isLoggedIn = false,
                isGuest = true,
                email = null,
                name = "Invitado"
            )
            _state.update { it.copy(isGuest = true, isLoggedIn = false, userEmail = null, showLoginSheet = false) }
        }
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
<<<<<<< HEAD
            delay(1500.milliseconds) // simulación, igual que tu addPerson()
=======
            delay(1000.milliseconds) // simulación de tiempo de logeo
>>>>>>> feature/data-sqlite

            var user = userRepository.loginUser(form.email, form.password)
            if (user == null) {
                // Auto-registrar si no existe (para facilitar login sin pantalla de registro)
                val newUser = User(
                    email = form.email,
                    password = form.password,
                    name = form.email.substringBefore("@")
                )
                if (userRepository.registerUser(newUser)) {
                    user = userRepository.loginUser(form.email, form.password)
                }
            }

            if (user != null) {
                configRepo.saveSession(
                    isLoggedIn = true,
                    isGuest = false,
                    email = user.email,
                    name = user.name
                )
                savedStateHandle["user_email"] = user.email
                savedStateHandle["is_logged_in"] = true
                savedStateHandle["is_guest"] = false

                _state.update {
                    it.copy(
                        login = LoginScreenState.Success(user.email),
                        isLoggedIn = true,
                        isGuest = false,
                        userEmail = user.email,
                        showLoginSheet = false
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        login = LoginScreenState.Error("Credenciales inválidas"),
                        form = it.form.copy(passwordError = "Error al autenticar o registrar usuario")
                    )
                }
            }
        }
    }

    fun logout() {
        savedStateHandle.remove<String>("user_email")
        savedStateHandle["is_logged_in"] = false
        savedStateHandle["is_guest"] = false
        _state.update {
            AuthUIState(showLoginSheet = false)
<<<<<<< HEAD
=======
        }
        viewModelScope.launch {
            configRepo.clearSession()
>>>>>>> feature/data-sqlite
        }
    }

    // Eliminar snackbarhostate de nombre una vez logeado o invitado
    fun markWelcomeShown() {
        _state.update { it.copy(welcomeMessageShown = true) }
    }
}