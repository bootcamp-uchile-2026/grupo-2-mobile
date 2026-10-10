package cl.uchile.dcc.mobile.ecotienda.data.database

import android.content.Context
import cl.uchile.dcc.mobile.ecotienda.data.DefaultData

/**
 * Carga datos iniciales en SQLite a partir de [DefaultData].
 * Solo inserta si las tablas de productos y productores están vacías.
 */
object DatabaseSeeder {

    fun seedIfEmpty(context: Context) {
        val appContext = context.applicationContext
        val productRepository = ProductRepository(appContext)
        val producerRepository = ProducerRepository(appContext)

        if (producerRepository.getAllProducers().isEmpty()) {
            DefaultData.Producer.forEach { producerRepository.addProducer(it) }
        }
        if (productRepository.getAllProduct().isEmpty()) {
            DefaultData.Product.forEach { productRepository.addProduct(it) }
        }
    }
}
