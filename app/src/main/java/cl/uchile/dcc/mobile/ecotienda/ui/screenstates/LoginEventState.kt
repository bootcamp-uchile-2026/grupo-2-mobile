package cl.uchile.dcc.mobile.ecotienda.ui.screenstates

sealed class LoginEventState {
        object Loading: LoginEventState()
        object Empty: LoginEventState()
        data class Error(val message: String): LoginEventState()
}