package cl.uchile.dcc.mobile.ecotienda.ui.screenstates
// EventSatate de login
sealed class LoginEventState {
        object Loading: LoginEventState()
        object Empty: LoginEventState()
        data class Error(val message: String): LoginEventState()
}