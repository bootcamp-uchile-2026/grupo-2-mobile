package cl.uchile.dcc.mobile.ecotienda.data.database_open_helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BuenOrigenOpenHelper(context: Context): SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object {
        const val DATABASE_NAME = "BuenOrigen.db"
        const val DATABASE_VERSION = 4

        const val TABLE_PRODUCT_REGISTRY = "ProductRegistry"

        const val TABLE_PRODUCER_REGISTRY = "ProducerRegistry"

        const val TABLE_CART_ITEM_REGISTRY = "CartItemRegistry"

        const val TABLE_USER_REGISTRY = "UserRegistry"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val tableProduct = """
            CREATE TABLE $TABLE_PRODUCT_REGISTRY (
                id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                description TEXT NOT NULL,
                price INTEGER NOT NULL,
                stock INTEGER NOT NULL,
                category TEXT NOT NULL,
                imageUrl TEXT,
                productProducer TEXT,
                producerID TEXT
            );
        """.trimIndent()
        p0?.execSQL(tableProduct)
        val tableProducer = """
            CREATE TABLE $TABLE_PRODUCER_REGISTRY (
                id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                description TEXT NOT NULL,
                imageUrl TEXT,
                history TEXT,
                location TEXT
            );
        """.trimIndent()
        p0?.execSQL(tableProducer)
        val tableCartItem = """
            CREATE TABLE $TABLE_CART_ITEM_REGISTRY (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                productId TEXT NOT NULL,
                productName TEXT NOT NULL,
                price INTEGER NOT NULL,
                imageUrl TEXT,
                producerID TEXT,
                productProducer TEXT,
                quantity INTEGER NOT NULL
            );
        """.trimIndent()
        p0?.execSQL(tableCartItem)
        val tableUser = """
            CREATE TABLE $TABLE_USER_REGISTRY (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                name TEXT NOT NULL
            );
        """.trimIndent()
        p0?.execSQL(tableUser)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        if (oldVersion < 4) {
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCT_REGISTRY")
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCER_REGISTRY")
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_CART_ITEM_REGISTRY")
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_USER_REGISTRY")
            onCreate(db)
        }
    }
}
