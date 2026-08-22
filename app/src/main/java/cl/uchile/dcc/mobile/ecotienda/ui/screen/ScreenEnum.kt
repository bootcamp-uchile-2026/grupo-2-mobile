package cl.uchile.dcc.mobile.ecotienda.ui.screen

// ScreenEnum :: String String  -> routing
// Crea la navegación de la tarea al gestionar mediante el MainScreenViewModel
// usando el ScreenEnum,
enum class ScreenEnum (
    val title: String,
    val route: String,
) {
    HOME(
        title = "Inicio",
        route = "HOME"
    ),

    CATALOG(
        title = "Catálogo",
        route = "Catálogo"
    ),

    LOGIN(
    title = "Ingreso de Usuarios",
    route = "Login"
    ),

    PRODUCER(
        title = "Productores",
        route = "Productores"
    ),

    CART(
        title = "Carrito",
        route = "Carrito"
    ),

    PAGINAPRODUCTO(
        title = "Página Producto",
        route = "Página Producto"
    ),

    PAGINAPRODUCTOR(
        title = "Página de Productores",
        route = "Página de Productores"
    ),


}

