package cl.uchile.dcc.mobile.ecotienda.data.repository

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private const val USER_PREFERENCES_NAME = "ecotienda_preferences"

val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_NAME,
)

// Contenedor de repositorios (AppContainer) centralizado
class AppContainer(context: Context) {
    private val appContext = context.applicationContext

    val buenOrigenRepository = BuenOrigenDataRepository(
        config = appContext.userPreferencesDataStore,
        value = appContext.getSharedPreferences("ecotienda", Context.MODE_PRIVATE),
        context = appContext,
    )
}

class EcoTiendaApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}