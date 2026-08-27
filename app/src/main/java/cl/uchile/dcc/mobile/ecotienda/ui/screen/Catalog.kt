package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.CatalogScreenState
import cl.uchile.dcc.mobile.ecotienda.viewmodel.CatalogViewModel
import coil3.compose.AsyncImage
import cl.uchile.dcc.mobile.ecotienda.model.Product
import cl.uchile.dcc.mobile.ecotienda.ui.theme.ecoTiendaColors
import cl.uchile.dcc.mobile.ecotienda.ui.theme.md_theme_light_onSecondaryContainer
import cl.uchile.dcc.mobile.ecotienda.utils.formatAsCurrency


@Composable
fun Catalog(
    modifier: Modifier,
    catalogViewModel: CatalogViewModel = viewModel(),
    onProductClick: (Product) -> Unit, // Cambiado: ahora recibe Product
    onAgregarClick: (Product) -> Unit
) {
    val ui by catalogViewModel.state.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ui.categories) { category ->
                FilterChip(
                    selected = ui.form.selectedCategory == category,
                    onClick = { catalogViewModel.selectCategory(category) },
                    label = { Text(category) }
                )
            }
        }

        when (val catalog = ui.catalog) {
            CatalogScreenState.Loading -> {
                CircularProgressIndicator()
            }

            CatalogScreenState.Empty -> {
                Text("No hay productos")
            }

            is CatalogScreenState.Success -> {
                // Prueba para ver cuantos productos por categoria
//                Text("${catalog.products.size} productos")

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), // ESTO es lo que suele faltar
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(catalog.products, key = { it.id }) { product ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clickable { onProductClick(product) },
                        ) {
                            // Usamos Box para superponer elementos
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()) {
                                //  Imagen de fondo
                                AsyncImage(
                                    model = product.imageUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )

                                // Texto en Card() sobre imagen
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(12.dp),
                                    verticalArrangement = Arrangement.Bottom // Empuja todo hacia abajo
                                ) {
                                    Text(
                                        text = product.productName,
                                        color = Color.White , // Color blanco para que resalte
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = product.price.formatAsCurrency(),
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyLarge
                                    )

                                    FilledIconButton(
                                        onClick = { onAgregarClick(product) },
                                        modifier = Modifier
                                            .padding(top = 8.dp)
                                            .size(30.dp)
                                            .align(Alignment.End),
                                        colors = IconButtonDefaults.filledIconButtonColors(
                                            containerColor = MaterialTheme.ecoTiendaColors.cl1
                                        )
                                    )
                                        {
                                            Icon(Icons.Filled.ShoppingCart, contentDescription = "+Agregar")
                                    }
                                }
                            }

                        }


                    }
                }
            }
        }
    }
}