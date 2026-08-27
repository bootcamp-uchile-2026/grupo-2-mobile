package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.uchile.dcc.mobile.ecotienda.ui.component.FigureIconButton
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.LoginScreenState
import cl.uchile.dcc.mobile.ecotienda.ui.theme.md_theme_light_secondaryContainer
import cl.uchile.dcc.mobile.ecotienda.ui.theme.md_theme_light_tertiaryContainer
import cl.uchile.dcc.mobile.ecotienda.viewmodel.AuthViewModel
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun Login(
    modifier: Modifier,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    authViewModel: AuthViewModel = viewModel(), // Viewmodel de Login
    onSuccess: () -> Unit = {},
) {
    // Observamos el estado global de autenticación
    val ui by authViewModel.state.collectAsState()

    val authState by authViewModel.state.collectAsState()
    val form = authState.form

// Cuando el fake login o el invitado terminan, avisamos al NavHost
    LaunchedEffect(authState.isLoggedIn, authState.isGuest) {
        if (authState.isLoggedIn || authState.isGuest) {
            onSuccess()
        }
    }

    // Vuelve atrás automáticamente cuando el login sea correcto
    LaunchedEffect(ui.isLoggedIn) {
        if (ui.isLoggedIn) {
            snackbarHostState.showSnackbar(
                message = "Bienvenido ${ui.form.email}",
                duration = SnackbarDuration.Short
            )

            // Esperar 2 segundos (2000 ms) antes de volver atrás
            delay(1000.milliseconds)

            // Backstack
            onBack()
        }
    }
    Column(
        modifier = Modifier.padding(20.dp, top = 36.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .align(alignment = Alignment.Start)
        ) {
        FigureIconButton(
            label = "Volver",
            callBack = onBack,
            icon = FeatherIcons.ArrowLeft,
            enabled = true,
        )
        }

        Text(text = "Ingresa aquí", style = TextStyle(fontSize = 40.sp))

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = form.email,
            onValueChange = { authViewModel.updateEmail(it) },
            label = { Text("Email") },
            isError = form.emailError != null,
            supportingText = { form.emailError?.let { Text(it) } },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = form.password,
            onValueChange = { authViewModel.updatePassword(it) },
            label = { Text("Contraseña") },
            isError = form.passwordError != null,
            supportingText = { form.passwordError?.let { Text(it) } },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = { authViewModel.login() },
                enabled = authState.login !is LoginScreenState.Loading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
            ) {
                if (authState.login is LoginScreenState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text("Ingresar")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        ClickableText(
            text = AnnotatedString("¿Password olvidada?"),
            onClick = { },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(thickness = 2.dp)
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {  },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = md_theme_light_tertiaryContainer
                )
            ) {
                Text(text = "Crea una cuenta")
            }

        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                shape = RoundedCornerShape(50.dp),
                onClick = { authViewModel.continueAsGuest() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Continua como invitado")
            }
        }
    }
}