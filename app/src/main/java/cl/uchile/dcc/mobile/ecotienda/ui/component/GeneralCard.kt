package cl.uchile.dcc.mobile.ecotienda.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.uchile.dcc.mobile.ecotienda.ui.theme.SoftBeige
import coil3.compose.AsyncImage

@Composable
fun GeneralCard(
    modifier: Modifier = Modifier,
    containerColor: Color = SoftBeige,
    imageUrl: String,
    text1: String,
    text2: String
) {
    Card(
        modifier = Modifier
            .height(170.dp)
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Box {
            // Imagen de fondo
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround,
                    modifier = modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth(1f)
                ) {
                    Text(
                        text = text1,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = text2,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
