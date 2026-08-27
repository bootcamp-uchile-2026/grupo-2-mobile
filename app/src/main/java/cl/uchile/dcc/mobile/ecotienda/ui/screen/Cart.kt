package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.uchile.dcc.mobile.ecotienda.model.Cart
import cl.uchile.dcc.mobile.ecotienda.ui.component.FigureIconButton
import cl.uchile.dcc.mobile.ecotienda.viewmodel.AuthViewModel
import cl.uchile.dcc.mobile.ecotienda.viewmodel.CartViewModel
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft

@Composable
fun Cart (
    modifier: Modifier,
    cart: Cart,
    onBack: () -> Unit,
    authViewModel: AuthViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()

) {
    val ui by authViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        if (!ui.isLoggedIn) {
            authViewModel.requestLoginSheet()
        }
    }

    if (ui.showLoginSheet) {
        LoginBottomSheet(
            form = ui.form,
            loginState = ui.login,
            onEmailChange = authViewModel::updateEmail,
            onPasswordChange = authViewModel::updatePassword,
            onLogin = authViewModel::login,
            onDismiss = authViewModel::dismissLoginSheet
        )
    }

    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        FigureIconButton(
            label = "Volver",
            callBack = onBack,
            icon = FeatherIcons.ArrowLeft,
            enabled = true,
        )

        Text(
            text = "Mi Carrito",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        if (cart.items.isEmpty()) {
            Text(text = "Tu carrito está vacío")
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cart.items) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(text = item.productName, modifier = Modifier.weight(1f))
                        Text(text = "x${item.quantity}", modifier = Modifier.padding(horizontal = 8.dp))
                        Text(text = "$${item.subtotal}")
                    }
                    HorizontalDivider()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Total:",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "$${cart.total}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

