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
            val dtos = apiService.getProducers()
            val entities = dtos.map { it.toEntity() }
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
            val dto = apiService.getProducerById(producerId)
            val entity = dto.toEntity()
            producerDao.addProducer(entity)
            entity
        } catch (_: Exception) {
            producerDao.getProducerById(producerId)
        }
    }

    suspend fun searchProducers(query: String): List<Producer> {
        return try {
            val dtos = apiService.searchProducers(query)
            val entities = dtos.map { it.toEntity() }
            for (p in entities) {
                producerDao.addProducer(p)
            }
            entities
        } catch (_: Exception) {
            producerDao.getAllProducer().filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.location.contains(query, ignoreCase = true)
            }
        }
    }
}
