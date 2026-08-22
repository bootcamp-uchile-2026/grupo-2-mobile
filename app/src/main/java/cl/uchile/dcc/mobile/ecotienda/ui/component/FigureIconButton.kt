package cl.uchile.dcc.mobile.gastospersonales.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cl.uchile.dcc.mobile.ecotienda.ui.theme.DarkBrown
import cl.uchile.dcc.mobile.ecotienda.ui.theme.EcoGreen
import cl.uchile.dcc.mobile.ecotienda.ui.theme.SoftBeige

// FigureIconButton :: String callBack icon enabled -> Icon() { }
// Genera un Button con forma de Icon, que se puede hacer click
// ejemplo: FigureIconButton("Volver", callBack = { }, FeatherIcons.ArrowLeft) Crea un button en forma de flecha a la izquierda
@Composable
fun FigureIconButton(
    label: String,
    callBack: () -> Unit,
    icon: ImageVector,
    enabled: Boolean = true,
    modifier: Modifier,
) {
    IconButton(
        onClick = { callBack() },
        enabled = enabled,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .padding(top = 8.dp, end = 8.dp),
            )
    }
}
