package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import cl.uchile.dcc.mobile.ecotienda.data.model.ArticleSummary
import cl.uchile.dcc.mobile.ecotienda.ui.component.BlogPreviewSection
import cl.uchile.dcc.mobile.ecotienda.ui.component.ProducerEmprendedorCard
import cl.uchile.dcc.mobile.ecotienda.ui.theme.CustomBrandTitleStyle
import cl.uchile.dcc.mobile.ecotienda.utils.formatAsCurrency
import cl.uchile.dcc.mobile.ecotienda.viewmodel.AuthViewModel
import cl.uchile.dcc.mobile.ecotienda.viewmodel.SearchViewModel
import coil3.compose.AsyncImage

private data class BannerSlide(
    val title: String,
    val subtitle: String,
    val buttonText: String,
    val backgroundColor: Color,
)

data class CategoryGridItem(
    val name: String,
    val imageRes: Int,
    val searchKeyword: String
)

private val categoryGridItems = listOf(
    CategoryGridItem("ALIMENTOS", R.drawable.alimentos, "Alimentos"),
    CategoryGridItem("AROMAS Y BIENESTAR", R.drawable.aromasybienestar, "Aromas"),
    CategoryGridItem("ARTESANÍAS", R.drawable.decoracionyarte, "Artesanía"),
    CategoryGridItem("CUIDADO PERSONAL", R.drawable.cuidadopersonal, "Cuidado Personal"),
    CategoryGridItem("HUERTO", R.drawable.huertoencasa, "Huerto"),
    CategoryGridItem("LIMPIEZA DEL HOGAR", R.drawable.limpiezadehogar, "Limpieza"),
    CategoryGridItem("MASCOTAS", R.drawable.mascotas, "Mascotas"),
    CategoryGridItem("ROPA Y ACCESORIOS", R.drawable.ropayaccesorios, "Ropa")
)

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
    articles: List<ArticleSummary> = emptyList(),
    articlesLoading: Boolean = false,
    articlesError: String? = null,
    onOpenBlog: () -> Unit = {},
    onOpenArticle: (Int) -> Unit = {},
    authViewModel: AuthViewModel = viewModel(),
    searchViewModel: SearchViewModel = viewModel()
) {
    val ui by authViewModel.state.collectAsState()
    val searchUiState by searchViewModel.uiState.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedCategoryName by remember { mutableStateOf<String?>(null) }

    val tabs = listOf("Para ti", "Categorías", "Emprendedores", "Huella Verde", "Blog")

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
        // Si hay una búsqueda de texto activa o filtros aplicados, mostramos directamente los resultados sin cambiar de página (Imagen 4)
        val isSearching = searchUiState.query.isNotBlank() || searchUiState.filter.isActive

        if (isSearching) {
            SearchResultsInline(
                query = searchUiState.query,
                results = searchUiState.searchResults,
                isLoading = searchUiState.isLoading,
                onClearSearch = { searchViewModel.clearSearch() }, // Borrar resultados o fiiltros para volver a HOME
                onProductClick = onProductClick,
                onAgregarClick = onAgregarClick
            )
        } else {
            // Menú de chips horizontales redondeados estilo mockup
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(tabs) { index, title ->
                    val isSelected = selectedTabIndex == index
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .clickable {
                                selectedTabIndex = index
                                selectedCategoryName = null
                            },
                        shape = RoundedCornerShape(24.dp),
                        color = if (isSelected) Color(0xFF1C3224) else Color.Transparent,
                        border = if (isSelected) null else BorderStroke(1.5.dp, Color(0xFF1C3224))
                    ) {
                        Text(
                            text = title,
                            color = if (isSelected) Color.White else Color(0xFF1C3224),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(horizontal = 22.dp, vertical = 9.dp)
                        )
                    }
                }
            }

            // Contenido de la pestaña activa
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when (selectedTabIndex) {
                    0 -> ParaTiTabContent(
                        productos = productos,
                        onProductClick = onProductClick,
                        onAgregarClick = onAgregarClick,
                        onSeeMoreProductsClick = {
                            selectedTabIndex = 1          // Cambia a la pestaña "Categorías"
                            selectedCategoryName = null   // Muestra las 8 fotos de categorías
                        }
                    )

                    1 -> CategoriasTabContent(
                        productos = productos,
                        selectedCategory = selectedCategoryName,
                        onSelectCategory = { cat -> selectedCategoryName = cat },
                        onBackToCategories = { selectedCategoryName = null },
                        onProductClick = onProductClick,
                        onAgregarClick = onAgregarClick
                    )

                    2 -> EmprendedoresTabContent(
                        producers = producers,
                        onProducerClick = onProducerClick
                    )

                    3 -> HuellaVerde()
                    4 -> BlogPreviewSection(
                        articles = articles,
                        isLoading = articlesLoading,
                        error = articlesError,
                        onOpenBlog = onOpenBlog,
                        onOpenArticle = onOpenArticle,
                    )
                }
            }
        }
    }
}

