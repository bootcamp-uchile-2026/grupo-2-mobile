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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import cl.uchile.dcc.mobile.ecotienda.ui.theme.DarkBrown
import cl.uchile.dcc.mobile.ecotienda.viewmodel.AuthViewModel
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft

@Composable
fun Login(
    modifier: Modifier,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    authViewModel: AuthViewModel = viewModel() // Viewmodel de Login
) {
    // Observamos el estado global de autenticación
    val ui by authViewModel.state.collectAsState()

    // Efecto para volver atrás automáticamente cuando el login sea exitoso
    LaunchedEffect(ui.isLoggedIn) {
        if (ui.isLoggedIn) {
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

        TextField(
            label = { Text(text = "Email") },
            value = ui.form.email,
            onValueChange = authViewModel::updateEmail,
            isError = ui.form.emailError != null,
            supportingText = {
                // Cambiamos el .let por un if simple para ayudar al compilador
                if (ui.form.emailError != null) {
                    Text(text = ui.form.emailError!!)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "Contraseña") },
            value = ui.form.password,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = authViewModel::updatePassword,
            isError = ui.form.passwordError != null,
            supportingText = {
                // Lo mismo aquí, usar if evita el error de inferencia de tipo T
                if (ui.form.passwordError != null) {
                    Text(text = ui.form.passwordError!!)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
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
                    Text(text = "Ingresa")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        ClickableText(
            text = AnnotatedString("¿Password olvidad?"),
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
                onClick = { },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Crea una cuenta")
            }

        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = { },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Continua como invitado")
            }
        }
    }
}