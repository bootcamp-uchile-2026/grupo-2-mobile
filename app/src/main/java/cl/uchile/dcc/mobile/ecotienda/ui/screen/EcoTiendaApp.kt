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
    viewModel: MainScreenViewModel = viewModel()
) {
    // La navegación se gestiona mediante el MainScreenViewModel usando el ScreenEnum
    val currentRoute by viewModel.currentRoute.collectAsState()
    val density = LocalDensity.current

    // Se crea variable para registrar si el teclado esta presente en pantalla
    val isKeyboardOpen = WindowInsets.ime.getBottom(density) > 0

    // Se crea el estado de snackbarHostState
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (currentRoute.showTopBar) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, top = 32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Este componente es solo visual y dispara la navegación
                    SearchStaticBar(
                        onClick = { viewModel.navigateTo(route = "SEARCH" ) },
                        modifier = Modifier.weight(1f)
                    )

                    FigureIconButton(
                        "Usuario",
                        callBack = { viewModel.navigateTo(route = "LOGIN") },
                        FeatherIcons.User,
                        modifier = Modifier
                            .align(Alignment.Bottom)
                    )
                }
            }
        },
        bottomBar = {
            if (!isKeyboardOpen && currentRoute.showBottomBar) {
                BottomNavigationBar(
                    currentRoute = currentRoute.route, // Proviene de viewModel.currentRoute
                    onNavigateTo = {
                        viewModel.navigateTo(it)
                    }
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
    )
    { innerPadding ->
        when (currentRoute) {
            Routes.HOME-> {
                HomeEcoTienda(
                    modifier = Modifier
                        .padding(innerPadding),
                    // Se pasa estado de snackbarHostState
                    snackbarHostState = snackbarHostState,
                    productos = DefaultData.Product,
                    onAgregarClick = { /* */ }
                )
            }
            Routes.SEARCH->
                SearchScreen(
                onBack = { viewModel.navigateTo(route = "HOME" )  },
                onSearch = { /* TODO */ }
            )

            Routes.CATALOG->{
                Catalog(
                    modifier = Modifier
                        .padding(innerPadding)
                )
            }
            Routes.ABOUT->
                About(
                )

            Routes.CART->
                Cart(
                    modifier = Modifier
                    .padding(innerPadding),
                    onBack = { viewModel.navigateTo(route = "HOME" )  }
                )
            else -> {}
        }
    }
}
