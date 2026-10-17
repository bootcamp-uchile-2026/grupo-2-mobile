package cl.uchile.dcc.mobile.ecotienda.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Search

// SearchStaticBar :: onClick modifier -> SeachrBar falsa
// Genera una searchbar falsa para poder carga la pantalla SearchScreen y buscar
//
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchStaticBar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    bgColor: Color = MaterialTheme.colorScheme.primaryContainer,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(56.dp), // Altura estándar de la SearchBar
        shape = CircleShape, // Forma redondeada
        color = bgColor, // Color de fondo grisáceo
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Buscar producto",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
            )
            Spacer(Modifier.weight(1f))

            Icon(
                modifier = Modifier,
                imageVector = FeatherIcons.Search,
                contentDescription = null,
                tint = Color.Black.copy(alpha = 0.8f),
            )
        }
    }
}
