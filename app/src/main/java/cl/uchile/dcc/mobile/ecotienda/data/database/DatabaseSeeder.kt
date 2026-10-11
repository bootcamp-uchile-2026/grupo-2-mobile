package cl.uchile.dcc.mobile.ecotienda.data.database

import android.content.Context
import cl.uchile.dcc.mobile.ecotienda.data.DefaultData

/* Carga datos iniciales en SQLite a partir de [DefaultData].
 * Solo inserta si las tablas de productos y productores están vacías.
 */
object DatabaseSeeder {

    fun seedIfEmpty(context: Context) {
        val appContext = context.applicationContext
        val productRepository = ProductRepository(appContext)
        val producerRepository = ProducerRepository(appContext)

        // Obtenemos id de productores que ya estan en SQlite
        val existingProducers = producerRepository.getAllProducers().map { it.id }.toSet()

        // Elegimos los productores de DefaultData cuyo ID no esté en la base de datos
        DefaultData.Producer.filter { it.id !in existingProducers }.forEach {
            producerRepository.addProducer(it)
        }

        // Mismo proceso para product
        val existingProducts = productRepository.getAllProduct().map { it.id }.toSet()
        DefaultData.Product.filter { it.id !in existingProducts }.forEach {
            productRepository.addProduct(it)
        }

//        if (producerRepository.getAllProducers().isEmpty()) {
//            DefaultData.Producer.forEach { producerRepository.addProducer(it) }
//        }
//        if (productRepository.getAllProduct().isEmpty()) {
//            DefaultData.Product.forEach { productRepository.addProduct(it) }
//        }
    }
}
