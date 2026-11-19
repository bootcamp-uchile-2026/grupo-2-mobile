package cl.uchile.dcc.mobile.ecotienda.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.uchile.dcc.mobile.ecotienda.data.address.ChileCommunes
import cl.uchile.dcc.mobile.ecotienda.data.database.CartItem
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.HOME_DELIVERY_COST
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.ShippingFormState
import cl.uchile.dcc.mobile.ecotienda.utils.formatAsCurrency
import coil3.compose.AsyncImage

private val CheckoutCardShape = RoundedCornerShape(22.dp)
private val CheckoutFieldShape = RoundedCornerShape(14.dp)

@Composable
fun CheckoutScreenTitle(
    text: String,
    subtitle: String? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 32.sp,
                lineHeight = 38.sp,
            ),
        )
        if (!subtitle.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
fun CheckoutCard(
    modifier: Modifier = Modifier,
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = CheckoutCardShape,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 3.dp,
        tonalElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (!title.isNullOrBlank()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            content()
        }
    }
}

// Organización de los productos en el carrito
@Composable
fun CartProductRow(
    item: CartItem,
    subtitle: String,
    onQuantityChange: (Int) -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val accent = MaterialTheme.colorScheme.primary

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.productName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(84.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = item.productName,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = item.price.formatAsCurrency(),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                if (subtitle.isNotBlank()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .border(1.dp, accent, RoundedCornerShape(20.dp))
                                .clickable { menuExpanded = true }
                                .padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(
                                text = item.quantity.toString(),
                                color = accent,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = "Cambiar cantidad",
                                tint = accent,
                                modifier = Modifier.size(18.dp),
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                        ) {
                            (1..10).forEach { qty ->
                                DropdownMenuItem(
                                    text = { Text(qty.toString()) },
                                    onClick = {
                                        menuExpanded = false
                                        onQuantityChange(qty)
                                    },
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onRemove),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Eliminar",
                            tint = accent,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .border(1.25.dp, accent, CircleShape)
                            .clip(CircleShape)
                            .clickable(onClick = onDecrement),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Remove,
                            contentDescription = "Disminuir cantidad",
                            tint = accent,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
                Text(
                    text = item.subtotal.formatAsCurrency(),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 2.dp),
                )
            }
        }
    }
}

