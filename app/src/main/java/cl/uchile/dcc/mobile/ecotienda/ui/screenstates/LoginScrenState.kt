package cl.uchile.dcc.mobile.ecotienda.ui.screenstates

data class LoginFormState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null
)

sealed class LoginScreenState {
    data object Idle : LoginScreenState()
    data object Loading : LoginScreenState()
    data class Success(val userEmail: String) : LoginScreenState()
    data class Error(val message: String) : LoginScreenState()
}

data class AuthUIState(
    val form: LoginFormState = LoginFormState(),
    val login: LoginScreenState = LoginScreenState.Idle,
    val isLoggedIn: Boolean = false,
    val showLoginSheet: Boolean = false
)