package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cl.uchile.dcc.mobile.ecotienda.data.DefaultData
import cl.uchile.dcc.mobile.ecotienda.ui.component.BottomNavigationBar
import cl.uchile.dcc.mobile.ecotienda.ui.component.SearchStaticBar
import cl.uchile.dcc.mobile.ecotienda.viewmodel.AuthViewModel
import cl.uchile.dcc.mobile.ecotienda.viewmodel.CartViewModel
import cl.uchile.dcc.mobile.ecotienda.viewmodel.MainScreenViewModel
import cl.uchile.dcc.mobile.ecotienda.viewmodel.ProducerDetailViewModel
import cl.uchile.dcc.mobile.ecotienda.viewmodel.ProductDetailViewModel
import cl.uchile.dcc.mobile.ecotienda.viewmodel.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcoTiendaApp(
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {},
    mainScreenViewModel: MainScreenViewModel = viewModel(),
    producerViewModel: ProducerDetailViewModel = viewModel(),
    productViewModel: ProductDetailViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(),
    searchViewModel: SearchViewModel = viewModel(),
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentScreen = ScreenRoutes.entries.find { it.route == currentRoute } ?: ScreenRoutes.HOME

    val isKeyboardOpen = WindowInsets.ime.getBottom(density) > 0

    val selectedProducer by producerViewModel.selectedProducer.collectAsState()
    val productDetailState by productViewModel.uiState.collectAsState()
    val cartState by cartViewModel.cart.collectAsState()
    val authState by authViewModel.state.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (currentScreen.showTopBar) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .windowInsetsPadding(WindowInsets.statusBars),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        SearchStaticBar(
                            onClick = { navController.navigate(route = "SEARCH") },
                            modifier = Modifier.weight(1f)
                        )

                        // Botón de alternador de tema Claro / Oscuro
                        IconButton(
                            onClick = onToggleTheme,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = if (isDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                                contentDescription = if (isDarkTheme) "Cambiar a modo claro" else "Cambiar a modo oscuro",
                                tint = Color.White
                            )
                        }
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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ScreenRoutes.LOADING.route,
        ) {
            composable(ScreenRoutes.HOME.route) {
                HomeEcoTienda(
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    productos = DefaultData.Product,
                    producers = DefaultData.Producer,
                    authViewModel = authViewModel,
                    onProductClick = { product ->
                        productViewModel.selectProduct(product)
                        navController.navigate(ScreenRoutes.PRODUCTPAGE.route)
                    },
                    onAgregarClick = { product ->
                        cartViewModel.addToCart(product, 1)
                        scope.launch {
                            snackbarHostState.showSnackbar("Añadido: ${product.productName}")
                        }
                    },
                    onProducerClick = { producer ->
                        producerViewModel.selectProducer(producer)
                        navController.navigate(ScreenRoutes.PRODUCERPAGE.route)
                    },
                    onSeeMoreProductsClick = {
                        navController.navigate(ScreenRoutes.CATALOG.route)
                    }
                )
            }
            composable(ScreenRoutes.CATALOG.route) {
                Catalog(
                    modifier = Modifier.padding(innerPadding),
                    onProductClick = { product ->
                        productViewModel.selectProduct(product)
                        navController.navigate(ScreenRoutes.PRODUCTPAGE.route)
                    },
                    onAgregarClick = { product ->
                        cartViewModel.addToCart(product, 1)
                        scope.launch {
                            snackbarHostState.showSnackbar("Añadido: ${product.productName}")
                        }
                    }
                )
            }
            composable(ScreenRoutes.SEARCH.route) {
                SearchScreen(
                    onBack = { navController.popBackStack() },
                    onSubmitSearch = { query ->
                        searchViewModel.onSearchSubmitted(query)
                        navController.navigate(ScreenRoutes.SEARCH_RESULTS.route)
                    },
                    searchViewModel = searchViewModel,
                    onProductClick = { product ->
                        productViewModel.selectProduct(product)
                        navController.navigate(ScreenRoutes.PRODUCTPAGE.route)
                    },
                    onAddToCart = { product ->
                        cartViewModel.addToCart(product, 1)
                        scope.launch {
                            snackbarHostState.showSnackbar("Añadido: ${product.productName}")
                        }
                    }
                )
            }
            composable(ScreenRoutes.SEARCH_RESULTS.route) {
                SearchResultsScreen(
                    searchViewModel = searchViewModel,
                    onBack = { navController.popBackStack() },
                    onProductClick = { product ->
                        productViewModel.selectProduct(product)
                        navController.navigate(ScreenRoutes.PRODUCTPAGE.route)
                    },
                    onAddToCart = { product ->
                        cartViewModel.addToCart(product, 1)
                        scope.launch {
                            snackbarHostState.showSnackbar("Añadido: ${product.productName}")
                        }
                    }
                )
            }
            composable(ScreenRoutes.ABOUT.route) {
                About(
                    modifier = Modifier.padding(innerPadding),
                    producers = DefaultData.Producer,
                    onProducerClick = { producer ->
                        producerViewModel.selectProducer(producer)
                        navController.navigate(ScreenRoutes.PRODUCERPAGE.route)
                    },
                )
            }
            composable(ScreenRoutes.PRODUCERPAGE.route) {
                selectedProducer?.let { producer ->
                    ProducerPage(
                        producer = producer,
                        onBack = { navController.popBackStack() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
            composable(ScreenRoutes.PRODUCTPAGE.route) {
                productDetailState.product?.let { product ->
                    ProductPage(
                        modifier = Modifier.padding(innerPadding),
                        product = product,
                        onAddToCart = { p, q ->
                            cartViewModel.addToCart(p, q)
                            scope.launch {
                                snackbarHostState.showSnackbar("Añadido: ${product.productName}")
                            }
                        },
                        onBack = { navController.popBackStack() },
                    )
                }
            }
            composable(ScreenRoutes.CART.route) {
                Cart(
                    modifier = Modifier.padding(innerPadding),
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
                        navController.navigate(ScreenRoutes.HOME.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onSignUp = { navController.navigate(ScreenRoutes.SIGNUP.route) }
                )
            }

            composable(ScreenRoutes.LOGIN.route) {
                LaunchedEffect(authState.isLoggedIn, authState.isGuest) {
                    if (authState.isLoggedIn || authState.isGuest) {
                        navController.navigate(ScreenRoutes.HOME.route) {
                            popUpTo(ScreenRoutes.LOGIN.route) { inclusive = true }
                        }
                    }
                }

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
                    onLogout = {},
                    onSignUp = { navController.navigate(ScreenRoutes.SIGNUP.route) }
                )
            }

            composable(ScreenRoutes.ORDERS.route) {
                Text(
                    "Mis compras",
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                )
            }
            composable(ScreenRoutes.LOADING.route) {
                LoadingScreen(
                    modifier = Modifier.padding(innerPadding),
                    viewModel = viewModel()
                )

                LaunchedEffect(Unit) {
                    delay(2000.milliseconds)
                    navController.navigate(ScreenRoutes.LOGIN.route) {
                        popUpTo(ScreenRoutes.LOADING.route) {
                            inclusive = true
                        }
                    }
                }
            }

            composable(ScreenRoutes.SIGNUP.route) {
                Signup(
                    authViewModel = authViewModel,
                    onBack = { navController.popBackStack() },
                    onSuccess = {
                        navController.navigate(ScreenRoutes.HOME.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(ScreenRoutes.HUELLAVERDE.route) {
                HuellaVerde(
                    modifier = Modifier.padding(innerPadding),
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
