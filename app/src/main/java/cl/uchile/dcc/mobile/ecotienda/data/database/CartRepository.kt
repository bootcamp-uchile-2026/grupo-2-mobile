package cl.uchile.dcc.mobile.ecotienda.data.database

import android.content.ContentValues
import android.content.Context
import cl.uchile.dcc.mobile.ecotienda.data.CartItem

class CartRepository(
    private val context: Context
) {
    private val dbHelper = BuenOrigenOpenHelper(context)

    fun getCartItems(): List<CartItem> {
        val items = mutableListOf<CartItem>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            BuenOrigenOpenHelper.TABLE_CART_ITEM_REGISTRY,
            null, null, null, null, null, null
        )
        with(cursor) {
            while (moveToNext()) {
                val productId = getString(getColumnIndexOrThrow("productId"))
                val productName = getString(getColumnIndexOrThrow("productName"))
                val price = getInt(getColumnIndexOrThrow("price"))
                val imageUrl = getString(getColumnIndexOrThrow("imageUrl"))
                val producerID = getString(getColumnIndexOrThrow("producerID"))
                val productProducer = getString(getColumnIndexOrThrow("productProducer"))
                val quantity = getInt(getColumnIndexOrThrow("quantity"))

                items.add(
                    CartItem(
                        productId = productId,
                        productName = productName,
                        price = price,
                        imageUrl = imageUrl ?: "",
                        producerID = producerID ?: "",
                        productProducer = productProducer ?: "",
                        quantity = quantity
                    )
                )
            }
        }
        cursor.close()
        db.close()
        return items
    }

    fun addCartItem(item: CartItem) {
        val db = dbHelper.writableDatabase
        // Verificar si ya existe el producto en el carrito
        val cursor = db.query(
            BuenOrigenOpenHelper.TABLE_CART_ITEM_REGISTRY,
            arrayOf("id", "quantity"),
            "productId = ?",
            arrayOf(item.productId),
            null, null, null
        )
        if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val currentQty = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"))
            val newQty = currentQty + item.quantity
            val values = ContentValues().apply {
                put("quantity", newQty)
            }
            db.update(
                BuenOrigenOpenHelper.TABLE_CART_ITEM_REGISTRY,
                values,
                "id = ?",
                arrayOf(id.toString())
            )
        } else {
            val values = ContentValues().apply {
                put("productId", item.productId)
                put("productName", item.productName)
                put("price", item.price)
                put("imageUrl", item.imageUrl)
                put("producerID", item.producerID)
                put("productProducer", item.productProducer)
                put("quantity", item.quantity)
            }
            db.insert(BuenOrigenOpenHelper.TABLE_CART_ITEM_REGISTRY, null, values)
        }
        cursor.close()
        db.close()
    }

    fun updateQuantity(productId: String, quantity: Int) {
        val db = dbHelper.writableDatabase
        if (quantity <= 0) {
            removeCartItem(productId)
        } else {
            val values = ContentValues().apply {
                put("quantity", quantity)
            }
            db.update(
                BuenOrigenOpenHelper.TABLE_CART_ITEM_REGISTRY,
                values,
                "productId = ?",
                arrayOf(productId)
            )
        }
        db.close()
    }

    fun removeCartItem(productId: String) {
        val db = dbHelper.writableDatabase
        db.delete(
            BuenOrigenOpenHelper.TABLE_CART_ITEM_REGISTRY,
            "productId = ?",
            arrayOf(productId)
        )
        db.close()
    }

    fun clearCart() {
        val db = dbHelper.writableDatabase
        db.delete(BuenOrigenOpenHelper.TABLE_CART_ITEM_REGISTRY, null, null)
        db.close()
    }
}
