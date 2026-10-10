package cl.uchile.dcc.mobile.ecotienda.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.uchile.dcc.mobile.ecotienda.ui.screen.EcoTiendaApp
import cl.uchile.dcc.mobile.ecotienda.ui.theme.EcoTiendaTheme

// MainActivity liviana: se mudó el contenedor de repos a AppContainer
// y la obtención de ViewModels se maneja automáticamente en cada ViewModel
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            EcoTiendaTheme {
                EcoTiendaApp()
            }
        }
    }
}