// Resultados de búsqueda en vivo en la misma página (Imagen 4)
@Composable
private fun SearchResultsInline(
    query: String,
    results: List<Product>,
    isLoading: Boolean,
    onClearSearch: () -> Unit, // Limpieza
    onProductClick: (Product) -> Unit,
    onAgregarClick: (Product) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 24.dp)
    ) {
        val titleText =
            if (query.isNotBlank()) "Resultados para “$query”" else "Resultados de filtros"
        Text(
            text = titleText,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        )

        // Botón que resetea la búsqueda y los filtros
        TextButton(onClick = onClearSearch) {
            Text(
                text = "Volver al inicio ✕",
                color = Color(0xFF8E412E),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }


    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else if (results.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No se encontraron productos que coincidan con tu búsqueda.",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                fontSize = 15.sp
            )
        }
    } else {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            val chunkedProducts = results.chunked(2)
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
}

// Vista de Categorías con imágenes en cuadrícula (Imagen 1)
@Composable
private fun CategoriasTabContent(
    productos: List<Product>,
    selectedCategory: String?,
    onSelectCategory: (String) -> Unit,
    onBackToCategories: () -> Unit,
    onProductClick: (Product) -> Unit,
    onAgregarClick: (Product) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 24.dp)
    ) {
        if (selectedCategory == null) {
            // Cuadrícula 2 columnas de las 8 categorías principales con foto según mockup (Imagen 1)
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                val chunkedCategories = categoryGridItems.chunked(2)
                chunkedCategories.forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        rowItems.forEach { item ->
                            CategoryPhotoCard(
                                item = item,
                                onClick = { onSelectCategory(item.searchKeyword) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        } else {
            // Cuando se presiona una categoría, se muestran sus productos en la misma pantalla
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackToCategories) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Categoría: $selectedCategory",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            val categoryProducts = remember(productos, selectedCategory) {
                val cat = selectedCategory.lowercase()
                productos.filter {
                    it.category.lowercase().contains(cat) ||
                            (cat.contains("ropa") && (it.category.lowercase()
                                .contains("ropa") || it.category.lowercase().contains("moda"))) ||
                            (cat.contains("artesan") && (it.category.lowercase()
                                .contains("artesan") || it.category.lowercase()
                                .contains("decoraci"))) ||
                            (cat.contains("aroma") && it.category.lowercase().contains("aroma"))
                }
            }

            if (categoryProducts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay productos disponibles en esta categoría.",
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            } else {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    val chunked = categoryProducts.chunked(2)
                    chunked.forEach { rowProducts ->
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
    }
}

// Tarjeta de Categoría con foto y texto blanco superpuesto (Imagen 1)
@Composable
private fun CategoryPhotoCard(
    item: CategoryGridItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(180.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Degradado inferior oscuro para legibilidad perfecta del texto
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f)),
                            startY = 60f
                        )
                    )
            )
            Text(
                text = item.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 14.dp)
            )
        }
    }
}

