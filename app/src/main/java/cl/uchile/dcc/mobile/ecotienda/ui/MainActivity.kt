package cl.uchile.dcc.mobile.ecotienda.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import cl.uchile.dcc.mobile.ecotienda.data.repository.BuenOrigenDataRepository
import cl.uchile.dcc.mobile.ecotienda.ui.screen.EcoTiendaApp
import cl.uchile.dcc.mobile.ecotienda.ui.theme.EcoTiendaTheme
import cl.uchile.dcc.mobile.ecotienda.viewmodel.MainScreenViewModel

class MainActivity : ComponentActivity() {
    private lateinit var  viewModel: MainScreenViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "BuenOrigenConfig")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val configPreferences: DataStore<Preferences> = this.dataStore
        val valuesPreferences = getSharedPreferences("valuesApp", MODE_PRIVATE)

        val configRepo = BuenOrigenDataRepository(
            configPreferences,
            valuesPreferences,
            applicationContext,
        )

        viewModel = MainScreenViewModel(configRepo)

        setContent {
            EcoTiendaTheme {
                EcoTiendaApp(viewModel)
            }
        }
    }
}