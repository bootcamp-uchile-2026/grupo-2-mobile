package cl.uchile.dcc.mobile.ecotienda.ui.screen

// ScreenEnum :: String String  -> routing
// Crea la navegación de la tarea al gestionar mediante el MainScreenViewModel
// usando el ScreenEnum,
enum class ScreenEnum (
    val title: String,
    val route: String,
    val showTopBar: Boolean = true,
    val showBottomBar: Boolean = true
) {
    HOME(
        title = "Inicio",
        route = "HOME"
    ),

    CATALOG(
        title = "Catálogo",
        route = "Catálogo"
    ),

    PAGINAPRODUCTO(
        title = "Página Producto",
        route = "Página Producto"
    ),

    PRODUCER(
        showTopBar = false,
        title = "Productores",
        route = "Productores"
    ),

    LOGIN(
        showTopBar = false, 
        showBottomBar = false, 
        title = "Ingreso de Usuarios",
        route = "Login"
    ),

    CART(
        showTopBar = false, 
        showBottomBar = false, 
        title = "Carrito",
        route = "Carrito"
    ),

    SEARCH(
        showTopBar = false, 
        showBottomBar = false, 
        title = "Búsqueda",
        route = "Buscador"
    );
}
