package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cl.uchile.dcc.mobile.ecotienda.model.DefaultData
import cl.uchile.dcc.mobile.ecotienda.ui.component.BottomNavigationBar
import cl.uchile.dcc.mobile.ecotienda.ui.component.SearchStaticBar
import cl.uchile.dcc.mobile.ecotienda.viewmodel.MainScreenViewModel
import cl.uchile.dcc.mobile.ecotienda.ui.component.FigureIconButton
import compose.icons.FeatherIcons
import compose.icons.feathericons.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcoTiendaApp(
) {
    // Implementación de navcontroller
    val navController = rememberNavController()
    val density = LocalDensity.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 2. Buscamos la pantalla en el enum (Default a HOME si es nulo)
    val currentScreen = ScreenRoutes.entries.find { it.route == currentRoute } ?: ScreenRoutes.HOME

    // Se crea variable para registrar si el teclado esta presente en pantalla
    val isKeyboardOpen = WindowInsets.ime.getBottom(density) > 0

    // Generacion de pantalla para cada producer en ProducerPage

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (currentScreen.showTopBar) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Este componente es solo visual y dispara la navegación
                    SearchStaticBar(
                        onClick = { navController.navigate(route = "SEARCH" ) },
                        modifier = Modifier.weight(1f)
                    )

                    FigureIconButton(
                    )
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
        ) {
            composable(ScreenRoutes.HOME.route) {
                HomeEcoTienda(
                    modifier = Modifier
                        .padding(innerPadding),
                    // Se pasa estado de snackbarHostState
                    snackbarHostState = snackbarHostState,
                    productos = DefaultData.Product,
            }
            composable(ScreenRoutes.CATALOG.route) {
                Catalog(
                    modifier = Modifier
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
                        navController.navigate(ScreenRoutes.PRODUCERPAGE.route) // Aqui navega
                   },
                )
            }
            composable(ScreenRoutes.PRODUCERPAGE.route) {
               selectedProducer?.let { producer ->
                    ProducerPage(
                        producer = producer,
                    )
                }
            }
            composable(ScreenRoutes.CART.route) {
                Cart(
                    modifier = Modifier
                    .padding(innerPadding),
                    onBack = { navController.popBackStack() }
                )
            }

        }
    }
}
