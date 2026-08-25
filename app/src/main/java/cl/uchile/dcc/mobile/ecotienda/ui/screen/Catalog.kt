package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.CatalogScreenState
import cl.uchile.dcc.mobile.ecotienda.viewmodel.CatalogViewModel

@Composable
fun Catalog(
    modifier: Modifier,
    catalogViewModel: CatalogViewModel = viewModel()
) {
    val ui by catalogViewModel.state.collectAsState()
    Column(
        modifier = modifier
    ) {
    LazyRow {
        items(ui.categories) { category ->
            FilterChip(
                selected = ui.form.selectedCategory == category,
                onClick = { catalogViewModel.selectCategory(category) },
                label = { Text(category) }
            )
        }
    }

    when (val catalog = ui.catalog) {
        is CatalogScreenState.Loading -> CircularProgressIndicator()
        is CatalogScreenState.Empty -> Text("No hay productos")
        is CatalogScreenState.Success -> {
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(catalog.products, key = { it.id }) { product ->
                    // card con product.productName, product.price, product.imageUrl
                }
            }
        }
    }
    }
}