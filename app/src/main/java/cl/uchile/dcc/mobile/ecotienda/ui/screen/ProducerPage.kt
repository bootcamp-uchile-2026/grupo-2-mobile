package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.uchile.dcc.mobile.ecotienda.model.Producer
import cl.uchile.dcc.mobile.ecotienda.ui.theme.Sage
import coil3.compose.AsyncImage

// Página de detalle de emprendedor (cambio de nombre)
// Navegación: Home -> Nosotros -> Emprendedor
// Backsatck agregado
@Composable
fun ProducerPage(
    producer: Producer,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Scroll vertical
    val scrollState = rememberScrollState()

    // Cambio de vista para el empendedor
    var selectedTab by remember { mutableIntStateOf(0) } // Historia by default

    val tabs = listOf("Historia", "Metodología", "Valoraciones")

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Navegación
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Sage)
            }
            Text(
                text = "Perfil del Emprendedor",
                style = MaterialTheme.typography.titleMedium,
                color = Sage
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Imagen tentativa de emprendedor
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF0F0F0))
        ) {
            AsyncImage(
                model = producer.imageUrl,
                contentDescription = producer.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Lugar de donde proviene el emprendedor
            Surface(
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.BottomStart),
                color = Sage.copy(alpha = 0.9f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = producer.location,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Información de nuestro emprendedor
        Text(
            text = producer.name.uppercase(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = producer.description,
            style = MaterialTheme.typography.headlineSmall,
            color = Sage,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Sección tentatilla de Huella Verde de emprendedor
//        Text(
//            text = "HUELLA VERDE DEL PRODUCTOR",
//            style = MaterialTheme.typography.titleMedium,
//            fontWeight = FontWeight.Bold
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(24.dp)
//                .clip(RoundedCornerShape(4.dp))
//                .background(Mint)
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth(0.85f) // Static sustainability score for producers
//                    .fillMaxHeight()
//                    .background(Sage)
//            )
//        }

        Spacer(modifier = Modifier.height(40.dp))

        // Navegación por tab
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = Sage,
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) Sage else Color.Gray
                        )
                    }
                )
            }
        }

        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

        Spacer(modifier = Modifier.height(24.dp))

        // Contenido de loas tab
        when (selectedTab) {
            0 -> { // Historia
                Text(
                    text = producer.history,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 24.sp
                )
            }
            2 -> { // Valoraciones?
                ProducerRatingSection()
            }
            else -> {
                Text(
                    text = "Información sobre ${tabs[selectedTab]} próximamente.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ProducerRatingSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 24.dp)
        ) {
            Text(text = "4.8", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = Sage)
            Row {
                repeat(5) { Icon(Icons.Default.Star, contentDescription = null, tint = Sage, modifier = Modifier.size(18.dp)) }
            }
            Text(text = "Confianza del productor", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }

        Column(modifier = Modifier.weight(1f)) {
            ProducerReviewItem()
            Spacer(modifier = Modifier.height(20.dp))
            ProducerReviewItem()
        }
    }
}

@Composable
fun ProducerReviewItem() {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(24.dp), tint = Color.Gray)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.fillMaxWidth().height(8.dp).background(Color(0xFFE0E0E0)))
            Spacer(modifier = Modifier.height(4.dp))
            Box(modifier = Modifier.fillMaxWidth(0.85f).height(8.dp).background(Color(0xFFE0E0E0)))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Row {
            repeat(5) { Icon(Icons.Default.Star, contentDescription = null, tint = Sage, modifier = Modifier.size(12.dp)) }
        }
    }
}
