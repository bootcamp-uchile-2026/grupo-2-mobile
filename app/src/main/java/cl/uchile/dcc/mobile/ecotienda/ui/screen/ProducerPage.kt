package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.uchile.dcc.mobile.ecotienda.model.Producer

@Composable
fun ProducerPage(modifier: Modifier, producer: Producer, onBack: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)) {
        Text(text = producer.name, style = MaterialTheme.typography.headlineMedium)
        Text(text = "Ubicación: ${producer.location}")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = producer.history)

        Button(onClick = onBack, modifier = Modifier.padding(top = 20.dp)) {
            Text("Volver")
        }
    }
}