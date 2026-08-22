package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.uchile.dcc.mobile.ecotienda.model.Product
import cl.uchile.dcc.mobile.ecotienda.ui.component.ProductCard

@Composable
fun HomeEcoTienda (
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    productos: List<Product>,
    onAgregarClick: (Product) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(productos) { producto ->
            ProductCard(
                product = producto,
                onAgregarClick = { onAgregarClick(producto)}
            )
        }

    }

}
