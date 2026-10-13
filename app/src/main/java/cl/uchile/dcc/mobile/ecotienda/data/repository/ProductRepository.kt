package cl.uchile.dcc.mobile.ecotienda.data.database_open_helper

import android.content.ContentValues
import android.content.Context
import cl.uchile.dcc.mobile.ecotienda.data.database.Product

class ProductRepository(
    private val context: Context
) {
    private val dbHelper = BuenOrigenOpenHelper(context)

    // CREAR del Crud productos
    fun addProduct(product: Product) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("id", product.id)
            put("name", product.productName)
            put("description", product.description)
            put("price", product.price)
            put("stock", product.stock)
            put("category", product.category)
            put("imageUrl", product.imageUrl)
            put("productProducer", product.productProducer)
            put("producerID", product.producerID)
        }
        db.insert(BuenOrigenOpenHelper.TABLE_PRODUCT_REGISTRY, null, values)
        db.close()
    }

    // READ del CRUD
    fun getAllProduct(): List<Product> {
        val products = mutableListOf<Product>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            BuenOrigenOpenHelper.TABLE_PRODUCT_REGISTRY,
            null,
            null,
            null,
            null,
            null,
            null
        )
        with(cursor) {
            while (moveToNext()) {
                val id = getString(getColumnIndexOrThrow("id"))
                val name = getString(getColumnIndexOrThrow("name"))
                val description = getString(getColumnIndexOrThrow("description"))
                val price = getInt(getColumnIndexOrThrow("price"))
                val stock = getInt(getColumnIndexOrThrow("stock"))
                val category = getString(getColumnIndexOrThrow("category"))
                val imageUrl = getString(getColumnIndexOrThrow("imageUrl"))
                val productProducer = getString(getColumnIndexOrThrow("productProducer"))
                val producerID = getString(getColumnIndexOrThrow("producerID"))

                val product = Product(
                    id = id,
                    productName = name,
                    description = description,
                    price = price,
                    stock = stock,
                    category = category,
                    imageUrl = imageUrl ?: "",
                    productProducer = productProducer ?: "",
                    producerID = producerID ?: ""
                )
                products.add(product)
            }
        }
        cursor.close()
        db.close()
        return products
    }

    // UPDATE de CRUD
    fun updateProduct(product: Product) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", product.productName)
            put("description", product.description)
            put("price", product.price)
            put("stock", product.stock)
            put("category", product.category)
            put("imageUrl", product.imageUrl)
            put("productProducer", product.productProducer)
            put("producerID", product.producerID)
        }
        db.update(
            BuenOrigenOpenHelper.TABLE_PRODUCT_REGISTRY,
            values,
            "id = ?",
            arrayOf(product.id)
        )
        db.close()
    }

    // DELETE del CRUD
    fun deleteProduct(product: Product) {
        val db = dbHelper.writableDatabase
        db.delete(
            BuenOrigenOpenHelper.TABLE_PRODUCT_REGISTRY,
            "id = ?",
            arrayOf(product.id)
        )
        db.close()
    }

    // Metodo para buscar producto por id
    fun getProductById(productId: String): Product? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            BuenOrigenOpenHelper.TABLE_PRODUCT_REGISTRY,
            null,
            "id = ?",
            arrayOf(productId),
            null, null, null
        )

        var product: Product? = null
        with(cursor) {
            if (moveToFirst()) {
                val id = getString(getColumnIndexOrThrow("id"))
                val name = getString(getColumnIndexOrThrow("name"))
                val description = getString(getColumnIndexOrThrow("description"))
                val price = getInt(getColumnIndexOrThrow("price"))
                val stock = getInt(getColumnIndexOrThrow("stock"))
                val category = getString(getColumnIndexOrThrow("category"))
                val imageUrl = getString(getColumnIndexOrThrow("imageUrl"))
                val productProducer = getString(getColumnIndexOrThrow("productProducer"))
                val producerID = getString(getColumnIndexOrThrow("producerID"))

                product = Product(
                    id = id,
                    productName = name,
                    description = description,
                    price = price,
                    stock = stock,
                    category = category,
                    imageUrl = imageUrl ?: "",
                    productProducer = productProducer ?: "",
                    producerID = producerID ?: ""
                )
            }
        }
        cursor.close()
        db.close()
        return product
    }

    // Metodo para buscar productos
    fun searchProducts(query: String): List<Product> {

        // Si la búsqueda está en blanco (ej: solo espacios), retornamos todos los productos
        if (query.isBlank()) return getAllProduct()

        val products = mutableListOf<Product>()
        val db = dbHelper.readableDatabase

        val selection = "name LIKE ? OR description LIKE ? OR category LIKE ? OR productProducer LIKE ?"
        val selectionArgs = arrayOf("%$query%", "%$query%", "%$query%", "%$query%")

        val cursor = db.query(
            BuenOrigenOpenHelper.TABLE_PRODUCT_REGISTRY,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getString(getColumnIndexOrThrow("id"))
                val name = getString(getColumnIndexOrThrow("name"))
                val description = getString(getColumnIndexOrThrow("description"))
                val price = getInt(getColumnIndexOrThrow("price"))
                val stock = getInt(getColumnIndexOrThrow("stock"))
                val category = getString(getColumnIndexOrThrow("category"))
                val imageUrl = getString(getColumnIndexOrThrow("imageUrl"))
                val productProducer = getString(getColumnIndexOrThrow("productProducer"))
                val producerID = getString(getColumnIndexOrThrow("producerID"))

                val product = Product(
                    id = id,
                    productName = name,
                    description = description,
                    price = price,
                    stock = stock,
                    category = category,
                    imageUrl = imageUrl ?: "",
                    productProducer = productProducer ?: "",
                    producerID = producerID ?: ""
                )
                products.add(product)
            }
        }
        cursor.close()
        db.close()
        return products
    }
}