// Chequeo de cupon
@Composable
fun CouponSection(
    code: String,
    feedback: String,
    applied: Boolean,
    onCodeChange: (String) -> Unit,
    onApply: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = code,
            onValueChange = onCodeChange,
            placeholder = { Text("Ingresa número de cupón") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = CheckoutFieldShape,
            colors = checkoutFieldColors(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(
            onClick = onApply,
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
        ) {
            Text("Aplicar cupón de descuento")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Outlined.LocalOffer,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
        }
        if (feedback.isNotBlank()) {
            Text(
                text = feedback,
                style = MaterialTheme.typography.bodySmall,
                color = if (applied) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                },
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

// Resumen de la compra según wireframes de UX/UI
@Composable
fun OrderSummarySection(
    productTotal: Int,
    shipping: Int,
    discount: Int,
    modifier: Modifier = Modifier,
) {
    val total = ((productTotal - discount) + shipping).coerceAtLeast(0)
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = "Resumen de la orden",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 22.sp,
                lineHeight = 28.sp,
            ),
        )
        SummaryRow("Total producto(s)", productTotal.formatAsCurrency())
        if (discount > 0) {
            SummaryRow("Cupón", "- ${discount.formatAsCurrency()}")
        }
        SummaryRow("Envío", shipping.formatAsCurrency())
        SummaryRow("Total", total.formatAsCurrency(), bold = true)
    }
}

@Composable
fun CheckoutActionButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
        ),
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun ShippingForm(
    state: ShippingFormState,
    onUpdate: (ShippingFormState) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        CheckoutCard(title = "Tipo de envío") {
            SelectableOption(
                title = "Envío a domicilio",
                subtitle = HOME_DELIVERY_COST.formatAsCurrency(),
                selected = state.deliveryType == ShippingFormState.DELIVERY,
                onClick = { onUpdate(state.copy(deliveryType = ShippingFormState.DELIVERY)) },
            )
            SelectableOption(
                title = "Retiro en tienda",
                subtitle = "Sin costo",
                selected = state.deliveryType == ShippingFormState.PICKUP,
                onClick = { onUpdate(state.copy(deliveryType = ShippingFormState.PICKUP)) },
            )
        }
        if (state.deliveryType != ShippingFormState.PICKUP) {
            CheckoutCard(title = "Dirección de entrega") {
                CommuneField(
                    value = state.commune,
                    onValueChange = { onUpdate(state.copy(commune = it)) },
                )
                CheckoutField(
                    value = state.street,
                    onValueChange = { onUpdate(state.copy(street = it)) },
                    label = "Calle",
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CheckoutField(
                        value = state.number,
                        onValueChange = { onUpdate(state.copy(number = it)) },
                        label = "Número",
                        enabled = !state.isNoNumber,
                        modifier = Modifier
                            .weight(1f)
                            .defaultMinSize(minWidth = 1.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Checkbox(
                        checked = state.isNoNumber,
                        onCheckedChange = { onUpdate(state.copy(isNoNumber = it, number = if (it) "" else state.number)) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                        ),
                    )
                    Text(
                        text = "Sin número",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                CheckoutField(
                    value = state.apartment,
                    onValueChange = { onUpdate(state.copy(apartment = it)) },
                    label = "Depto / oficina (opcional)",
                )
                CheckoutField(
                    value = state.deliveryInstructions,
                    onValueChange = { onUpdate(state.copy(deliveryInstructions = it)) },
                    label = "Indicaciones para la entrega (opcional)",
                    singleLine = false,
                    minLines = 3,
                )
            }
            Text(
                text = "Contrastamos la calle con OpenStreetMap. © colaboradores de OpenStreetMap.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
fun PaymentSelector(
    selectedMethod: String,
    onSelect: (String) -> Unit,
) {
    val methods = listOf(
        "Tarjeta de crédito",
        "Tarjeta de débito",
        "Transferencia bancaria",
        "Mercado Pago",
    )
    CheckoutCard(title = "Método de pago") {
        methods.forEach { method ->
            SelectableOption(
                title = method,
                subtitle = null,
                selected = method == selectedMethod,
                onClick = { onSelect(method) },
            )
        }
    }
}

@Composable
private fun CommuneField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var fieldWidth by remember { mutableStateOf(0) }
    val suggestions = ChileCommunes.suggestions(value)
    Box(modifier = Modifier.fillMaxWidth()) {
        CheckoutField(
            value = value,
            onValueChange = {
                onValueChange(it)
                expanded = ChileCommunes.suggestions(it).isNotEmpty()
            },
            label = "Comuna",
            trailingIcon = {
                IconButton(onClick = { expanded = suggestions.isNotEmpty() && !expanded }) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Ver comunas",
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { fieldWidth = it.size.width },
        )
        DropdownMenu(
            expanded = expanded && suggestions.isNotEmpty(),
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { fieldWidth.toDp() }),
        ) {
            suggestions.forEach { name ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = {
                        onValueChange(name)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun CheckoutField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true,
    singleLine: Boolean = true,
    minLines: Int = 1,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        enabled = enabled,
        singleLine = singleLine,
        minLines = minLines,
        trailingIcon = trailingIcon,
        shape = CheckoutFieldShape,
        colors = checkoutFieldColors(),
    )
}

@Composable
private fun SelectableOption(
    title: String,
    subtitle: String?,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val borderColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
            ),
        )
        Column(modifier = Modifier.padding(end = 8.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    bold: Boolean = false,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = value,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun checkoutFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
    disabledBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
    cursorColor = MaterialTheme.colorScheme.primary,
    focusedLabelColor = MaterialTheme.colorScheme.primary,
    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
)

@Composable
fun CheckoutDivider() {
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f))
}
