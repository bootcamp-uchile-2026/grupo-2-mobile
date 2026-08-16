package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.uchile.dcc.mobile.ecotienda.ui.component.FigureIconButton
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import kotlin.text.Typography.section

@Composable
fun Cart (modifier: Modifier,
          onBack: () -> Unit) {
    Box(
        modifier = modifier
            .padding(4.dp)) {
        val sections = listOf(
            Routes.HOME,
            Routes.CATALOG,
            Routes.ABOUT,
            Routes.CART
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

