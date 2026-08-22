package cl.uchile.dcc.mobile.ecotienda.ui.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cl.uchile.dcc.mobile.ecotienda.ui.screen.ScreenRoutes
import cl.uchile.dcc.mobile.ecotienda.ui.theme.DarkBrown
import cl.uchile.dcc.mobile.ecotienda.ui.theme.EcoGreen
import cl.uchile.dcc.mobile.ecotienda.ui.theme.SoftBeige

// BottomNavigationBar :: String callBack Colors -> BottomBar() { }
// Genera una barra de navegacion abajo con atajos a las pantallas: HOME, CATALOG, ABOUT, CART
// ejemplo: BottomNavigationBar ("HOME", callBack = { }, Colors) Crea una barra con atajos

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets.navigationBars,
    containerColor: Color = SoftBeige,
    selectedColor: Color = EcoGreen,
    selectedTextColor: Color = selectedColor,
    unselectedColor: Color = DarkBrown.copy(alpha = 0.7f),
    unselectedTextColor: Color = unselectedColor,

    )
{
    NavigationBar(
        modifier = modifier,
        containerColor = containerColor,
        tonalElevation = 8.dp
    ) {
        val sections = listOf(
            ScreenRoutes.HOME,
            ScreenRoutes.CATALOG,
            ScreenRoutes.ABOUT,
            ScreenRoutes.CART
        )

        sections.forEach { section ->
            NavigationBarItem(
                selected = currentRoute == section.route,
                onClick = { onNavigateTo(section.route) },
                icon = { Icon(section.icon, contentDescription = section.title) },
                label = { Text(section.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedColor,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = unselectedColor,
                    selectedTextColor = selectedColor,
                    unselectedTextColor = unselectedColor,
                )
            )
        }
    }
}