package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cl.uchile.dcc.mobile.ecotienda.model.DefaultData
import cl.uchile.dcc.mobile.ecotienda.model.Product
import cl.uchile.dcc.mobile.ecotienda.ui.component.AccountSidePanel
import cl.uchile.dcc.mobile.ecotienda.ui.component.BottomNavigationBar
import cl.uchile.dcc.mobile.ecotienda.ui.component.SearchStaticBar
import cl.uchile.dcc.mobile.ecotienda.viewmodel.MainScreenViewModel
import cl.uchile.dcc.mobile.ecotienda.ui.component.FigureIconButton
import cl.uchile.dcc.mobile.ecotienda.ui.theme.ecoTiendaColors
import cl.uchile.dcc.mobile.ecotienda.viewmodel.ProducerDetailViewModel
import cl.uchile.dcc.mobile.ecotienda.viewmodel.ProductDetailViewModel
import cl.uchile.dcc.mobile.ecotienda.viewmodel.CartViewModel
import cl.uchile.dcc.mobile.ecotienda.viewmodel.AuthViewModel
import compose.icons.FeatherIcons
import compose.icons.feathericons.User
import kotlinx.coroutines.launch

// EcoTiendaApp es el backbone de la aplicación
// Contiene Scafold, TopBar, BottomBar, Navcontroller e invoca a ViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcoTiendaApp(
    viewModel: MainScreenViewModel = viewModel(),
    producerViewModel: ProducerDetailViewModel = viewModel(),
    productViewModel: ProductDetailViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
) {
    // Implementación de navcontroller
    val navController = rememberNavController()

    // Se crea el estado de snackbarHostState
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    // BackStack
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 2. Buscamos la pantalla en el enum (Default a HOME si es nulo)
    val currentScreen = ScreenRoutes.entries.find { it.route == currentRoute } ?: ScreenRoutes.HOME

    // Se crea variable para registrar si el teclado esta presente en pantalla
    val isKeyboardOpen = WindowInsets.ime.getBottom(density) > 0

    // Generacion de pantalla para cada producer en ProducerPage
    val selectedProducer by producerViewModel.selectedProducer.collectAsState()
    val productDetailState by productViewModel.uiState.collectAsState()
    val cartState by cartViewModel.cart.collectAsState()
    val authState by authViewModel.state.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        // Mensajes
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (currentScreen.showTopBar) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .windowInsetsPadding(WindowInsets.statusBars),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Este componente es solo visual y dispara la navegación
                    SearchStaticBar(
                        onClick = { navController.navigate(route = "SEARCH" ) },
                        modifier = Modifier.weight(1f)
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        FigureIconButton(
                            label = "Usuario",
                            callBack = { navController.navigate(ScreenRoutes.ACCOUNT.route) },
                            icon = Icons.Filled.Person,
                        )
                        Text(
                            text = if (authState.isLoggedIn) authState.userEmail?.split("@")?.get(0) ?: "Usuario" 
                                   else if (authState.isGuest) "Invitado" 
                                   else "Ingresar",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.ecoTiendaColors.cl3,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (!isKeyboardOpen && currentScreen.showBottomBar) {
                BottomNavigationBar(
                    currentRoute = navController.currentBackStackEntry?.destination?.route,
                    onNavigateTo = {
                        navController.navigate(it)
                    }
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
    )
    { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ScreenRoutes.LOGIN.route, // Cambio a Login First
        ) {
            composable(ScreenRoutes.HOME.route) {
                HomeEcoTienda(
                    modifier = Modifier
                        .padding(innerPadding),
                    // Se pasa estado de snackbarHostState
                    snackbarHostState = snackbarHostState,
                    productos = DefaultData.Product,
                    authViewModel = authViewModel,
                    // Ir al detalle al tocar la tarjeta
                    onProductClick = { product ->
                        productViewModel.selectProduct(product) // Usamos productViewModel
                        navController.navigate(ScreenRoutes.PRODUCTPAGE.route)
                    },

                    // Agregar al carro al tocar el botón verde
                    onAgregarClick = { product ->
                        cartViewModel.addToCart(product, 1) // Usamos cartViewModel
                        scope.launch {
                            snackbarHostState.showSnackbar("Añadido: ${product.productName}")
                        }
                    }
                )
            }
            composable(ScreenRoutes.CATALOG.route) {
                Catalog(
                    modifier = Modifier
                        .padding(innerPadding),
                    onProductClick = { product ->
                        productViewModel.selectProduct(product) // Usamos productViewModel
                        navController.navigate(ScreenRoutes.PRODUCTPAGE.route)
                    },

                    // Agregar al carro al tocar el botón verde
                    onAgregarClick = { product ->
                        cartViewModel.addToCart(product, 1) // Usamos cartViewModel
                        scope.launch {
                            snackbarHostState.showSnackbar("Añadido: ${product.productName}")
                        }
                    }
                )
            }
            composable(ScreenRoutes.SEARCH.route) {
                SearchScreen(
                onBack = { navController.popBackStack() },
                onSearch = { /* TODO */ }
                )
            }
            composable(ScreenRoutes.ABOUT.route) {
                About(
                    modifier = Modifier
                        .padding(innerPadding),
                    producers = DefaultData.Producer,
                    onProducerClick = { producer ->
                        producerViewModel.selectProducer(producer) // Aquí se guarda
                        navController.navigate(ScreenRoutes.PRODUCERPAGE.route) // Aqui navega
                   },
                )
            }
            composable(ScreenRoutes.PRODUCERPAGE.route) {
                // Si hay un productor seleccionado, mostramos su página
               selectedProducer?.let { producer ->
                    ProducerPage(
                        producer = producer,
                        onBack = { navController.popBackStack() },
                        modifier = Modifier
                            .padding(innerPadding)
                    )
                }
            }
            composable(ScreenRoutes.PRODUCTPAGE.route) {
                productDetailState.product?.let { product ->
                    ProductPage(
                        modifier = Modifier.padding(innerPadding),
                        product = product,
                        onAddToCart = { p, q -> cartViewModel.addToCart(p, q)
                            scope.launch {
                                snackbarHostState.showSnackbar("Añadido: ${product.productName}")
                            }},
                        onBack = { navController.popBackStack() },
                    )
                }
            }
            composable(ScreenRoutes.CART.route) {
                Cart(
                    modifier = Modifier
                    .padding(innerPadding),
                    cart = cartState,
                    authViewModel = authViewModel,
                    cartViewModel = cartViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(ScreenRoutes.ACCOUNT.route) {
                AccountGate(
                    modifier = Modifier.padding(innerPadding),
                    authViewModel = authViewModel,
                    snackbarHostState = snackbarHostState,
                    onEnterApp = {
                        navController.navigate(ScreenRoutes.HOME.route) {
                            popUpTo(ScreenRoutes.ACCOUNT.route) { inclusive = true }
                        }
                    },
                    onOpenOrders = { navController.navigate(ScreenRoutes.ORDERS.route) },
                    onLogout = {
                        // AuthViewModel ya deja isLoggedIn/isGuest en false
                        // AccountGate vuelve a mostrar Login solo
                    }
                )
            }

            composable(ScreenRoutes.LOGIN.route) {
                // por si algo viejo navega a LOGIN
                AccountGate(
                    modifier = Modifier.padding(innerPadding),
                    authViewModel = authViewModel,
                    snackbarHostState = snackbarHostState,
                    onEnterApp = {
                        navController.navigate(ScreenRoutes.HOME.route) {
                            popUpTo(ScreenRoutes.LOGIN.route) { inclusive = true }
                        }
                    },
                    onOpenOrders = { navController.navigate(ScreenRoutes.ORDERS.route) },
                    onLogout = {}
                )
            }

            composable(ScreenRoutes.ORDERS.route) {
                // placeholder hasta que tener datos de compras
                Text(
                    "Mis compras",
                    modifier = Modifier.padding(innerPadding).padding(16.dp)
                )
            }

        }
    }
    // Sidepanel para login/account
    AccountSidePanel(
        visible = authState.showLoginSheet,
        onDismiss = { authViewModel.dismissLoginSheet() }
    ) {
        AccountPanelContent(
            authViewModel = authViewModel,
            snackbarHostState = snackbarHostState,
            onGoHome = { authViewModel.dismissLoginSheet() },
            onOpenOrders = {
                authViewModel.dismissLoginSheet()
                navController.navigate(ScreenRoutes.ORDERS.route)
            }
        )
    }

}
