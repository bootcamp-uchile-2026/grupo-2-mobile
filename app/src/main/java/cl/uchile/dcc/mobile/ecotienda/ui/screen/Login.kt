package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.uchile.dcc.mobile.ecotienda.ui.component.FigureIconButton
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.LoginScreenState
import cl.uchile.dcc.mobile.ecotienda.ui.theme.CustomBrandParagraph
import cl.uchile.dcc.mobile.ecotienda.ui.theme.CustomBrandTitleStyle
import cl.uchile.dcc.mobile.ecotienda.viewmodel.AuthViewModel
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

// Login pagina de ingreso a aplicación
@Composable
fun Login(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    val ui by authViewModel.state.collectAsState()

    // Vuelve atrás automáticamente cuando el login sea correcto
    LaunchedEffect(ui.isLoggedIn) {
        if (ui.isLoggedIn) {
            snackbarHostState.showSnackbar(
                message = "Bienvenido ${ui.form.email}",
                duration = SnackbarDuration.Short
            )

            delay(500.milliseconds)
            onBack()
        }
    }

    Column(
        modifier = Modifier.padding(horizontal = 20.dp).padding(top = 36.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.align(alignment = Alignment.Start)
        ) {
            FigureIconButton(
                label = "Volver",
                callBack = onBack,
                icon = FeatherIcons.ArrowLeft,
                enabled = true,
            )
        }

        Text(text = "Ingresa aquí", style = CustomBrandTitleStyle)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = ui.form.email,
            onValueChange = authViewModel::updateEmail,
            label = { Text("Email", style = MaterialTheme.typography.bodyMedium) },
            isError = ui.form.emailError != null,
            supportingText = {
                if (ui.form.emailError != null) {
                    Text(text = ui.form.emailError!!)
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = ui.form.password,
            onValueChange = authViewModel::updatePassword,
            label = { Text("Contraseña", style = MaterialTheme.typography.bodyMedium) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = ui.form.passwordError != null,
            supportingText = {
                if (ui.form.passwordError != null) {
                    Text(text = ui.form.passwordError!!)
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp)) {
            Button(
                onClick = authViewModel::login,
                enabled = ui.login !is LoginScreenState.Loading,
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (ui.login is LoginScreenState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Ingresar", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        ClickableText(
            text = AnnotatedString("¿Password olvidada?"),
            onClick = { },
            style = CustomBrandParagraph
        )
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(thickness = 2.dp)
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp)) {
            Button(
                onClick = { },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray
                )
            ) {
                Text(text = "Registrate", style = MaterialTheme.typography.bodyMedium)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .padding(16.dp, 0.dp, 16.dp, 0.dp)) {
            Button(
                onClick = authViewModel::continueAsGuest,
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                Text(text = "Invitado", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
