package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.EnergySavingsLeaf
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.uchile.dcc.mobile.ecotienda.viewmodel.AuthViewModel
import java.text.NumberFormat
import java.util.Locale

private enum class ProfileSection {
    ORDERS,
    REVIEWS,
    GIFTS,
    ADDRESSES,
    MESSAGES,
    NOTIFICATIONS,
    PROFILE,
    SETTINGS,
    HELP,
}

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    onLogout: () -> Unit,
    orderCount: Int = 0,
    reviewCount: Int = 0,
    greenPoints: Int = 0,
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {},
) {
    var sectionName by rememberSaveable { mutableStateOf<String?>(null) }
    val section = sectionName?.let { name -> ProfileSection.entries.firstOrNull { it.name == name } }
    BackHandler(enabled = section != null) { sectionName = null }

    if (section == null) {
        ProfileHome(
            modifier = modifier,
            authViewModel = authViewModel,
            orderCount = orderCount,
            reviewCount = reviewCount,
            greenPoints = greenPoints,
            isDarkTheme = isDarkTheme,
            onToggleTheme = onToggleTheme,
            onOpen = { sectionName = it.name },
        )
    } else {
        ProfileSectionScreen(
            modifier = modifier,
            section = section,
            authViewModel = authViewModel,
            isDarkTheme = isDarkTheme,
            onToggleTheme = onToggleTheme,
            onBack = { sectionName = null },
            onLogout = onLogout,
        )
    }
}

@Composable
private fun ProfileHome(
    modifier: Modifier,
    authViewModel: AuthViewModel,
    orderCount: Int,
    reviewCount: Int,
    greenPoints: Int,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onOpen: (ProfileSection) -> Unit,
) {
    val authState by authViewModel.state.collectAsState()
    val storedName by authViewModel.userName.collectAsState()
    val fullName = displayName(storedName, authState.userEmail)
    val hello = fullName.substringBefore(" ").ifBlank { fullName }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "¡Hola $hello!",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(18.dp))
        Surface(
            modifier = Modifier.size(112.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Foto de perfil",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp),
                )
            }
        }
        Spacer(Modifier.height(14.dp))
        Text(
            text = fullName,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Outlined.EnergySavingsLeaf,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = " Tienes ${formatCount(greenPoints)} puntos verdes",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(Modifier.height(22.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ProfileCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                icon = Icons.Outlined.ShoppingBag,
                title = "Historial de compras",
                subtitle = purchasesLabel(orderCount),
                onClick = { onOpen(ProfileSection.ORDERS) },
            )
            ProfileCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                icon = Icons.Outlined.StarOutline,
                title = "Reseñas y calificaciones",
                subtitle = reviewsLabel(reviewCount),
                onClick = { onOpen(ProfileSection.REVIEWS) },
            )
        }
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ProfileCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                icon = Icons.Outlined.CardGiftcard,
                title = "Gift cards",
                subtitle = null,
                onClick = { onOpen(ProfileSection.GIFTS) },
            )
            ProfileCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                icon = Icons.Outlined.Place,
                title = "Administrar direcciones",
                subtitle = null,
                onClick = { onOpen(ProfileSection.ADDRESSES) },
            )
        }

        Spacer(Modifier.height(22.dp))

        // Opción directa para alternar tema claro / oscuro en la cuenta
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = if (isDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            text = "Modo oscuro",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = if (isDarkTheme) "Activado" else "Desactivado",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { onToggleTheme() }
                )
            }
        }

        Spacer(Modifier.height(14.dp))
        ProfileRow("Mensajes") { onOpen(ProfileSection.MESSAGES) }
        ProfileRow("Notificaciones y Ofertas") { onOpen(ProfileSection.NOTIFICATIONS) }
        ProfileRow("Perfil") { onOpen(ProfileSection.PROFILE) }
        ProfileRow("Configuración de la aplicación") { onOpen(ProfileSection.SETTINGS) }
        ProfileRow("Ayuda") { onOpen(ProfileSection.HELP) }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun ProfileCard(
    modifier: Modifier,
    icon: ImageVector,
    title: String,
    subtitle: String?,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp),
            )
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun ProfileRow(
    label: String,
    onClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
            )
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}

