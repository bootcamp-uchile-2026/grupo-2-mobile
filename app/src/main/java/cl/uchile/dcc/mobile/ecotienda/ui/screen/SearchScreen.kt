package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.uchile.dcc.mobile.ecotienda.data.database.Product
import cl.uchile.dcc.mobile.ecotienda.ui.component.ProductCardSearch
import cl.uchile.dcc.mobile.ecotienda.viewmodel.SearchViewModel
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onSubmitSearch: (String) -> Unit,
    searchViewModel: SearchViewModel = viewModel(),
    onProductClick: (Product) -> Unit,
    onAddToCart: (Product) -> Unit = {}
) {
    val uiState by searchViewModel.uiState.collectAsState()
    var active by remember { mutableStateOf(true) }

    SearchBar(
        modifier = Modifier.fillMaxWidth(),
        query = uiState.query,
        onQueryChange = searchViewModel::onQueryChange,
        onSearch = { query ->
            active = false
            onSubmitSearch(query)
        },
        active = active,
        onActiveChange = { active = it },
        placeholder = { Text("Busca en Buen Origen...") },
        leadingIcon = {
            IconButton(onClick = onBack) {
                Icon(FeatherIcons.ArrowLeft, contentDescription = "Atrás")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            uiState.searchResults.take(5).forEach { product ->
                ProductCardSearch(
                    product = product,
                    onProductClick = { onProductClick(product) },
                    onAgregarClick = { onAddToCart(product) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun SearchResultsScreen(
    searchViewModel: SearchViewModel = viewModel(),
    onBack: () -> Unit,
    onProductClick: (Product) -> Unit,
    onAddToCart: (Product) -> Unit
) {
    val uiState by searchViewModel.uiState.collectAsState()

    SearchResultsContent(
        query = uiState.query,
        results = uiState.searchResults,
        isLoading = uiState.isLoading,
        onBack = onBack,
        onProductClick = onProductClick,
        onAddToCart = onAddToCart
    )
}

@Composable
fun SearchResultsContent(
    query: String,
    results: List<Product>,
    isLoading: Boolean,
    onBack: () -> Unit,
    onProductClick: (Product) -> Unit,
    onAddToCart: (Product) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(FeatherIcons.ArrowLeft, contentDescription = "Volver a búsqueda")
            }
            Text(
                text = "Resultados para \"$query\"",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = if (isLoading) "Buscando productos..." else "${results.size} productos encontrados",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 12.dp, bottom = 12.dp)
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (results.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No se encontraron productos que coincidan con tu búsqueda.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(results, key = { it.id }) { product ->
                    ProductCardSearch(
                        product = product,
                        onProductClick = { onProductClick(product) },
                        onAgregarClick = { onAddToCart(product) }
                    )
                }
            }
        }
    }
}
