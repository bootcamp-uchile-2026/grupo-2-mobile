package cl.uchile.dcc.mobile.ecotienda.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.ProductFilter
import cl.uchile.dcc.mobile.ecotienda.ui.theme.CustomBrandTitleStyle

private val sortOptions = listOf(
    "Ordenar por...",
    "Menor precio",
    "Mayor precio",
    "Más recientes",
    "Mayor calificación",
    "Más sustentable"
)

private val categoryOptions = listOf(
    "Todas las categorías",
    "Alimentos",
    "Aromas y bienestar",
    "Artesanías",
    "Cuidado personal",
    "Huerto",
    "Limpieza del hogar",
    "Mascotas",
    "Ropa y accesorios"
)

private val productTypesByCategory = mapOf(
    "Ropa y accesorios" to listOf("Polerones", "Poleras", "Pantalones", "Chaquetas", "Accesorios"),
    "Alimentos" to listOf("Frutas y verduras", "Snacks saludables", "Conservas", "Café y té", "Miel"),
    "Aromas y bienestar" to listOf("Velas", "Aceites esenciales", "Inciensos", "Difusores"),
    "Artesanías" to listOf("Cerámica", "Madera", "Textiles", "Decoración"),
    "Cuidado personal" to listOf("Jabones", "Shampoo", "Cremas", "Cepillos"),
    "Huerto" to listOf("Semillas", "Macetas", "Herramientas", "Sustratos"),
    "Limpieza del hogar" to listOf("Detergentes", "Lavaloza", "Paños y esponjas"),
    "Mascotas" to listOf("Alimentos mascotas", "Juguetes", "Cuidado e higiene")
)

private val sustainableAttributesList = listOf(
    "Orgánico",
    "Reciclado",
    "Comercio justo",
    "Vegano",
    "Biodegradable",
    "Envase reciclable",
    "Cruelty-Free",
    "Compostable"
)

