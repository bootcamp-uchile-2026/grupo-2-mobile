package cl.uchile.dcc.mobile.ecotienda.data.database

import androidx.room.Insert
import androidx.room.Query

class BuenOrigenDAO {
    // CREATE
    @Insert
    fun addProduct(product: Product): Long {
        return TODO("Provide the return value")
    }

    //READ
    @Query("SELECT * FROM products")
    fun getAllProducts(): List<Product> {
        return TODO("Provide the return value")
    }

    // UPDATE
    fun updateProducts(product: Product) {

    }
    
    // DELETE
    fun deleteProduct(product: Product) {

    }
}