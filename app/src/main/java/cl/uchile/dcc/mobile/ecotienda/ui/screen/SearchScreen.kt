package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onSearch: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(true) } // Iniciamos activa para que cubra la pantalla

    // Esta caja asegura que la búsqueda use todo el espacio
    Box(Modifier
        .fillMaxSize()) {
        SearchBar(
            modifier = Modifier.align(Alignment.TopCenter),
            query = query,
            onQueryChange = { query = it },
            onSearch = {
                onSearch(it)
                active = false
            },
            active = active,
            onActiveChange = {
                active = it
                if (!it) onBack() // Si se desactiva, volvemos atrás
            },
            placeholder = { Text("Busca en EcoTienda...") },
            leadingIcon = {
                IconButton(onClick = onBack) {
                    Icon(FeatherIcons.ArrowLeft, contentDescription = "Atrás")
                }
            }
        ) {
            // Aquí van las sugerencias o resultados recientes
            Text("Sugerencias...", modifier = Modifier.padding(16.dp))
        }
    }
}