@Composable
private fun ParaTiTabContent(
    productos: List<Product>,
    onProductClick: (Product) -> Unit,
    onAgregarClick: (Product) -> Unit,
    onSeeMoreProductsClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    // Banners tipo carrusel sin foto, solo fondo suave, texto y botón según requerimiento
    val banners = remember {
        listOf(
            BannerSlide(
                title = "Envíos a todo\nChile",
                subtitle = "Recibe tus productos sustentables en la puerta de tu hogar",
                buttonText = "Conocer más",
                backgroundColor = Color(0xFFD6DEC9) // Verde salvia suave del mockup
            ),
            // Banner para
            BannerSlide(
                title = "Compara\ncon Buen Origen",
                subtitle = "Haz click en la camara en nuestros productos y ve las diferencias",
                buttonText = "Descubrir",
                backgroundColor = Color(0xFFE5DDD0) // Puedes cambiar el color de fondo
            ),

            BannerSlide(
                title = "Productos\nLocales",
                subtitle = "Apoya a pequeños emprendedores y productores de Chile",
                buttonText = "Explorar",
                backgroundColor = Color(0xFFDFD6C7) // Tono tierra suave
            ),
            BannerSlide(
                title = "Huella\nVerde",
                subtitle = "Acumula puntos en cada compra y canjea recompensas",
                buttonText = "Ver puntos",
                backgroundColor = Color(0xFFCAD7C8) // Eucalipto suave
            )
        )
    }

    val pagerState = rememberPagerState(pageCount = { banners.size })

    // Selección de recomendados: si la API no provee lista de recomendados,
    // se eligen productos destacados por puntos verdes o descuentos
    val recommendedProducts = remember(productos) {
        if (productos.isEmpty()) {
            emptyList()
        } else {
            val destacados = productos.filter { it.greenPoints > 0 || it.discountPercentage > 0 }
            if (destacados.size >= 4) destacados.take(6) else productos.take(6)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        // Carrusel Banner Superior (permite ver parte de la siguiente tarjeta al lado)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 44.dp),
                pageSpacing = 12.dp
            ) { page ->
                val banner = banners[page]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(195.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = banner.backgroundColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 18.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Encabezado del banner: logo_solo + Buen Origen
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo_solo),
                                contentDescription = "Logo Buen Origen",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Buen Origen",
                                style = CustomBrandTitleStyle.copy(
                                    fontSize = 14.sp,
                                    lineHeight = 16.sp,
                                    color = Color(0xFF1C3224)
                                )
                            )
                        }

                        // Título y subtítulo
                        Column {
                            Text(
                                text = banner.title,
                                style = CustomBrandTitleStyle.copy(
                                    fontSize = 26.sp,
                                    lineHeight = 28.sp,
                                    color = Color(0xFF1C3224),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = banner.subtitle,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    lineHeight = 14.sp,
                                    color = Color(0xFF2C3E2F)
                                ),
                                maxLines = 2
                            )
                        }

                        // Botón terracota redondeado
                        Button(
                            onClick = { onSeeMoreProductsClick() },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF8E412E),
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 8.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text(
                                text = banner.buttonText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Indicadores de puntos
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(banners.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (pagerState.currentPage == index) 8.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (pagerState.currentPage == index) Color(0xFF1C3224) else Color.Gray.copy(
                                    alpha = 0.4f
                                )
                            )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

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
                    fontSize = 19.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Ver más",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.clickable { onSeeMoreProductsClick() }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Cuadrícula de 2 columnas de productos recomendados
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            val chunkedProducts = recommendedProducts.chunked(2)
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
            .clickable { onProductClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFEDE9E1)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(145.dp)
                    .background(Color(0xFFF6F3ED))
            ) {
                AsyncImage(
                    model = product.imageUrl.ifBlank { R.drawable.fondo_producto },
                    contentDescription = product.productName,
                    fallback = painterResource(id = R.drawable.fondo_producto),
                    error = painterResource(id = R.drawable.fondo_producto),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Botón circular terracota de agregar al carrito estilo mockup
                IconButton(
                    onClick = onAgregarClick,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .size(36.dp)
                        .background(Color(0xFF8E412E), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ShoppingCart,
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
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // 5 estrellas oscuras estilo mockup
                Row(
                    modifier = Modifier.padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(5) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color(0xFF1B2B1E),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                // Fila de precio y etiqueta de descuento (Imagen 4)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = product.price.formatAsCurrency(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (product.discountPercentage > 0) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color.Transparent,
                            border = BorderStroke(1.dp, Color(0xFFE53935))
                        ) {
                            Text(
                                text = "${product.discountPercentage}%",
                                color = Color(0xFFE53935),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                            )
                        }
                    }
                }

                // Precio de lista tachado si tiene descuento (Imagen 4)
                if (product.listPrice > product.price) {
                    Text(
                        text = product.listPrice.formatAsCurrency(),
                        style = MaterialTheme.typography.bodySmall.copy(
                            textDecoration = TextDecoration.LineThrough,
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    )
                }

                // Etiquetas distintivas (categoría / materiales naturales / hecho a mano)
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val tagText =
                        if (product.category.isNotBlank()) product.category else "Materiales Naturales"
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF6D8CC)
                    ) {
                        Text(
                            text = tagText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF6B2A15),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                if (product.greenPoints > 0 || product.discountPercentage > 0) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFDCE8D8),
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = "Hecho a mano",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1C3224),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
