package cl.uchile.dcc.mobile.ecotienda.ui.screen

import android.text.Layout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import cl.uchile.dcc.mobile.ecotienda.model.Producer
import cl.uchile.dcc.mobile.ecotienda.model.Product
import cl.uchile.dcc.mobile.ecotienda.ui.component.GeneralCard
import cl.uchile.dcc.mobile.ecotienda.ui.component.ProducerCard
import cl.uchile.dcc.mobile.ecotienda.ui.component.ProductCard
import cl.uchile.dcc.mobile.ecotienda.ui.theme.DarkBrown
import cl.uchile.dcc.mobile.ecotienda.ui.theme.SoftBeige
import kotlin.hashCode

// Pagina de acerca de nosotros, presentando los productores
@Composable
fun About(
    modifier: Modifier,
    producers: List<Producer>,
    textColor: Color = DarkBrown,
) {
    // 1. Eliminamos pagerState ya que no lo usaremos para scroll vertical

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
                GeneralCard(imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%2Fid%2FOIP.JS_gyJYif5Zf-fXp7i6-7gHaEK%3Fr%3D0%26pid%3DApi&f=1&ipt=cff1b6ac60c329c0b6bbb2b72d810ccf69ea13a2887ea4ff7cd48c2195b1bf9a&ipo=images")

                // Un pequeño espacio antes de la lista de productores
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }

        // 3. Los productores se listan hacia abajo automáticamente
        items(producers) { producer ->
            ProducerCard(
                producer = producer,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}