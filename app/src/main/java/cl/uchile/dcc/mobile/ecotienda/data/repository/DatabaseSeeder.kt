package cl.uchile.dcc.mobile.ecotienda.data.repository

import android.content.Context
import cl.uchile.dcc.mobile.ecotienda.data.DefaultData
import cl.uchile.dcc.mobile.ecotienda.data.database.BuenOrigenDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseSeeder {

    fun seedIfEmpty(context: Context) {
        val appContext = context.applicationContext
        val db = BuenOrigenDatabase.getInstance(appContext)

        CoroutineScope(Dispatchers.IO).launch {
            val producerDao = db.producerDAO
            val productDao = db.productDAO

            val existingProducers = producerDao.getAllProducer().map { it.id }.toSet()
            DefaultData.Producer.filter { it.id !in existingProducers }.forEach {
                producerDao.addProducer(it)
            }

            val existingProducts = productDao.getAllProducts().map { it.id }.toSet()
            DefaultData.Product.filter { it.id !in existingProducts }.forEach {
                productDao.addProduct(it)
            }
        }
    }
}
