package cl.uchile.dcc.mobile.ecotienda.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.uchile.dcc.mobile.ecotienda.model.CartItem
import cl.uchile.dcc.mobile.ecotienda.ui.screenstates.ShippingFormState
import cl.uchile.dcc.mobile.ecotienda.ui.theme.Sage
import cl.uchile.dcc.mobile.ecotienda.ui.theme.ecoTiendaColors
import cl.uchile.dcc.mobile.ecotienda.utils.formatAsCurrency
import coil3.compose.AsyncImage

@Composable
fun CartItemRow(
    item: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.productName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.productName, fontWeight = FontWeight.Bold)
                Text(text = item.productProducer, style = MaterialTheme.typography.bodySmall)
                Text(text = item.price.formatAsCurrency(), color = Sage)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDecrement) {
                    Icon(Icons.Default.RemoveCircleOutline, contentDescription = "Menos")
                }
                Text(text = item.quantity.toString(), fontWeight = FontWeight.Bold)
                IconButton(onClick = onIncrement) {
                    Icon(Icons.Default.AddCircleOutline, contentDescription = "Más")
                }
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar", tint = Color.Red.copy(alpha = 0.7f))
                }
            }
        }
    }
}

@Composable
fun OrderSummaryCard(
    subtotal: Int,
    discount: Int,
    total: Int,
    onConfirm: () -> Unit,
    confirmLabel: String = "Confirmar y Pagar"
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "RESUMEN DE LA COMPRA", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "SUBTOTAL")
                Text(text = subtotal.formatAsCurrency())
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "CUPÓN DE DESCUENTO")
                Text(text = "- ${discount.formatAsCurrency()}")
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "TOTAL A PAGAR", fontWeight = FontWeight.Bold)
                Text(text = total.formatAsCurrency(), fontWeight = FontWeight.Bold, color = Sage)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Sage),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(text = confirmLabel, color = Color.White)
            }
        }
    }
}

@Composable
fun ShippingForm(
    state: ShippingFormState,
    onUpdate: (ShippingFormState) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(text = "ENVÍO", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        
        OutlinedTextField(
            value = state.commune,
            onValueChange = { onUpdate(state.copy(commune = it)) },
            label = { Text("Comuna*") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.street,
            onValueChange = { onUpdate(state.copy(street = it)) },
            label = { Text("Calle*") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = state.number,
                onValueChange = { onUpdate(state.copy(number = it)) },
                label = { Text("Número*") },
                modifier = Modifier.weight(1f),
                enabled = !state.isNoNumber
            )
            Spacer(modifier = Modifier.width(8.dp))
            Checkbox(
                checked = state.isNoNumber,
                onCheckedChange = { onUpdate(state.copy(isNoNumber = it)) }
            )
            Text(text = "Sin número", style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.apartment,
            onValueChange = { onUpdate(state.copy(apartment = it)) },
            label = { Text("Depto/ofic/casa (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.deliveryInstructions,
            onValueChange = { onUpdate(state.copy(deliveryInstructions = it)) },
            label = { Text("Indicaciones para la entrega (opcional)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
    }
}

@Composable
fun PaymentSelector(
    selectedMethod: String,
    onSelect: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(text = "MÉTODO DE PAGO", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        
        val methods = listOf("Tarjeta de Crédito", "Tarjeta de Débito", "Transferencia Bancaria", "Mercado Pago")
        methods.forEach { method ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(method) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = method == selectedMethod,
                    onClick = { onSelect(method) },
                    colors = RadioButtonDefaults.colors(selectedColor = Sage)
                )
                Text(text = method, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}
