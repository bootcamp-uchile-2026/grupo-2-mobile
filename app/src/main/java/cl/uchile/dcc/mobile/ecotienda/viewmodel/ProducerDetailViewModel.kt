package cl.uchile.dcc.mobile.ecotienda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import cl.uchile.dcc.mobile.ecotienda.data.DefaultData
import cl.uchile.dcc.mobile.ecotienda.data.Producer
import cl.uchile.dcc.mobile.ecotienda.data.database.ProducerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProducerDetailViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val producerRepository = ProducerRepository(application)

    private val savedProducerId: String? = savedStateHandle["producer_id"] ?: savedStateHandle["producerId"]
    private val restoredProducer: Producer? = savedProducerId?.let { id ->
        producerRepository.getProducerById(id) ?: DefaultData.Producer.find { it.id == id }
    }

    // Seleccion de producer en viewModel
    private val _selectedProducer = MutableStateFlow<Producer?>(restoredProducer)

    val selectedProducer: StateFlow<Producer?> = _selectedProducer

    // Selección de productor
    fun selectProducer(producer: Producer) {
        savedStateHandle["producer_id"] = producer.id
        _selectedProducer.value = producer
    }
}