private val colorsList = listOf("Negro", "Blanco", "Rojo", "Verde", "Azul")
private val sizesList = listOf("S", "M", "L", "XL")
private val genderList = listOf("Femenino", "Masculino", "Neutro")
private val materialsList = listOf("Algodón", "Lino", "Lana")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    sheetState: SheetState,
    currentFilter: ProductFilter,
    searchQuery: String = "",
    onDismiss: () -> Unit,
    onApplyFilter: (ProductFilter) -> Unit
) {
    var sortOrder by remember { mutableStateOf(currentFilter.sortOrder) }
    var category by remember { mutableStateOf(currentFilter.category) }
    var productType by remember { mutableStateOf(currentFilter.productType) }
    var sustainableAttributes by remember { mutableStateOf(currentFilter.sustainableAttributes) }

    // Características de ropa (imágenes 3 y 4)
    var gender by remember { mutableStateOf(currentFilter.gender) }
    var size by remember { mutableStateOf(currentFilter.size) }
    var colors by remember { mutableStateOf(currentFilter.colors) }
    var material by remember { mutableStateOf(currentFilter.material) }

    var sortExpanded by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var typeExpanded by remember { mutableStateOf(false) }

    val isCategorySelected = category.isNotBlank() && category != "Todas las categorías"
    val isClothingFlow = category == "Ropa y accesorios" ||
            productType.contains("Poleron", ignoreCase = true) ||
            productType.contains("Polera", ignoreCase = true) ||
            searchQuery.contains("poler", ignoreCase = true) ||
            searchQuery.contains("ropa", ignoreCase = true) ||
            searchQuery.contains("polera", ignoreCase = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Encabezado con Filtro y Limpiar ✕
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filtro",
                    style = CustomBrandTitleStyle.copy(
                        fontSize = 28.sp,
                        lineHeight = 32.sp,
                        color = Color.Black
                    )
                )

                TextButton(
                    onClick = {
                        sortOrder = "Ordenar por..."
                        category = "Todas las categorías"
                        productType = ""
                        sustainableAttributes = emptySet()
                        gender = null
                        size = null
                        colors = emptySet()
                        material = null
                    }
                ) {
                    Text(
                        text = "Limpiar ✕",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!isClothingFlow) {
                // 1. Ordenar por...
                Text(
                    text = "Ordenar por...",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .border(1.dp, Color(0xFF2C3229), RoundedCornerShape(12.dp))
                            .clickable { sortExpanded = true }
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = sortOrder,
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }

                    DropdownMenu(
                        expanded = sortExpanded,
                        onDismissRequest = { sortExpanded = false }
                    ) {
                        sortOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    sortOrder = option
                                    sortExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Categoría
                Text(
                    text = "Categoría",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .border(1.dp, Color(0xFF2C3229), RoundedCornerShape(12.dp))
                            .clickable { categoryExpanded = true }
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = category,
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }

                    DropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categoryOptions.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    category = cat
                                    categoryExpanded = false
                                    productType = "" // Reset product type if category changes
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Tipo de producto (deshabilitado hasta que se elija categoría)
                Text(
                    text = "Tipo de producto",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .border(
                                    1.dp,
                                    if (isCategorySelected) Color(0xFF2C3229) else Color.Gray.copy(alpha = 0.4f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable(enabled = isCategorySelected) { typeExpanded = true }
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (productType.isNotBlank()) productType else "Tipo de producto",
                                fontSize = 15.sp,
                                color = if (isCategorySelected) Color.Black else Color.Gray
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = if (isCategorySelected) Color.Black else Color.Gray
                            )
                        }

                        if (!isCategorySelected) {
                            Text(
                                text = "Selecciona una categoría*",
                                color = Color(0xFFA04828),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                            )
                        }
                    }

                    if (isCategorySelected) {
                        val types = productTypesByCategory[category] ?: emptyList()
                        DropdownMenu(
                            expanded = typeExpanded,
                            onDismissRequest = { typeExpanded = false }
                        ) {
                            types.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type) },
                                    onClick = {
                                        productType = type
                                        typeExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 4. Atributos sustentables (Checkboxes)
                Text(
                    text = "Atributos sustentables",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(10.dp))

                sustainableAttributesList.forEach { attr ->
                    val isChecked = attr in sustainableAttributes
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                sustainableAttributes = if (isChecked) {
                                    sustainableAttributes - attr
                                } else {
                                    sustainableAttributes + attr
                                }
                            }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .background(
                                    if (isChecked) Color(0xFF9EAF97) else Color(0xFFD4DFC9),
                                    RoundedCornerShape(6.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isChecked) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = attr,
                            fontSize = 15.sp,
                            color = Color(0xFF2C3229)
                        )
                    }
                }
            } else {
                // Filtro especial para Ropa / Polerones (Imagen 3)

                // 1. Género (Radio buttons)
                Text(
                    text = "Género",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    genderList.forEach { g ->
                        val isSelected = gender == g
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { gender = if (isSelected) null else g }
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { gender = if (isSelected) null else g },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF1C3224)
                                )
                            )
                            Text(text = g, fontSize = 15.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 2. Talla (Chips cuadrados)
                Text(
                    text = "Talla",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    sizesList.forEach { s ->
                        val isSelected = size == s
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) Color(0xFF9EAF97) else Color(0xFFDCE5D6)
                                )
                                .clickable { size = if (isSelected) null else s },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = s,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (isSelected) Color.White else Color(0xFF2C3229)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 3. Color (Checkboxes)
                Text(
                    text = "Color",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                colorsList.forEach { c ->
                    val isChecked = c in colors
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                colors = if (isChecked) colors - c else colors + c
                            }
                            .padding(vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .background(
                                    if (isChecked) Color(0xFF9EAF97) else Color(0xFFD4DFC9),
                                    RoundedCornerShape(6.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isChecked) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = c, fontSize = 15.sp)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 4. Material (Radio buttons)
                Text(
                    text = "Material",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    materialsList.forEach { mat ->
                        val isSelected = material == mat
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { material = if (isSelected) null else mat }
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { material = if (isSelected) null else mat },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF1C3224)
                                )
                            )
                            Text(text = mat, fontSize = 15.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Botón Aplicar filtros terracota
            Button(
                onClick = {
                    onApplyFilter(
                        ProductFilter(
                            sortOrder = sortOrder,
                            category = category,
                            productType = productType,
                            sustainableAttributes = sustainableAttributes,
                            gender = gender,
                            size = size,
                            colors = colors,
                            material = material
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8E412E),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Aplicar filtros",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
