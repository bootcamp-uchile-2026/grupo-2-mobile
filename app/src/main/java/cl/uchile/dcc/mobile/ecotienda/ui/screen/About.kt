package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.uchile.dcc.mobile.ecotienda.model.Producer
import cl.uchile.dcc.mobile.ecotienda.ui.component.CompromisosSection
import cl.uchile.dcc.mobile.ecotienda.ui.component.GeneralCard
import cl.uchile.dcc.mobile.ecotienda.ui.component.ProducerAvatarItem
import cl.uchile.dcc.mobile.ecotienda.ui.component.ProducerCard
import cl.uchile.dcc.mobile.ecotienda.ui.theme.ecoTiendaColors

// Pagina de acerca de nosotros, presentando los productores
@Composable
fun About(
    modifier: Modifier,
    producers: List<Producer>,
    textColor: Color = MaterialTheme.ecoTiendaColors.cl3,
    onProducerClick: (Producer) -> Unit
) {
    // Scroll vertical con Lazycolumn

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 24.dp) // Espacio al final
    ) {
        // 2. El título y la GeneralCard van en un solo 'item'
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Acerca de nosotros",
                    style = MaterialTheme.typography.titleLarge,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                )
                GeneralCard(imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%2Fid%2FOIP.JS_gyJYif5Zf-fXp7i6-7gHaEK%3Fr%3D0%26pid%3DApi&f=1&ipt=cff1b6ac60c329c0b6bbb2b72d810ccf69ea13a2887ea4ff7cd48c2195b1bf9a&ipo=images",
                    text1 = "Personas que comparten", text2 = "Valores ambientales")

                // Sección de Compromisos
                CompromisosSection()

                Spacer(modifier = Modifier.padding(16.dp))

                Text(
                    text = "NUESTROS EMPRENDEDORES",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(producers) { producer ->
                        ProducerAvatarItem(
                            producer = producer,
                            onClick = { onProducerClick(producer) },
                            modifier = Modifier.width(140.dp)
                        )
                    }
                }
            }
        }
    }
}
