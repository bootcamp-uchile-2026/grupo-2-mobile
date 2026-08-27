package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.uchile.dcc.mobile.ecotienda.viewmodel.AuthViewModel

@Composable
fun AccountPanelContent(
    authViewModel: AuthViewModel,
    snackbarHostState: SnackbarHostState,
    onGoHome: () -> Unit,
    onOpenOrders: () -> Unit,
) {
    val authState by authViewModel.state.collectAsState()
    val hasSession = authState.isLoggedIn || authState.isGuest

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (hasSession) {
            AccountScreen(
                authViewModel = authViewModel,
                onOpenOrders = onOpenOrders,
                onGoHome = onGoHome,
                onLogout = { authViewModel.logout() }
            )
        } else {
            Login(
                snackbarHostState = snackbarHostState,
                authViewModel = authViewModel,
                onBack = onGoHome,
                onSuccess = onGoHome,
                modifier = Modifier
            )
        }
    }
}
