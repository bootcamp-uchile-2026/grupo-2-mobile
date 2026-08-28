package cl.uchile.dcc.mobile.ecotienda.utils

import java.text.NumberFormat
import java.util.Locale

/**
 * Formatea un entero como moneda chilena ($1.000)
 */
fun Int.formatAsCurrency(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    // Eliminamos los decimales si no son necesarios
    format.maximumFractionDigits = 0
    return format.format(this)
}