package cl.uchile.dcc.mobile.ecotienda.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Search


// SearchStaticBar :: onClick modifier -> SeachrBar falsa
// Genera una searchbar falsa para poder carga la pantalla SearchScreen y buscar
//
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchStaticBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    bgColor: Color = MaterialTheme.colorScheme.primaryContainer,
) {
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = modifier
            .clickable(onClick = onSearch)
            .height(56.dp), // Altura estándar de la SearchBar
        shape = CircleShape, // Forma redondeada
        color = bgColor, // Color de fondo grisáceo
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                if (query.isEmpty()) {
                    Text(
                        text = "Buscar producto",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                    )
                } else {
                    Text(
                        text = query,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
            Icon(
                imageVector = FeatherIcons.Search,
                contentDescription = "Buscar",
                tint = Color.Black.copy(alpha = 0.8f),
            )
        }
    }
}
