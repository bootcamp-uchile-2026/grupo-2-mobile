package cl.uchile.dcc.mobile.ecotienda.data.repository

import cl.uchile.dcc.mobile.ecotienda.data.database.Producer
import cl.uchile.dcc.mobile.ecotienda.data.database.ProducerDAO
import cl.uchile.dcc.mobile.ecotienda.data.network.ApiRepository
import cl.uchile.dcc.mobile.ecotienda.data.network.ProducerApiService
import cl.uchile.dcc.mobile.ecotienda.data.network.dto.toEntity

class ProducerRepository(
    private val producerDao: ProducerDAO,
    private val apiService: ProducerApiService = ApiRepository.producerApiService
) {
    suspend fun addProducer(producer: Producer) {
        producerDao.addProducer(producer)
    }

    suspend fun getAllProducers(): List<Producer> {
        return try {
            val entities = apiService.getProducers().map { it.toEntity() }
            for (p in entities) {
                producerDao.addProducer(p)
            }
            entities.ifEmpty { producerDao.getAllProducer() }
        } catch (_: Exception) {
            producerDao.getAllProducer()
        }
    }

    suspend fun getProducerById(producerId: String): Producer? {
        return try {
            val existing = producerDao.getProducerById(producerId)
            val fresh = apiService.getProducerById(producerId).toEntity()
            val entity = fresh.copy(
                location = fresh.location.ifBlank { existing?.location.orEmpty() },
                history = fresh.history.ifBlank { existing?.history.orEmpty() },
            )
            producerDao.addProducer(entity)
            entity
        } catch (_: Exception) {
            producerDao.getProducerById(producerId)
        }
    }

    suspend fun searchProducers(query: String): List<Producer> {
        val all = getAllProducers()
        if (query.isBlank()) return all
        return all.filter {
            it.name.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true) ||
                it.location.contains(query, ignoreCase = true)
        }
    }
}
