package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.LoginFormState
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.LoginScreenState

// ui/component/LoginBottomSheet.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginBottomSheet(
    form: LoginFormState,
    loginState: LoginScreenState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onDismiss: () -> Unit,
    onContinueAsGuest: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Iniciar Sesión", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = form.email,
                onValueChange = onEmailChange,
                label = { Text("Email") },
                isError = form.emailError != null,
                supportingText = { form.emailError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = form.password,
                onValueChange = onPasswordChange,
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                isError = form.passwordError != null,
                supportingText = { form.passwordError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onLogin,
                enabled = loginState !is LoginScreenState.Loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (loginState is LoginScreenState.Loading) {
                    CircularProgressIndicator()
                } else {
                    Text("Ingresar")
                }
            }
            TextButton(onClick = onContinueAsGuest) {
                Text("Continuar como invitado")
            }
        }
    }
}
