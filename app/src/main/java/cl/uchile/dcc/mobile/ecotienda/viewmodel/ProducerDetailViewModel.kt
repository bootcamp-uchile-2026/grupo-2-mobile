package cl.uchile.dcc.mobile.ecotienda.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import cl.uchile.dcc.mobile.ecotienda.data.DefaultData
import cl.uchile.dcc.mobile.ecotienda.data.Producer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProducerDetailViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val savedProducerId: String? = savedStateHandle["producer_id"] ?: savedStateHandle["producerId"]
    private val restoredProducer: Producer? = savedProducerId?.let { id ->
        DefaultData.Producer.find { it.id == id }
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