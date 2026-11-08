package cl.uchile.dcc.mobile.ecotienda.data.repository

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import cl.uchile.dcc.mobile.ecotienda.data.database.BuenOrigenDatabase
import cl.uchile.dcc.mobile.ecotienda.data.network.ApiRepository

private const val USER_PREFERENCES_NAME = "ecotienda_preferences"

// Reune y entrega las despendencias compartidas a la app
val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_NAME,
)

// Contenedor de repositorios (AppContainer) centralizado con Room y Retrofit API
class AppContainer(context: Context) {
    private val appContext = context.applicationContext

    val database: BuenOrigenDatabase by lazy {
        BuenOrigenDatabase.getInstance(appContext)
    }

    val productRepository: ProductRepository by lazy {
        ProductRepository(
            productDao = database.productDAO,
            apiService = ApiRepository.productApiService
        )
    }

    val producerRepository: ProducerRepository by lazy {
        ProducerRepository(
            producerDao = database.producerDAO,
            apiService = ApiRepository.producerApiService
        )
    }

    val userRepository: UserRepository by lazy {
        UserRepository(
            userDao = database.userDAO,
            apiService = ApiRepository.userApiService
        )
    }

    val cartRepository: CartRepository by lazy {
        CartRepository(
            cartItemDao = database.cartItemDAO,
            apiService = ApiRepository.cartApiService
        )
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
