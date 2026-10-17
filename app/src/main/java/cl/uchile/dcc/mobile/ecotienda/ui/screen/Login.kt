package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.uchile.dcc.mobile.ecotienda.R
import cl.uchile.dcc.mobile.ecotienda.ui.component.FigureIconButton
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.LoginScreenState
import cl.uchile.dcc.mobile.ecotienda.ui.theme.CustomBrandParagraph
import cl.uchile.dcc.mobile.ecotienda.ui.theme.CustomBrandTitleStyle
import cl.uchile.dcc.mobile.ecotienda.viewmodel.AuthViewModel
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

// Login pagina de ingreso a aplicación, autenticacion fake
@Composable
fun Login(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    authViewModel: AuthViewModel = viewModel(), // Viewmodel de Login
    onSuccess: () -> Unit = {},
    onSignUp: () -> Unit = {}
) {
    // Observamos el estado global de autenticación
    val user by authViewModel.state.collectAsState()
    val authState by authViewModel.state.collectAsState()
    val form = authState.form

    var passwordVisible by remember { mutableStateOf(false) }

    // Cuando el fake login o el invitado terminan, avisamos al NavHost
    LaunchedEffect(authState.isLoggedIn, authState.isGuest) {
        if (authState.isLoggedIn || authState.isGuest) {
            onSuccess()
        }
    }

    // Vuelve atrás automáticamente cuando el login sea correcto
    LaunchedEffect(user.isLoggedIn) {
        if (user.isLoggedIn) {
            snackbarHostState.showSnackbar(
                message = "Bienvenido ${user.form.email}",
                duration = SnackbarDuration.Short
            )

            // Esperar 0,5 segundos (500 ms) antes de volver atrás
            delay(500.milliseconds)

            // Backstack
            onBack()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 16.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            FigureIconButton(
                label = "Volver",
                callBack = onBack,
                icon = FeatherIcons.ArrowLeft,
                enabled = true,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo_solo),
            contentDescription = "Logo EcoTienda",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Subtítulo
        Text(
            text = "Cuidemos el planeta una compra a la vez",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Título Principal
        Text(
            text = "¡Bienvenido!",
            style = CustomBrandTitleStyle.copy(
                fontSize = 36.sp,
                textAlign = TextAlign.Center
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Campo Email con etiqueta superior
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Email",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            OutlinedTextField(
                value = form.email,
                onValueChange = { authViewModel.updateEmail(it) },
                placeholder = {
                    Text(
                        "Ingresa tu email",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                },
                isError = form.emailError != null,
                supportingText = form.emailError?.let { { Text(it) } },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    errorContainerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Contraseña con etiqueta superior y visor de contraseña
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Contraseña",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            OutlinedTextField(
                value = form.password,
                onValueChange = { authViewModel.updatePassword(it) },
                placeholder = {
                    Text(
                        "Ingresa tu contraseña",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                },
                isError = form.passwordError != null,
                supportingText = form.passwordError?.let { { Text(it) } },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else
                        Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    errorContainerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón "Ingresar"
        Button(
            onClick = { authViewModel.login() },
            enabled = authState.login !is LoginScreenState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = Color.White
            )
        ) {
            if (authState.login is LoginScreenState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    "Ingresar",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Enlaces de recuperar contraseña y registro
        val forgotPasswordText = buildAnnotatedString {
            append("¿Olvidaste tu contraseña? ")
            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                append("Haz click aquí")
            }
        }
        ClickableText(
            text = forgotPasswordText,
            onClick = { },
            style = CustomBrandParagraph.copy(
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        val signUpText = buildAnnotatedString {
            append("¿No tienes una cuenta? ")
            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                append("Crea una aquí")
            }
        }
        ClickableText(
            text = signUpText,
            onClick = { onSignUp() },
            style = CustomBrandParagraph.copy(
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Separador con "o"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.Gray.copy(alpha = 0.4f)
            )
            Text(
                text = "o",
                modifier = Modifier.padding(horizontal = 12.dp),
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.Gray.copy(alpha = 0.4f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botones de Inicio de Sesión Social: Apple, Facebook, Google
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SocialButton(
                text = "",
                onClick = { }
            )
            SocialButton(
                text = "f",
                onClick = { }
            )
            SocialButton(
                text = "G",
                onClick = { }
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Texto pie de página
        val termsText = buildAnnotatedString {
            append("Al continuar, aceptas nuestros ")
            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                append("Términos de Uso")
            }
            append(" y ")
            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                append("Políticas de Privacidad")
            }
        }
        Text(
            text = termsText,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun SocialButton(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = Color(0xFFEFE8D8),
        modifier = Modifier.size(48.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
            )
        }
    }
}
