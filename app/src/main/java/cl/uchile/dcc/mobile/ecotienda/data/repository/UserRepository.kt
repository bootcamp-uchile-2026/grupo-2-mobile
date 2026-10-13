package cl.uchile.dcc.mobile.ecotienda.data.database_open_helper

import android.content.ContentValues
import android.content.Context
import cl.uchile.dcc.mobile.ecotienda.data.database.User

class UserRepository(
    private val context: Context
) {
    private val dbHelper = BuenOrigenOpenHelper(context)

    fun registerUser(user: User): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("email", user.email)
            put("password", user.password)
            put("name", user.name)
        }
        val result = db.insert(BuenOrigenOpenHelper.TABLE_USER_REGISTRY, null, values)
        db.close()
        return result != -1L
    }

    fun loginUser(email: String, pass: String): User? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            BuenOrigenOpenHelper.TABLE_USER_REGISTRY,
            arrayOf("id", "email", "password", "name"),
            "email = ? AND password = ?",
            arrayOf(email, pass),
            null, null, null
        )

        var user: User? = null
        with(cursor) {
            if (moveToFirst()) {
                user = User(
                    id = getLong(getColumnIndexOrThrow("id")),
                    email = getString(getColumnIndexOrThrow("email")),
                    password = getString(getColumnIndexOrThrow("password")),
                    name = getString(getColumnIndexOrThrow("name"))
                )
            }
        }
        cursor.close()
        db.close()
        return user
    }
}
