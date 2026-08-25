package cl.uchile.dcc.mobile.ecotienda.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.MaterialTheme
import cl.uchile.dcc.mobile.ecotienda.ui.theme.ecoTiendaColors

// FigureIconButton :: String callBack icon enabled -> Icon() { }
// Genera un Button con forma de Icon, que se puede hacer click
// ejemplo: FigureIconButton("Volver", callBack = { }, FeatherIcons.ArrowLeft) Crea un button en forma de flecha a la izquierda
@Composable
fun FigureIconButton(
    label: String,
    callBack: () -> Unit,
    icon: ImageVector,
    enabled: Boolean = true,
    iconColor: Color = MaterialTheme.ecoTiendaColors.cl3,
    iconCircleColor: Color = MaterialTheme.ecoTiendaColors.cl2,
) {
    IconButton(
        onClick = { callBack() },
        enabled = enabled,
        modifier = Modifier
            .background(iconCircleColor, CircleShape),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier,
            )
    }
}
