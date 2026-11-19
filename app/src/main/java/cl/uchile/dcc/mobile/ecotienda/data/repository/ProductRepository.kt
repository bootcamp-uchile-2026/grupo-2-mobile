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
            val entities = apiService.getProducts().content.map { it.toEntity() }
            for (p in entities) {
                productDao.addProduct(p)
            }
            entities.ifEmpty { productDao.getAllProducts() }
        } catch (_: Exception) {
            productDao.getAllProducts()
        }
    }

    suspend fun updateProduct(product: Product) {
        productDao.updateProducts(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    suspend fun getProductById(productId: String): Product? {
        return try {
            val existing = productDao.getProductById(productId)
            val fresh = apiService.getProductById(productId).toEntity()
            val entity = fresh.copy(
                productProducer = fresh.productProducer.ifBlank { existing?.productProducer.orEmpty() },
                category = fresh.category.ifBlank { existing?.category.orEmpty() },
                producerID = fresh.producerID.ifBlank { existing?.producerID.orEmpty() },
                imageUrl = fresh.imageUrl.ifBlank { existing?.imageUrl.orEmpty() },
            )
            productDao.addProduct(entity)
            entity
        } catch (_: Exception) {
            productDao.getProductById(productId)
        }
    }

    suspend fun searchProducts(query: String): List<Product> {
        if (query.isBlank()) return emptyList()
        return try {
            val entities = apiService.searchProducts(query).content.map { it.toEntity() }
            for (p in entities) {
                productDao.addProduct(p)
            }
            entities
        } catch (_: Exception) {
            emptyList()
        }
    }
}
