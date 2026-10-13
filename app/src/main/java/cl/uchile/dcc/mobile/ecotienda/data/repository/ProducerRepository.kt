package cl.uchile.dcc.mobile.ecotienda.data.repository

import cl.uchile.dcc.mobile.ecotienda.data.database.Producer
import cl.uchile.dcc.mobile.ecotienda.data.database.ProducerDAO

class ProducerRepository(
    private val producerDao: ProducerDAO
) {
    suspend fun addProducer(producer: Producer) {
        producerDao.addProducer(producer)
    }

    suspend fun getAllProducers(): List<Producer> {
        return producerDao.getAllProducer()
    }

    suspend fun getProducerById(producerId: String): Producer? {
        return producerDao.getProducerById(producerId)
    }
}
