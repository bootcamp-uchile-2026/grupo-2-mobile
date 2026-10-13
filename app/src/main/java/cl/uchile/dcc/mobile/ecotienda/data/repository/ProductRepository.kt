package cl.uchile.dcc.mobile.ecotienda.data.repository

import cl.uchile.dcc.mobile.ecotienda.data.database.Product
import cl.uchile.dcc.mobile.ecotienda.data.database.ProductDAO

class ProductRepository(
    private val productDao: ProductDAO
) {
    suspend fun addProduct(product: Product) {
        productDao.addProduct(product)
    }

    suspend fun getAllProduct(): List<Product> {
        return productDao.getAllProducts()
    }

    suspend fun updateProduct(product: Product) {
        productDao.updateProducts(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    suspend fun getProductById(productId: String): Product? {
        return productDao.getProductById(productId)
    }

    suspend fun searchProducts(query: String): List<Product> {
        if (query.isBlank()) return getAllProduct()
        return productDao.searchProducts(query)
    }
}
