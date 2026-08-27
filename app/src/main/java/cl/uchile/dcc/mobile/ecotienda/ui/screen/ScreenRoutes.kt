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
    HOME(
        title = "Inicio",
        route = "HOME",
        icon = Icons.Filled.Home
    ),

    CATALOG(
        title = "Catálogo",
        route = "CATALOG",
        icon = Icons.Filled.GridView

    ),

    PRODUCTPAGE(
        title = "Página Producto",
        route = "PRODUCTPAGE",
        icon = Icons.Filled.Nature
    ),


    // Sin topbar
    PRODUCERPAGE(
        showTopBar = false,
        title = "Página de Productores",
        route = "PRODUCERPAGE",
        icon = Icons.Filled.NaturePeople
    ),

    ABOUT(
        showTopBar = false,
        title = "Nosotros",
        route = "ABOUT",
        icon = Icons.Filled.People

    ),

    // Sin ninguna barra (ni bottom ni top)
    LOGIN(
        showTopBar = false,
        showBottomBar = false,
        title = "Ingreso de Usuarios",
        route = "Login",
        icon = Icons.Filled.Person
    ),

    CART(
        showTopBar = false,
        showBottomBar = false,
        title = "Carrito",
        route = "CART",
        icon = Icons.Filled.ShoppingCart
    ),

    SEARCH(
        showTopBar = false,
        showBottomBar = false,
        title = "Búsqueda",
        route = "SEARCH",
        icon = Icons.Filled.Search
    );
}