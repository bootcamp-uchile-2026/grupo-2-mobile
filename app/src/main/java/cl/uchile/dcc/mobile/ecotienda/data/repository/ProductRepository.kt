package cl.uchile.dcc.mobile.ecotienda.data.repository

import cl.uchile.dcc.mobile.ecotienda.data.database.Product
import cl.uchile.dcc.mobile.ecotienda.data.database.ProductDAO
import cl.uchile.dcc.mobile.ecotienda.data.network.ApiRepository
import cl.uchile.dcc.mobile.ecotienda.data.network.ProductApiService
import cl.uchile.dcc.mobile.ecotienda.data.network.dto.toEntity

class ProductRepository(
    private val productDao: ProductDAO,
    private val apiService: ProductApiService = ApiRepository.productApiService
) {
    suspend fun addProduct(product: Product) {
        productDao.addProduct(product)
    }

    suspend fun getAllProduct(): List<Product> {
        return try {
            val remoteDtos = apiService.getProducts()
            val entities = remoteDtos.map { it.toEntity() }
            for (p in entities) {
                productDao.addProduct(p)
            }
            entities.ifEmpty { productDao.getAllProducts() }
        } catch (e: Exception) {
            productDao.getAllProducts()
        }
    }

    suspend fun updateProduct(product: Product) {
        productDao.updateProducts(product)
    }

    suspend fun deleteProduct(product: Product) {
        try {
            apiService.deleteProduct(product.id)
        } catch (_: Exception) { }
        productDao.deleteProduct(product)
    }

    suspend fun getProductById(productId: String): Product? {
        return try {
            val dto = apiService.getProductById(productId)
            val entity = dto.toEntity()
            productDao.addProduct(entity)
            entity
        } catch (e: Exception) {
            productDao.getProductById(productId)
        }
    }

    suspend fun searchProducts(query: String): List<Product> {
        if (query.isBlank()) return getAllProduct()
        return try {
            val remoteDtos = apiService.searchProducts(query)
            val entities = remoteDtos.map { it.toEntity() }
            for (p in entities) {
                productDao.addProduct(p)
            }
            entities.ifEmpty { productDao.searchProducts(query) }
        } catch (e: Exception) {
            productDao.searchProducts(query)
        }
    }
}
