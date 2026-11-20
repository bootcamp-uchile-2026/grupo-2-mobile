package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cl.uchile.dcc.mobile.ecotienda.R
import cl.uchile.dcc.mobile.ecotienda.data.database.Producer
import cl.uchile.dcc.mobile.ecotienda.data.database.Product
import cl.uchile.dcc.mobile.ecotienda.data.repository.CartRepository
import androidx.compose.material3.rememberModalBottomSheetState
import cl.uchile.dcc.mobile.ecotienda.ui.component.BottomNavigationBar
import cl.uchile.dcc.mobile.ecotienda.ui.component.FilterBottomSheet
import cl.uchile.dcc.mobile.ecotienda.ui.component.NotificationIconButton
import cl.uchile.dcc.mobile.ecotienda.ui.component.SearchStaticBar
import cl.uchile.dcc.mobile.ecotienda.ui.theme.CustomBrandTitleStyle
import cl.uchile.dcc.mobile.ecotienda.viewmodel.AuthViewModel
import cl.uchile.dcc.mobile.ecotienda.viewmodel.BlogViewModel
import cl.uchile.dcc.mobile.ecotienda.viewmodel.CartAddResult
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
    blogViewModel: BlogViewModel = viewModel(),
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
    val blogState by blogViewModel.uiState.collectAsState()
    val searchUiState by searchViewModel.uiState.collectAsState()

    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    fun addAndNotify(product: Product, quantity: Int, openCart: Boolean = false) {
        val result = cartViewModel.addToCart(product, quantity)
        val message = when (result) {
            CartAddResult.NO_STOCK -> "Sin stock"
            CartAddResult.AT_MAX, CartAddResult.LIMITED -> "Máximo 3 unidades"
            CartAddResult.ADDED -> "Añadido: ${product.productName}"
        }
        if (!(result == CartAddResult.ADDED && openCart)) {
            scope.launch { snackbarHostState.showSnackbar(message) }
        }
        if (openCart && result != CartAddResult.NO_STOCK) {
            navController.navigate(ScreenRoutes.CART.route)
        }
    }

    val appContext = LocalContext.current.applicationContext
    var catalogProducts by remember { mutableStateOf<List<Product>>(emptyList()) }
    var catalogProducers by remember { mutableStateOf<List<Producer>>(emptyList()) }
    val productsById = catalogProducts.associateBy { it.id }
    val cartCo2Kg = cartState.items.sumOf { item ->
        (productsById[item.productId]?.co2Avoided ?: 0.0) * item.quantity
    }
    val cartGreenPoints = cartState.items.sumOf { item ->
        (productsById[item.productId]?.greenPoints ?: 0) * item.quantity
    }
    val cartQuantityCaps = cartState.items.associate { item ->
        val product = productsById[item.productId]
        val cap = when {
            product == null -> CartRepository.MAX_UNITS_PER_PRODUCT
            !product.available || product.stock <= 0 -> 1
            else -> minOf(CartRepository.MAX_UNITS_PER_PRODUCT, product.stock).coerceAtLeast(1)
        }
        item.productId to cap
    }

    LaunchedEffect(Unit) {
        val container = (appContext as? cl.uchile.dcc.mobile.ecotienda.data.repository.EcoTiendaApp)
            ?.container ?: return@LaunchedEffect
        catalogProducts = container.productRepository.getAllProduct()
        catalogProducers = container.producerRepository.getAllProducers()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (currentScreen.showTopBar) {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(WindowInsets.statusBars)
                            .padding(top = 10.dp, bottom = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Logo centrado con Buen Origen (logo_solo + texto)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo_solo),
                                contentDescription = "Logo Buen Origen",
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Buen Origen",
                                style = CustomBrandTitleStyle.copy(
                                    fontSize = 24.sp,
                                    lineHeight = 28.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }

                        // Barra de búsqueda interactiva con menú de filtrado y campana de notificaciones
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            SearchStaticBar(
                                query = searchUiState.query,
                                onQueryChange = searchViewModel::onQueryChange,
                                onSearch = { searchViewModel.onSearchSubmitted(searchUiState.query) },
                                onFilterClick = { showFilterSheet = true },
                                modifier = Modifier.weight(1f)
                            )

                            NotificationIconButton(
                                count = 1,
                                onClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Tienes 1 notificación nueva: ¡Envíos gratis en tu primera compra!")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (!isKeyboardOpen && currentScreen.showBottomBar) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigateTo = { route ->
                        if (route == ScreenRoutes.HOME.route) {
                            // Limpia la búsqueda y los filtros para restaurar el Home original
                            searchViewModel.clearSearch()

                            val returned = navController.popBackStack(
                                ScreenRoutes.HOME.route,
                                inclusive = false,
                            )
                            if (!returned && navController.currentDestination?.route != ScreenRoutes.HOME.route) {
                                navController.navigate(ScreenRoutes.HOME.route) {
                                    popUpTo(0) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        } else {
                            navController.navigate(route) {
                                popUpTo(ScreenRoutes.HOME.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
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
                    productos = catalogProducts,
                    producers = catalogProducers,
                    authViewModel = authViewModel,
                    searchViewModel = searchViewModel,
                    onProductClick = { product ->
                        productViewModel.selectProduct(product)
                        navController.navigate(ScreenRoutes.PRODUCTPAGE.route)
                    },
                    onAgregarClick = { product ->
                        addAndNotify(product, 1)
                    },
                    onProducerClick = { producer ->
                        producerViewModel.selectProducer(producer)
                        navController.navigate(ScreenRoutes.PRODUCERPAGE.route)
                    },
                    onSeeMoreProductsClick = {
                        navController.navigate(ScreenRoutes.CATALOG.route)
                    },
                    articles = blogState.articles,
                    articlesLoading = blogState.isLoading,
                    articlesError = blogState.error,
                    onOpenBlog = { navController.navigate(ScreenRoutes.BLOG.route) },
                    onOpenArticle = { articleId ->
                        blogViewModel.openArticle(articleId)
                        navController.navigate(ScreenRoutes.ARTICLE.route)
                    },
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
                        addAndNotify(product, 1)
                    }
                )
            }
            composable(ScreenRoutes.SEARCH.route) {
                SearchScreen(
                    onBack = { navController.popBackStack()
                        searchViewModel.clearSearch()},
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
                        addAndNotify(product, 1)
                    }
                )
            }
            composable(ScreenRoutes.SEARCH_RESULTS.route) {
                SearchResultsScreen(
                    searchViewModel = searchViewModel,
                    onBack = { navController.popBackStack()
                        searchViewModel.clearSearch()},
                    onProductClick = { product ->
                        productViewModel.selectProduct(product)
                        navController.navigate(ScreenRoutes.PRODUCTPAGE.route)
                    },
                    onAddToCart = { product ->
                        addAndNotify(product, 1)
                    }
                )
            }
            composable(ScreenRoutes.ABOUT.route) {
                About(
                    modifier = Modifier.padding(innerPadding),
                    producers = catalogProducers,
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
                        onAddToCart = { p, q -> addAndNotify(p, q) },
                        onBack = { navController.popBackStack() },
                        onTakePhoto = { navController.navigate(ScreenRoutes.PHOTOCOMP.route) },
                        onOpenCart = { navController.navigate(ScreenRoutes.CART.route) },
                    )
                }
            }
            composable(ScreenRoutes.CART.route) {
                Cart(
                    modifier = Modifier.padding(innerPadding),
                    cart = cartState,
                    authViewModel = authViewModel,
                    cartViewModel = cartViewModel,
                    productCategories = catalogProducts.associateBy({ it.id }, { it.category }),
                    quantityCaps = cartQuantityCaps,
                    co2Kg = cartCo2Kg,
                    greenPoints = cartGreenPoints,
                    onBack = { navController.popBackStack() },
                    onContinueShopping = {
                        navController.navigate(ScreenRoutes.HOME.route) {
                            launchSingleTop = true
                        }
                    },
                )
            }

            composable(ScreenRoutes.ACCOUNT.route) {
                AccountGate(
                    modifier = Modifier.padding(innerPadding),
                    authViewModel = authViewModel,
                    snackbarHostState = snackbarHostState,
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = onToggleTheme,
                    onEnterApp = {
                        navController.navigate(ScreenRoutes.HOME.route) {
                            popUpTo(ScreenRoutes.ACCOUNT.route) { inclusive = true }
                        }
                    },
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
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = onToggleTheme,
                    onEnterApp = {
                        navController.navigate(ScreenRoutes.HOME.route) {
                            popUpTo(ScreenRoutes.LOGIN.route) { inclusive = true }
                        }
                    },
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

            composable(ScreenRoutes.BLOG.route) {
                BlogScreen(
                    modifier = Modifier.padding(innerPadding),
                    articles = blogState.articles,
                    isLoading = blogState.isLoading,
                    error = blogState.error,
                    onBack = { navController.popBackStack() },
                    onOpenArticle = { articleId ->
                        blogViewModel.openArticle(articleId)
                        navController.navigate(ScreenRoutes.ARTICLE.route)
                    },
                )
            }
            composable(ScreenRoutes.ARTICLE.route) {
                ArticleScreen(
                    modifier = Modifier.padding(innerPadding),
                    detail = blogState.detail,
                    isLoading = blogState.detailLoading,
                    error = blogState.detailError,
                    onBack = { navController.popBackStack() },
                )
            }
            composable(ScreenRoutes.HUELLAVERDE.route) {
                HuellaVerde(
                    modifier = Modifier.padding(innerPadding),
                    onBack = { navController.popBackStack() }
                )
            }

            composable(ScreenRoutes.PHOTOCOMP.route) {
                PhotoComp(
                    modifier = Modifier.padding(innerPadding),
                    product = productDetailState.product,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        if (showFilterSheet) {
            FilterBottomSheet(
                sheetState = sheetState,
                currentFilter = searchUiState.filter,
                searchQuery = searchUiState.query,
                onDismiss = { showFilterSheet = false },
                onApplyFilter = { newFilter ->
                    searchViewModel.updateFilter(newFilter)
                    showFilterSheet = false
                }
            )
        }
    }
}
