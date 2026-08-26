package cl.uchile.dcc.mobile.ecotienda.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.uchile.dcc.mobile.ecotienda.model.Producer
import cl.uchile.dcc.mobile.ecotienda.ui.theme.ecoTiendaColors
import compose.icons.FeatherIcons
import compose.icons.feathericons.Map

@Composable
fun ProducerCard(
    modifier: Modifier,
    producer: Producer,
    containerColor: Color = MaterialTheme.ecoTiendaColors.cl4,
    textColor: Color = MaterialTheme.ecoTiendaColors.cl3,
    onBack: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding( 8.dp),
        // Borde de card de color
        /* border = BorderStroke(1.dp, textColor.copy(alpha = 0.2f)), */
        // Borde degradado
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = containerColor // Así se aplica el color en M3
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically, // Centra el icono verticalmente respecto al texto
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = FeatherIcons.Map,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier
                    .padding(16.dp),
            )
            Column() {
            Text(
                text = producer.name,          // Nombre, cambiar description a nombre
                style = MaterialTheme.typography.titleMedium,
                color = textColor,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = producer.location,       // Donde esta
                style = MaterialTheme.typography.bodyMedium,
                color = textColor.copy(alpha = 0.9f)
            )
            Text(
                text = "Productos: 9",       // Cantidad de productos
                style = MaterialTheme.typography.bodySmall,
                color = textColor.copy(alpha = 0.9f)
            )
        }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd // Alinea el contenido al centro-derecha
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                        .size(36.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = "Ficha del Productor",
                        tint = textColor,
                        modifier = Modifier.size(28.dp)
                    )
                }
        }
        }
    }


}