package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cl.uchile.dcc.mobile.ecotienda.model.Product
import coil3.compose.AsyncImage

@Composable
fun ProductPage(
    product: Product,
    onAddToCart: (Product, Int) -> Unit,
    onBack: () -> Unit,
) {
    Column(
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AsyncImage(
            model = product.imageUrl,
            contentDescription = product.productName,
            modifier = Modifier
            contentScale = ContentScale.Crop
        )


        Text(
        )
        Text(
            text = "$${product.price}",
        )

        Text(
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ) {
            }


            ) {
            }
        }
    }
}
