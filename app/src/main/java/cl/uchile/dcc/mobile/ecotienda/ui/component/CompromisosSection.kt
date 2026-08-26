package cl.uchile.dcc.mobile.ecotienda.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.uchile.dcc.mobile.ecotienda.ui.theme.Mint
import cl.uchile.dcc.mobile.ecotienda.ui.theme.Sage

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CompromisosSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "NUESTROS COMPROMISOS",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Sage,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            maxItemsInEachRow = 2
        ) {
            CompromisoItem(
                icon = Icons.Default.Recycling,
                title = "Cero Residuos",
                description = "Productos reutilizables, sostenibles y respetuosos con el medioambiente.",
                modifier = Modifier.fillMaxWidth(0.5f).padding(8.dp)
            )
            CompromisoItem(
                icon = Icons.Default.Pets, // Representando animales/cruelty free
                title = "Libres de Tóxicos",
                description = "Con ingredientes que no contaminan el planeta ni tu cuerpo.",
                modifier = Modifier.fillMaxWidth(0.5f).padding(8.dp)
            )
            CompromisoItem(
                icon = Icons.Default.Eco,
                title = "Cruelty Free",
                description = "Certificación PETA Vegan & Cruelty Free para EcoTienda.",
                modifier = Modifier.fillMaxWidth(0.5f).padding(8.dp)
            )
            CompromisoItem(
                icon = Icons.Default.Storefront,
                title = "Marcas Locales",
                description = "Trabajamos con emprendedores y marcas locales que nos inspiran.",
                modifier = Modifier.fillMaxWidth(0.5f).padding(8.dp)
            )
        }
    }
}

@Composable
fun CompromisoItem(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Mint, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Sage,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Sage,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = Sage,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )
    }
}
