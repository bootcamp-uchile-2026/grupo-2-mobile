package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.uchile.dcc.mobile.ecotienda.R
import cl.uchile.dcc.mobile.ecotienda.data.DefaultData
import cl.uchile.dcc.mobile.ecotienda.data.database.Producer
import cl.uchile.dcc.mobile.ecotienda.data.database.Product
import cl.uchile.dcc.mobile.ecotienda.ui.component.CompromisosSection
import cl.uchile.dcc.mobile.ecotienda.ui.component.ProducerEmprendedorCard
import cl.uchile.dcc.mobile.ecotienda.ui.theme.CustomBrandTitleStyle
import cl.uchile.dcc.mobile.ecotienda.utils.formatAsCurrency
import cl.uchile.dcc.mobile.ecotienda.viewmodel.AuthViewModel
import coil3.compose.AsyncImage

@Composable
fun HomeEcoTienda(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    productos: List<Product>,
    producers: List<Producer> = DefaultData.Producer,
    onAgregarClick: (Product) -> Unit,
    onProductClick: (Product) -> Unit,
    onProducerClick: (Producer) -> Unit = {},
    onSeeMoreProductsClick: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    val ui by authViewModel.state.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Ofertas", "Compromiso", "Huella Verde", "Emprendedores")

    LaunchedEffect(ui.isLoggedIn, ui.isGuest) {
        if ((ui.isLoggedIn || ui.isGuest) && !ui.welcomeMessageShown) {
            authViewModel.markWelcomeShown()
            val message = if (ui.isLoggedIn) "Bienvenido ${ui.userEmail ?: ui.form.email}"
            else "Navegando como invitado"
            snackbarHostState.showSnackbar(message = message)
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Menu de pestañas superior en color verde
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color(0xFF1C3224),
            contentColor = Color.White,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                if (selectedTabIndex < tabPositions.size) {
                    SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = Color(0xFFD06A3B)
                    )
                }
            },
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 15.sp
                            ),
                            color = if (selectedTabIndex == index) Color(0xFFD06A3B) else Color.White
                        )
                    }
                )
            }
        }

        // Contenido de la pestaña activa
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when (selectedTabIndex) {
                0 -> OfertasTabContent(
                    productos = productos,
                    onProductClick = onProductClick,
                    onAgregarClick = onAgregarClick,
                    onSeeMoreProductsClick = onSeeMoreProductsClick
                )
                1 -> Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    CompromisosSection()
                }
                2 -> HuellaVerde()
                3 -> EmprendedoresTabContent(
                    producers = producers,
                    onProducerClick = onProducerClick
                )
            }
        }
    }
}

@Composable
private fun OfertasTabContent(
    productos: List<Product>,
    onProductClick: (Product) -> Unit,
    onAgregarClick: (Product) -> Unit,
    onSeeMoreProductsClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val pagerState = rememberPagerState(pageCount = { 3 })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Carrusel Banner Superior
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) { page ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .padding(horizontal = 6.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (page == 0) Color(0xFFA2B289) else if (page == 1) Color(0xFF8DAA91) else Color(0xFF7A9A83)
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_solo),
                            contentDescription = "Banner Oferta",
                            modifier = Modifier
                                .size(110.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                }
            }

            // Indicadores de puntos
            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (pagerState.currentPage == index) 8.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (pagerState.currentPage == index) Color(0xFF1C3224) else Color.Gray.copy(alpha = 0.5f)
                            )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sección: Populares en nuestra tienda
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Populares en nuestra tienda",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Ver más",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray,
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.clickable { onSeeMoreProductsClick() }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Fila horizontal de productos populares
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(productos) { product ->
                ProductCompactCard(
                    product = product,
                    onProductClick = { onProductClick(product) },
                    onAgregarClick = { onAgregarClick(product) }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Sección: ¡Productos perfectos para ti!
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "¡Productos perfectos para ti!",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Ver más",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray,
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.clickable { onSeeMoreProductsClick() }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Cuadrícula 2 columnas de productos perfectos para ti
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            val chunkedProducts = productos.chunked(2)
            chunkedProducts.forEach { rowProducts ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    rowProducts.forEach { product ->
                        ProductCompactCard(
                            product = product,
                            onProductClick = { onProductClick(product) },
                            onAgregarClick = { onAgregarClick(product) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowProducts.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun EmprendedoresTabContent(
    producers: List<Producer>,
    onProducerClick: (Producer) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Nuestros Productores",
            style = CustomBrandTitleStyle.copy(
                fontSize = 32.sp,
                textAlign = TextAlign.Center
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Conoce a quiénes están detrás de Buen Origen",
            style = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Center
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        producers.forEach { producer ->
            ProducerEmprendedorCard(
                producer = producer,
                onClick = { onProducerClick(producer) }
            )
        }
    }
}

@Composable
fun ProductCompactCard(
    product: Product,
    onProductClick: () -> Unit,
    onAgregarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(160.dp)
            .clickable { onProductClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.productName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                IconButton(
                    onClick = onAgregarClick,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .background(Color(0xFFD06A3B), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = "Agregar al carrito",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = product.productName,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    repeat(4) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color(0xFF1B2B1E),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                    Icon(
                        imageVector = Icons.Outlined.StarOutline,
                        contentDescription = null,
                        tint = Color(0xFF1B2B1E),
                        modifier = Modifier.size(12.dp)
                    )
                }

                Text(
                    text = product.price.formatAsCurrency(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
