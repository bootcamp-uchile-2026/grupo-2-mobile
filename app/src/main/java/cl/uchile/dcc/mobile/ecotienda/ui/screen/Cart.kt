package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.uchile.dcc.mobile.ecotienda.ui.component.FigureIconButton
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft

@Composable
fun Cart (modifier: Modifier,
          onBack: () -> Unit) {
    Box(
        modifier = modifier
            .padding(4.dp)) {
        val sections = listOf(
            ScreenRoutes.HOME,
            ScreenRoutes.CATALOG,
            ScreenRoutes.ABOUT,
            ScreenRoutes.CART
        )
        FigureIconButton(
            label = "Volver",
            callBack = onBack,
            icon = FeatherIcons.ArrowLeft,
            enabled = true,
            modifier = Modifier
        )
    }
}

