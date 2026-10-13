package cl.uchile.dcc.mobile.ecotienda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import cl.uchile.dcc.mobile.ecotienda.data.DefaultData
import cl.uchile.dcc.mobile.ecotienda.data.database.BuenOrigenDatabase
import cl.uchile.dcc.mobile.ecotienda.data.database.Producer
import cl.uchile.dcc.mobile.ecotienda.data.repository.ProducerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProducerDetailViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val app = application as? cl.uchile.dcc.mobile.ecotienda.data.repository.EcoTiendaApp
    private val producerRepository = app?.container?.producerRepository ?: ProducerRepository(
        BuenOrigenDatabase.getInstance(application).producerDAO
    )

    private val savedProducerId: String? = savedStateHandle["producer_id"] ?: savedStateHandle["producerId"]

    private val _selectedProducer = MutableStateFlow<Producer?>(null)
    val selectedProducer: StateFlow<Producer?> = _selectedProducer

    init {
        savedProducerId?.let { id ->
            viewModelScope.launch {
                val producer = producerRepository.getProducerById(id) ?: DefaultData.Producer.find { it.id == id }
                _selectedProducer.value = producer
            }
        }
    }

    fun selectProducer(producer: Producer) {
        savedStateHandle["producer_id"] = producer.id
        _selectedProducer.value = producer
    }
}