@Composable
private fun ProfileSectionScreen(
    modifier: Modifier,
    section: ProfileSection,
    authViewModel: AuthViewModel,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onBack: () -> Unit,
    onLogout: () -> Unit,
) {
    val authState by authViewModel.state.collectAsState()
    val storedName by authViewModel.userName.collectAsState()
    val fullName = displayName(storedName, authState.userEmail)
    val title = when (section) {
        ProfileSection.ORDERS -> "Historial de compras"
        ProfileSection.REVIEWS -> "Reseñas y calificaciones"
        ProfileSection.GIFTS -> "Gift cards"
        ProfileSection.ADDRESSES -> "Administrar direcciones"
        ProfileSection.MESSAGES -> "Mensajes"
        ProfileSection.NOTIFICATIONS -> "Notificaciones y Ofertas"
        ProfileSection.PROFILE -> "Perfil"
        ProfileSection.SETTINGS -> "Configuración de la aplicación"
        ProfileSection.HELP -> "Ayuda"
    }
    val body = when (section) {
        ProfileSection.ORDERS -> "Todavía no hay compras. Cuando un pedido quede registrado en tu cuenta, va a aparecer en esta lista."
        ProfileSection.REVIEWS -> "Todavía no hay reseñas. Después de recibir un pedido vas a poder calificar el producto."
        ProfileSection.ADDRESSES -> "Todavía no hay direcciones guardadas. Por ahora la dirección se escribe al pagar."
        ProfileSection.GIFTS -> "Las gift cards todavía no están disponibles en la tienda."
        ProfileSection.MESSAGES -> "No tienes mensajes."
        ProfileSection.NOTIFICATIONS -> "No tienes notificaciones ni ofertas nuevas."
        ProfileSection.SETTINGS -> "Personaliza la apariencia de la aplicación:"
        ProfileSection.HELP -> "Si el catálogo no carga, cierra la app y vuelve a abrirla."
        ProfileSection.PROFILE -> null
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 8.dp),
    ) {
        TextButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            Text("Volver", modifier = Modifier.padding(start = 6.dp))
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(16.dp))
        if (section == ProfileSection.PROFILE) {
            Text(fullName, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(6.dp))
            Text(
                text = authState.userEmail.orEmpty(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(24.dp))
            OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                Text("Cerrar sesión")
            }
        } else if (section == ProfileSection.SETTINGS) {
            Text(
                text = "Personaliza el aspecto visual de la aplicación:",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(16.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "Modo oscuro",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = if (isDarkTheme) "Activado" else "Desactivado",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { onToggleTheme() }
                    )
                }
            }
        } else {
            Text(
                text = body.orEmpty(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private fun displayName(storedName: String, email: String?): String {
    val name = storedName.trim()
    if (name.isNotBlank() && !name.equals("Invitado", ignoreCase = true)) {
        return name.replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase(Locale.forLanguageTag("es-CL")) else char.toString()
        }
    }
    val fromEmail = email?.substringBefore("@")?.trim().orEmpty()
    if (fromEmail.isNotBlank()) {
        return fromEmail.replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase(Locale.forLanguageTag("es-CL")) else char.toString()
        }
    }
    return "Usuario"
}

private fun formatCount(value: Int): String {
    return NumberFormat.getIntegerInstance(Locale.forLanguageTag("es-CL")).format(value)
}

private fun purchasesLabel(count: Int): String {
    return if (count == 1) "1 compra" else "${formatCount(count)} compras"
}

private fun reviewsLabel(count: Int): String {
    return if (count == 1) "1 reseña" else "${formatCount(count)} reseñas"
}
