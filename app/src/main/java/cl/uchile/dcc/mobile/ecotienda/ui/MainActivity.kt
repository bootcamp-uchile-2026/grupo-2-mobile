package cl.uchile.dcc.mobile.ecotienda.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.uchile.dcc.mobile.ecotienda.ui.component.BottomNavigationBar
import cl.uchile.dcc.mobile.ecotienda.ui.component.SearchBarID
import cl.uchile.dcc.mobile.ecotienda.ui.screen.HomeEcoTienda
import cl.uchile.dcc.mobile.ecotienda.ui.screen.ProductosEcoTienda
import cl.uchile.dcc.mobile.ecotienda.ui.screen.ScreenEnum
import cl.uchile.dcc.mobile.ecotienda.ui.screen.SearchScreen
import cl.uchile.dcc.mobile.ecotienda.ui.theme.EcoTiendaTheme
import cl.uchile.dcc.mobile.ecotienda.viewmodel.MainScreenViewModel
import cl.uchile.dcc.mobile.gastospersonales.ui.component.FigureIconButton
import compose.icons.FeatherIcons
import compose.icons.feathericons.ShoppingCart
import compose.icons.feathericons.User

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
            if (actualScreen != ScreenEnum.SEARCH) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically, // Alinea verticalmente los iconos
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Espacio entre barra y botón

                ) {
                    SearchBarID(
                        TextFieldState(),
                        onSearch = {  },
                        listOf(),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { screenViewModel.changeScreen(ScreenEnum.SEARCH) } //Navega a pantalla SearchScreen()
                    )
                    FigureIconButton(
                        "Usuario",
                        callBack = { /* TODO */ },
                        FeatherIcons.User,
                        modifier = Modifier
                            .align(Alignment.Bottom)
                    )
                }
            }
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
            ScreenEnum.SEARCH -> SearchScreen(
                onBack = { screenViewModel.changeScreen(ScreenEnum.HOME) },
                onSearch = { /* TODO */ }
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