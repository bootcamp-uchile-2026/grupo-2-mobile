package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.uchile.dcc.mobile.ecotienda.model.Product
import cl.uchile.dcc.mobile.ecotienda.ui.theme.Sage
import cl.uchile.dcc.mobile.ecotienda.ui.theme.Mint
import coil3.compose.AsyncImage

// Página de detalle de productos
// Navegación: Home -> Catalogo -> Producto
// Backsatck agregado
@Composable
fun ProductPage(
    product: Product,
    onAddToCart: (Product, Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var quantity by remember { mutableIntStateOf(1) }
    var selectedTab by remember { mutableIntStateOf(2) }

    val tabs = listOf("Materiales", "Elaboración", "Valoraciones")

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
                text = "Detalle de Producto",
                style = MaterialTheme.typography.titleMedium,
                color = Sage
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Galería de imagenes
        Row(modifier = Modifier.fillMaxWidth().height(300.dp)) {
            // Thumbnail falso
            Column(
                modifier = Modifier
                    .width(60.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(4) { index ->
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .border(1.dp, if (index == 0) Sage else Color.LightGray, RoundedCornerShape(4.dp))
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFF0F0F0))
                    ) {
                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            alpha = if (index == 0) 1f else 0.4f
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Imagen principal
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.productName,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF8F8F8)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Info del producto
        Text(
            text = product.productName.uppercase(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "$${product.price}",
            style = MaterialTheme.typography.headlineSmall,
            color = Sage,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Product Description
        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            lineHeight = 20.dp.value.sp // Use proper line height if needed
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Opciones de producto falsa
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            Column(modifier = Modifier.weight(1f)) {
//                Text(text = "COLOR", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
//                FakeDropdown(text = "Selecciona una opción")
//            }
//            Column(modifier = Modifier.weight(1f)) {
//                Text(text = "TALLA", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
//                FakeDropdown(text = "Selecciona una opción")
//            }
//        }

        Spacer(modifier = Modifier.height(20.dp))

        // Selección de cantidad y añadir al carrito
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
                    .padding(horizontal = 4.dp)
            ) {
                IconButton(onClick = { if (quantity > 1) quantity-- }, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(20.dp))
                }
                Text(
                    text = quantity.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                IconButton(onClick = { quantity++ }, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { onAddToCart(product, quantity) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Sage)
            ) {
                Text("Agregar al carrito", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Idea de medidor de Huella Verde
        Text(
            text = "HUELLA VERDE",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Mint)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f) // Static sustainability score
                    .fillMaxHeight()
                    .background(Sage)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Navegación
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

        // Contenido
        when (selectedTab) {
            2 -> RatingSection()
            else -> {
                Text(
                    text = "Más detalles sobre ${tabs[selectedTab]} próximamente.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
    }
}

@Composable
fun FakeDropdown(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
            .clickable { /* No-op */ }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
fun RatingSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 24.dp)
        ) {
            Text(text = "4.5", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = Sage)
            Row {
                repeat(4) { Icon(Icons.Default.Star, contentDescription = null, tint = Sage, modifier = Modifier.size(18.dp)) }
                Icon(Icons.Outlined.StarOutline, contentDescription = null, tint = Sage, modifier = Modifier.size(18.dp))
            }
            Text(text = "Calificación sustentable", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }

        Column(modifier = Modifier.weight(1f)) {
            ReviewItem()
            Spacer(modifier = Modifier.height(20.dp))
            ReviewItem()
        }
    }
}

@Composable
fun ReviewItem() {
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
            // "Redacted" Review Text Bars
            Box(modifier = Modifier.fillMaxWidth().height(8.dp).background(Color(0xFFE0E0E0)))
            Spacer(modifier = Modifier.height(4.dp))
            Box(modifier = Modifier.fillMaxWidth(0.8f).height(8.dp).background(Color(0xFFE0E0E0)))
            Spacer(modifier = Modifier.height(4.dp))
            Box(modifier = Modifier.fillMaxWidth(0.9f).height(8.dp).background(Color(0xFFE0E0E0)))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Row {
            repeat(4) { Icon(Icons.Default.Star, contentDescription = null, tint = Sage, modifier = Modifier.size(12.dp)) }
        }
    }
}
