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
import cl.uchile.dcc.mobile.ecotienda.ui.theme.ecoTiendaColors
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
    bgColor: Color = MaterialTheme.ecoTiendaColors.cl4,
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
            Icon(
                imageVector = FeatherIcons.Search,
                contentDescription = null,
                tint = Color.Companion.Black
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Busca algo especial",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}
