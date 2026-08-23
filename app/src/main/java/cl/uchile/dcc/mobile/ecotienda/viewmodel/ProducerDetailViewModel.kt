package cl.uchile.dcc.mobile.ecotienda.viewmodel

import androidx.lifecycle.ViewModel
import cl.uchile.dcc.mobile.ecotienda.model.Producer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProducerDetailViewModel: ViewModel() {
    // Seleccion de producer en viewModel
    private val _selectedProducer = MutableStateFlow<Producer?>(null)

    val selectedProducer: StateFlow<Producer?> = _selectedProducer

    // Selección de productor
    fun selectProducer(producer: Producer) {
        _selectedProducer.value = producer
    }
}