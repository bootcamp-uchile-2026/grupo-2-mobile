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
import cl.uchile.dcc.mobile.ecotienda.ui.screen.ScreenEnum
import cl.uchile.dcc.mobile.ecotienda.ui.theme.DarkBrown
import cl.uchile.dcc.mobile.ecotienda.ui.theme.EcoGreen
import cl.uchile.dcc.mobile.ecotienda.ui.theme.SoftBeige
import compose.icons.FeatherIcons
import compose.icons.feathericons.Grid
import compose.icons.feathericons.Home
import compose.icons.feathericons.ShoppingCart
import compose.icons.feathericons.Users

@Composable
fun BottomNavigationBar(
    currentScreen: ScreenEnum,
    onScreenSelected: (ScreenEnum) -> Unit,
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
        NavigationBarItem(
            selected = currentScreen == ScreenEnum.HOME,
            onClick = { onScreenSelected(ScreenEnum.HOME) },
            icon = { Icon(FeatherIcons.Home, contentDescription = "Inicio") },
            label = { Text(ScreenEnum.HOME.title) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                indicatorColor = Color.Transparent,
                unselectedIconColor = unselectedColor,
                selectedTextColor = selectedColor,
                unselectedTextColor = unselectedColor,
            )
        )
        NavigationBarItem(
            selected = currentScreen == ScreenEnum.CATALOG,
            onClick = {  onScreenSelected(ScreenEnum.CATALOG) },
            icon = { Icon(FeatherIcons.Grid, contentDescription = "Catálogo", ) },
            label = { Text(ScreenEnum.CATALOG.title) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                indicatorColor = Color.Transparent,
                unselectedIconColor = unselectedColor,
                selectedTextColor = selectedColor,
                unselectedTextColor = unselectedColor,
            )
        )
        NavigationBarItem(
            selected = currentScreen == ScreenEnum.PRODUCER,
            onClick = { onScreenSelected(ScreenEnum.PRODUCER)},
            icon = { Icon(FeatherIcons.Users, contentDescription = "Productores") },
            label = { Text(ScreenEnum.PRODUCER.title) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                indicatorColor = Color.Transparent,
                unselectedIconColor = unselectedColor,
                selectedTextColor = selectedColor,
                unselectedTextColor = unselectedColor,
            )
        )
        NavigationBarItem(
            selected = currentScreen == ScreenEnum.CART,
            onClick = { onScreenSelected(ScreenEnum.CART) },
            icon = { Icon(FeatherIcons.ShoppingCart, contentDescription = "Carrito") },
            label = { Text(ScreenEnum.CART.title) },
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