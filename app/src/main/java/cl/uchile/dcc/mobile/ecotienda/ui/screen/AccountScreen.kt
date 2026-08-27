package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cl.uchile.dcc.mobile.ecotienda.viewmodel.AuthViewModel

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    onOpenOrders: () -> Unit,
    onGoHome: () -> Unit,
    onLogout: () -> Unit,
) {
    val authState by authViewModel.state.collectAsState()
    val displayName = when {
        authState.isGuest -> "Invitado"
        !authState.userEmail.isNullOrBlank() ->
            authState.userEmail!!.substringBefore("@")
        else -> "Usuario"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(88.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = displayName.take(1).uppercase(),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        Text(displayName, style = MaterialTheme.typography.titleLarge)
        Text(
            text = if (authState.isGuest) "Sesión de invitado"
            else authState.userEmail.orEmpty(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )


        Spacer(Modifier.height(24.dp))

        ListItem(
            headlineContent = { Text("Perfil") },
            leadingContent = { Icon(Icons.Filled.Person, null) },
            trailingContent = { Icon(Icons.Filled.ChevronRight, null) },
            modifier = Modifier
                .clickable { /* más adelante editar perfil */ }
        )
        HorizontalDivider()
        ListItem(
            headlineContent = { Text("Mis compras") },
            leadingContent = { Icon(Icons.Filled.ShoppingBag, null) },
            trailingContent = { Icon(Icons.Filled.ChevronRight, null) },
            modifier = Modifier.clickable(onClick = onOpenOrders)
        )
        HorizontalDivider()

        Spacer(Modifier.height(24.dp))
        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Logout, null)
            Spacer(Modifier.width(8.dp))
            Text("Cerrar sesión")
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onGoHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ir al inicio")
        }
    }
}