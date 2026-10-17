package cl.uchile.dcc.mobile.ecotienda.data.repository

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import cl.uchile.dcc.mobile.ecotienda.data.User
import cl.uchile.dcc.mobile.ecotienda.data.database.BuenOrigenOpenHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object PreferencesKeys {
    val THEME_MOD = stringPreferencesKey("TemaActual")
    val PRODUCTS = stringPreferencesKey("Productos")
    val USERNAME = stringPreferencesKey("NombreUsuario")

    // Claves de sesión
    val IS_LOGGED_IN = booleanPreferencesKey("IsLoggedIn")
    val IS_GUEST = booleanPreferencesKey("IsGuest")
    val USER_EMAIL = stringPreferencesKey("UserEmail")
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

    private val dbHelper = BuenOrigenOpenHelper(context)

    fun registerUser(user: User): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("email", user.email)
            put("password", user.password)
            put("name", user.name)
        }
        val result = db.insert("User", null, values)
        db.close()
        return result != -1L
    }

    fun loginUser(email: String, pass: String): User? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "User",
            arrayOf("id", "email", "password", "name"),
            "email = ? AND password = ?",
            arrayOf(email, pass),
            null, null, null
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            )
        }
        cursor.close()
        db.close()
        return user
    }

    // Loggeo, usuario y contraseña
    val isLoggedIn: Flow<Boolean> = config.data
        .map { preferences -> preferences[PreferencesKeys.IS_LOGGED_IN] ?: false }

    val isGuest: Flow<Boolean> = config.data
        .map { preferences -> preferences[PreferencesKeys.IS_GUEST] ?: false }

    val userEmail: Flow<String?> = config.data
        .map { preferences -> preferences[PreferencesKeys.USER_EMAIL] }

    // Guardamos el estado de la sesión al iniciar sesión o continuar como invitado
    suspend fun saveSession(isLoggedIn: Boolean, isGuest: Boolean, email: String?, name: String) {
        config.edit { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] = isLoggedIn
            preferences[PreferencesKeys.IS_GUEST] = isGuest
            if (email != null) {
                preferences[PreferencesKeys.USER_EMAIL] = email
            } else {
                preferences.remove(PreferencesKeys.USER_EMAIL)
            }
            preferences[PreferencesKeys.USERNAME] = name
        }
    }

    // Limpia el estado de la sesión al cerrar sesión
    suspend fun clearSession() {
        config.edit { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] = false
            preferences[PreferencesKeys.IS_GUEST] = false
            preferences.remove(PreferencesKeys.USER_EMAIL)
            preferences[PreferencesKeys.USERNAME] = "Invitado"
        }
    }
}