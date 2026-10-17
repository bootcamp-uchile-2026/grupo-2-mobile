package cl.uchile.dcc.mobile.ecotienda.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cl.uchile.dcc.mobile.ecotienda.R

// Definición de las fuentes personalizadas desde res/font
val CharmanSerifFontFamily = FontFamily(
    Font(R.font.charman_serif_black, FontWeight.Black),
)

val RecoletaFontFamily = FontFamily(
    Font(R.font.recoleta_black, FontWeight.Black),
)

val CustomBrandTitleStyle = TextStyle(
    fontFamily = CharmanSerifFontFamily,
    fontSize = 40.sp,
    lineHeight = 32.sp,
    fontWeight = FontWeight.Black,
)

val CustomBrandParagraph = TextStyle(
    fontFamily = CharmanSerifFontFamily,
    fontSize = 14.sp,
    lineHeight = 32.sp,
    fontWeight = FontWeight.Normal,
)

// Configuración de Tipografía para la aplicación
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = RecoletaFontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = CharmanSerifFontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = RecoletaFontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
)
