package cl.uchile.dcc.mobile.ecotienda.ui.screen

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.EnergySavingsLeaf
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.outlined.EnergySavingsLeaf
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Nature
import androidx.compose.material.icons.outlined.NaturePeople
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

// ScreenRoutes ENUM de pantallas en la app
enum class ScreenRoutes(
    val title: String,
    val route: String,
    val icon: ImageVector,
    val unselectedIcon: ImageVector = icon,
    val showTopBar: Boolean = true,
    val showBottomBar: Boolean = true
) {

    HUELLAVERDE( title = "Huella Verde", route = "HUELLAVERDE", icon = Icons.Filled.EnergySavingsLeaf, unselectedIcon = Icons.Outlined.EnergySavingsLeaf),
    CART(title = "Carrito", route = "CART", icon = Icons.Filled.ShoppingCart, unselectedIcon = Icons.Outlined.ShoppingCart),
    HOME(title = "Inicio", route = "HOME", icon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home),
    CATALOG(title = "Catálogo", route = "CATALOG", icon = Icons.Filled.GridView, unselectedIcon = Icons.Outlined.GridView),
    PRODUCTPAGE(showTopBar = false, title = "Página Producto", route = "PRODUCTPAGE", icon = Icons.Filled.Nature, unselectedIcon = Icons.Outlined.Nature),
    ORDERS(title = "Página Ordenes", route = "ORDERS", icon = Icons.Filled.Nature, unselectedIcon = Icons.Outlined.Nature ),
    SEARCH(title = "Búsqueda", route = "SEARCH", icon = Icons.Filled.Search, unselectedIcon = Icons.Outlined.Search),
    BLOG(title = "Blog", route = "BLOG", icon = Icons.AutoMirrored.Filled.Article, unselectedIcon = Icons.AutoMirrored.Outlined.Article),
    ARTICLE(title = "Artículo", route = "ARTICLE", icon = Icons.AutoMirrored.Filled.Article, unselectedIcon = Icons.AutoMirrored.Outlined.Article),

    ACCOUNT(title = "Cuenta de Usuarios", route = "Account", icon = Icons.Filled.Person2),

    // Sin topbar
    PRODUCERPAGE(showTopBar = false, title = "Página de Productores", route = "PRODUCERPAGE", icon = Icons.Filled.NaturePeople, unselectedIcon = Icons.Outlined.NaturePeople),
    ABOUT(showTopBar = false, title = "Nosotros", route = "ABOUT", icon = Icons.Filled.People, unselectedIcon = Icons.Outlined.People),
    PHOTOCOMP(showTopBar = false, title = "Página Toma de foto", route = "PHOTOCOMP", icon = Icons.Filled.Photo, unselectedIcon = Icons.Outlined.Photo),


    // Sin ninguna barra (ni bottom ni top)
    LOGIN(showTopBar = false, showBottomBar = false, title = "Ingreso de Usuarios", route = "Login", icon = Icons.Filled.Person),
    SEARCH_RESULTS(showTopBar = false, showBottomBar = false, title = "Resultados de búsqueda", route = "SEARCH_RESULTS", icon = Icons.Filled.Search, unselectedIcon = Icons.Outlined.Search),
    LOADING(showTopBar = false, showBottomBar = false, title = "Cargando", route = "LOADING", icon = Icons.Filled.Search, unselectedIcon = Icons.Outlined.Search),
    SIGNUP(showTopBar = false, showBottomBar = false, title = "Registro", route = "SIGNUP", icon = Icons.Filled.Person, unselectedIcon = Icons.Outlined.Person),


}