package cl.uchile.dcc.mobile.ecotienda.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.EnergySavingsLeaf
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.uchile.dcc.mobile.ecotienda.utils.formatKilograms
import java.text.NumberFormat
import java.util.Locale

@Composable
fun FootprintCards(
    co2Kg: Double,
    greenPoints: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        FootprintCard(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            container = MaterialTheme.colorScheme.primaryContainer,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Cloud,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(22.dp),
                )
            },
            value = formatKilograms(co2Kg),
            title = "CO₂ ahorrado",
            caption = "Impacto ambiental estimado",
        )
        FootprintCard(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            container = MaterialTheme.colorScheme.secondaryContainer,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.EnergySavingsLeaf,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(22.dp),
                )
            },
            value = "+${formatPoints(greenPoints)}",
            title = "Puntos Verdes",
            caption = "Qué acumulas con esta compra",
        )
    }
}

@Composable
private fun FootprintCard(
    modifier: Modifier,
    container: androidx.compose.ui.graphics.Color,
    icon: @Composable () -> Unit,
    value: String,
    title: String,
    caption: String,
) {
    val content = if (container == MaterialTheme.colorScheme.secondaryContainer) {
        MaterialTheme.colorScheme.onSecondaryContainer
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer
    }
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = container,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            icon()
            Text(
                text = value,
                color = content,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                lineHeight = 32.sp,
            )
            Text(
                text = title,
                color = content,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
            )
            Text(
                text = caption,
                color = content.copy(alpha = 0.8f),
                fontSize = 12.sp,
                lineHeight = 16.sp,
            )
        }
    }
}

private fun formatPoints(value: Int): String {
    return NumberFormat.getIntegerInstance(Locale.forLanguageTag("es-CL")).format(value)
}
