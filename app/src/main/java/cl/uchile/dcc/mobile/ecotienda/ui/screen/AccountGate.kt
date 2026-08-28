package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.material3.SnackbarHostState
import cl.uchile.dcc.mobile.ecotienda.viewmodel.AuthViewModel

// AccountGate esta la pantalla que realiza el cambio entre login/account dependiendo de estado de autenticacion
// Navegación: Home -> Incluye logi o Home -> Perfil

@Composable
fun AccountGate(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    snackbarHostState: SnackbarHostState,
    onEnterApp: () -> Unit,
    onOpenOrders: () -> Unit,
    onLogout: () -> Unit,
) {
    val authState by authViewModel.state.collectAsState()
    val hasSession = authState.isLoggedIn || authState.isGuest

    // Selección de pantallas
    if (hasSession) {
        AccountScreen(
            modifier = modifier,
            authViewModel = authViewModel,
            onOpenOrders = onOpenOrders,
            onGoHome = onEnterApp,
            onLogout = {
                authViewModel.logout()
                onLogout()
            }
        )
    } else {
        Login(
            modifier = modifier,
            snackbarHostState = snackbarHostState,
            authViewModel = authViewModel,
            onBack = {},
            onSuccess = onEnterApp
        )
    }
}