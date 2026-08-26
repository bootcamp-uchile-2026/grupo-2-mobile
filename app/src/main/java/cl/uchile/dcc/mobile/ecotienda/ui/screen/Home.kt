package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cl.uchile.dcc.mobile.ecotienda.R
import cl.uchile.dcc.mobile.ecotienda.model.Product
import cl.uchile.dcc.mobile.ecotienda.ui.component.HuellaVerdeSection
import cl.uchile.dcc.mobile.ecotienda.ui.component.ProductCard
import cl.uchile.dcc.mobile.ecotienda.ui.theme.Sage
import cl.uchile.dcc.mobile.ecotienda.ui.theme.md_theme_light_primary
import kotlinx.coroutines.launch


@Composable
fun HomeEcoTienda(
    modifier: Modifier,
    snackbarHostState: SnackbarHostState,
    productos: List<Product>,
    onAgregarClick: (Product) -> Unit,
    onProductClick: (Product) -> Unit,
) {
    if (productos.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { productos.size })
    val scope = rememberCoroutineScope()

    // Usamos un Column como base para que el carrusel no ocupe
    // necesariamente toda la pantalla vertical si no quieres.
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Imagen local",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center

        ) {
            Text(
                text = "Salvando el planeta una compra a la vez",
                color = md_theme_light_primary,
                style = MaterialTheme.typography.titleMedium,
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd // Alinea el contenido al centro-derecha

        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
            ) { page ->
                ProductCard(
                    product = productos[page],
                    onProductClick = { onProductClick(productos[page]) },
                    onAgregarClick = { onAgregarClick(productos[page]) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp) // un poquito de margen si quieres
                )
            }

            // Flecha derecha
            if (pagerState.currentPage < productos.lastIndex) {
                IconButton(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 12.dp)
                        .size(44.dp)
                        .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = "Siguiente Producto",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
        HuellaVerdeSection()

    }
}