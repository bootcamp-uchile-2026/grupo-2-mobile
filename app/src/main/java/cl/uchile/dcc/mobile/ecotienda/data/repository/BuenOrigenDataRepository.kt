package cl.uchile.dcc.mobile.ecotienda.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object PreferencesKeys {
    val THEME_MOD = stringPreferencesKey("TemaActual")
    val PRODUCTS = stringPreferencesKey("Productos")
    val USERNAME = stringPreferencesKey("NombreUsuario")
}

class BuenOrigenDataRepository(
    private val config: DataStore<Preferences>,
    private val value: SharedPreferences,
    private val context: Context
) {
    // database
//    private val database = BuenOrigenDataRepository.getInstance(context)

    val theme: Flow<String> = config.data
        .map { preferences ->
        preferences[PreferencesKeys.THEME_MOD] ?: "Auto"
    }

    suspend  fun setTheme(theme: String) {
        config.edit { preferences ->
            preferences[PreferencesKeys.THEME_MOD] = theme
        }
    }

    val products: Flow<String> = config.data
        .map { preferences ->
            preferences[PreferencesKeys.PRODUCTS] ?: ""
        }

    suspend fun setProducts(products: String) {
        config.edit { preferences ->
            preferences[PreferencesKeys.PRODUCTS] = products
        }
    }

    val name: Flow<String> = config.data
        .map { preferences ->
            preferences[PreferencesKeys.USERNAME] ?: "Invitado"
        }

    suspend fun setName(name: String) {
        config.edit { preferences ->
            preferences[PreferencesKeys.USERNAME] = name
        }
    }

}