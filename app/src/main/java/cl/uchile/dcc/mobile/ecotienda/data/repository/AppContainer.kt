package cl.uchile.dcc.mobile.ecotienda.data.repository

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import cl.uchile.dcc.mobile.ecotienda.data.database.BuenOrigenDatabase

private const val USER_PREFERENCES_NAME = "ecotienda_preferences"

val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_NAME,
)

// Contenedor de repositorios (AppContainer) centralizado con Room
class AppContainer(context: Context) {
    private val appContext = context.applicationContext

    val database: BuenOrigenDatabase by lazy {
        BuenOrigenDatabase.getInstance(appContext)
    }

    val productRepository: ProductRepository by lazy {
        ProductRepository(database.productDAO)
    }

    val producerRepository: ProducerRepository by lazy {
        ProducerRepository(database.producerDAO)
    }

    val userRepository: UserRepository by lazy {
        UserRepository(database.userDAO)
    }

    val cartRepository: CartRepository by lazy {
        CartRepository(database.cartItemDAO)
    }

    val buenOrigenRepository: BuenOrigenDataRepository by lazy {
        BuenOrigenDataRepository(
            config = appContext.userPreferencesDataStore,
            value = appContext.getSharedPreferences("ecotienda", Context.MODE_PRIVATE),
            context = appContext,
        )
    }
}

class EcoTiendaApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        DatabaseSeeder.seedIfEmpty(this)
        container = AppContainer(this)
    }
}
