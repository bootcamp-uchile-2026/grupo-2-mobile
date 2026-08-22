package cl.uchile.dcc.mobile.ecotienda.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.uchile.dcc.mobile.ecotienda.ui.component.BottomNavigationBar
import cl.uchile.dcc.mobile.ecotienda.ui.screen.HomeEcoTienda
import cl.uchile.dcc.mobile.ecotienda.ui.screen.ProductosEcoTienda
import cl.uchile.dcc.mobile.ecotienda.ui.screen.ScreenEnum
import cl.uchile.dcc.mobile.ecotienda.ui.theme.EcoTiendaTheme
import cl.uchile.dcc.mobile.ecotienda.viewmodel.MainScreenViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoTiendaTheme {
                MainScreen()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(screenViewModel: MainScreenViewModel = viewModel()) {
    // La navegación se gestiona mediante el MainScreenViewModel usando el ScreenEnum
    val actualScreen = screenViewModel.actualScreen
    val density = LocalDensity.current

    // Se crea variable para registrar si el teclado esta presente en pantalla
    val isKeyboardOpen = WindowInsets.ime.getBottom(density) > 0

    // Se crea el estado de snackbarHostState
    val snackbarHostState = remember { SnackbarHostState() }


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
        },
        bottomBar = {
            if (!isKeyboardOpen) {
                BottomNavigationBar(
                    currentScreen = actualScreen, // Proviene de screenViewModel.actualScreen
                    onScreenSelected = { newScreen ->
                        screenViewModel.changeScreen(newScreen)
                    }
                )
                    }



//                NavigationBar(
//                    windowInsets = WindowInsets.navigationBars
//                ) {
//                    // NavigationBarItem crea un tipo IconButton y texto que permite navegar entre pantalla
//                    NavigationBarItem(
//                        selected = actualScreen == ScreenEnum.HOME,
//                        onClick = { screenViewModel.changeScreen(ScreenEnum.HOME) },
//                        icon = {
//                            Icon(
//                                FeatherIcons.Home,
//                                contentDescription = ScreenEnum.HOME.title
//                            )
//                        },
//                        label = { Text(ScreenEnum.HOME.title) }
//                    )
//                    NavigationBarItem(
//                        selected = actualScreen == ScreenEnum.CATALOGO,
//                        onClick = { screenViewModel.changeScreen(ScreenEnum.CATALOGO) },
//                        icon = {
//                            Icon(
//                                FeatherIcons.Grid,
//                                contentDescription = ScreenEnum.CATALOGO.title
//                            )
//                        },
//                        label = { Text(ScreenEnum.CATALOGO.title) }
//                    )
//                    NavigationBarItem(
//                        selected = actualScreen == ScreenEnum.PRODUCTORES,
//                        onClick = { screenViewModel.changeScreen(ScreenEnum.PRODUCTORES) },
//                        icon = {
//                            Icon(
//                                FeatherIcons.Users,
//                                contentDescription = ScreenEnum.PRODUCTORES.title
//                            )
//                        },
//                        label = { Text(ScreenEnum.CATALOGO.title) }
//                    )
//                    NavigationBarItem(
//                        selected = actualScreen == ScreenEnum.HUELLAVERDE,
//                        onClick = { screenViewModel.changeScreen(ScreenEnum.HUELLAVERDE) },
//                        icon = {
//                            Icon(
//                                FeatherIcons.CloudRain,
//                                contentDescription = ScreenEnum.HUELLAVERDE.title
//                            )
//                        },
//                        label = { Text(ScreenEnum.HUELLAVERDE.title) }
//                    )
//                }

            },
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
            ) { innerPadding ->
            when (actualScreen) {
                ScreenEnum.HOME -> HomeEcoTienda(
                    modifier = Modifier
                        .padding(innerPadding),
                    // Se pasa estado de snackbarHostState
                    snackbarHostState = snackbarHostState
                )

                ScreenEnum.CATALOG -> ProductosEcoTienda(
                    modifier = Modifier
                        .padding(innerPadding)
                )

                else -> {}
            }
        }
        }

        @Preview(showBackground = true)
        @Composable
        fun GreetingPreview() {
            EcoTiendaTheme {
            }
        }