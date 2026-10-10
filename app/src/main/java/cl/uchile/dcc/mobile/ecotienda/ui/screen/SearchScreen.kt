package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.uchile.dcc.mobile.ecotienda.data.Product
import cl.uchile.dcc.mobile.ecotienda.ui.component.ProductCard
import cl.uchile.dcc.mobile.ecotienda.ui.component.ProductCardSearch
import cl.uchile.dcc.mobile.ecotienda.viewmodel.SearchViewModel
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft

// SearchScreen es la pantalla que se agranda al usar el buscardor
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    searchViewModel: SearchViewModel = viewModel(), // Instanciamos el ViewModel
    onProductClick: (Product) -> Unit // Para ir al detalle al hacer click en un resultado
) {
    val uiState by searchViewModel.uiState.collectAsState()
    var active by remember { mutableStateOf(true) }

    Box(Modifier.fillMaxSize()) {
        SearchBar(
            modifier = Modifier.align(Alignment.TopCenter),
            query = uiState.query,
            onQueryChange = { newQuery ->
                // Opcional: puedes buscar mientras escribe, o esperar a onSearch
                searchViewModel.searchProducts(newQuery)
            },
            onSearch = { queryText ->
                searchViewModel.searchProducts(queryText)
            },
            active = active,
            onActiveChange = {
                active = it
                if (!it) onBack()
            },
            placeholder = { Text("Busca en Buen Origen...") },
            leadingIcon = {
                IconButton(onClick = onBack) {
                    Icon(FeatherIcons.ArrowLeft, contentDescription = "Atrás")
                }
            }
        ) {
            // Aquí mostramos los resultados obtenidos de SQLite
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.searchResults) { product ->
                    // Puedes usar tu componente ProductCard o un ListItem sencillo
                    ProductCardSearch(
                        product = product,
                        onProductClick = { onProductClick(product) },
                        onAgregarClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp) // un poquito de margen si quieres
                    )
//                    ListItem(
//                        headlineContent = { Text(product.productName) },
//                        supportingContent = { Text(product.description) },
//                        trailingContent = { Text("$${product.price}") },
//                        modifier = Modifier.clickable {
//                            onProductClick(product)
//                        }
//                    )
                }
            }
        }
    }
}