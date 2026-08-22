package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

enum class ScreenRoutes(
    val title: String,
    val route: String,
    val icon: ImageVector,
    val showTopBar: Boolean = true,
    val showBottomBar: Boolean = true
) {

    // Sin topbar

    // Sin ninguna barra (ni bottom ni top)
}