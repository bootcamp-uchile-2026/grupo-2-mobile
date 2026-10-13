package cl.uchile.dcc.mobile.ecotienda.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(product: Product): Long

    @Query("SELECT * FROM product_registry")
    suspend fun getAllProducts(): List<Product>

    @Query("SELECT * FROM product_registry WHERE id = :id LIMIT 1")
    suspend fun getProductById(id: String): Product?

    @Query("SELECT * FROM product_registry WHERE product_name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' OR product_producer LIKE '%' || :query || '%'")
    suspend fun searchProducts(query: String): List<Product>

    @Update
    suspend fun updateProducts(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)
}
