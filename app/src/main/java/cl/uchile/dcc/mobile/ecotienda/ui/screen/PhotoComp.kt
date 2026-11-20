package cl.uchile.dcc.mobile.ecotienda.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import cl.uchile.dcc.mobile.ecotienda.data.database.Product
import cl.uchile.dcc.mobile.ecotienda.ui.theme.CustomBrandTitleStyle
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.Locale

@Composable
fun PhotoComp(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    product: Product? = null,
) {
    val context = LocalContext.current
    var hasCamera by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCamera = granted }

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var reading by remember { mutableStateOf(false) }
    var comparison by remember { mutableStateOf<LabelComparison?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }

        Text(
            text = "Compara tu producto con Buen Origen",
            style = CustomBrandTitleStyle.copy(
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
            ),
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = product?.productName?.let { "Analiza una etiqueta y compárala con $it" }
                ?: "Saca una foto a la etiqueta de ropa o producto para comparar sus componentes.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (!hasCamera) {
            Button(
                onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                ),
            ) {
                Text("Permitir cámara")
            }
        } else {
            LabelCameraPreview(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(24.dp)),
                onImageCapture = { imageCapture = it },
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val capture = imageCapture ?: return@Button
                    reading = true
                    errorMessage = null
                    capture.takePicture(
                        ContextCompat.getMainExecutor(context),
                        object : ImageCapture.OnImageCapturedCallback() {
                            override fun onCaptureSuccess(image: ImageProxy) {
                                val bitmap = image.toBitmap()
                                image.close()
                                readLabel(bitmap) { text, error ->
                                    reading = false
                                    if (error != null || text.isNullOrBlank()) {
                                        comparison = null
                                        errorMessage = error ?: "No se detectó texto claro en la etiqueta."
                                    } else {
                                        comparison = compareLabelWithProduct(product, text)
                                    }
                                }
                            }

                            override fun onError(exception: ImageCaptureException) {
                                reading = false
                                errorMessage = "No se pudo capturar la foto."
                            }
                        },
                    )
                },
                enabled = !reading && imageCapture != null,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8E412E),
                    contentColor = Color.White,
                ),
            ) {
                Text(if (reading) "Analizando etiqueta..." else "Analizar etiqueta")
            }
        }

        if (reading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }

        errorMessage?.let { message ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }

        comparison?.let { result ->
            Spacer(modifier = Modifier.height(24.dp))
            ComparisonResult(result)
        }
    }
}

@Composable
private fun LabelCameraPreview(
    onImageCapture: (ImageCapture) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    LaunchedEffect(lifecycleOwner) {
        val future = ProcessCameraProvider.getInstance(context)
        future.addListener(
            {
                val provider = future.get()
                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }
                val capture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()
                provider.unbindAll()
                provider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    capture,
                )
                onImageCapture(capture)
            },
            ContextCompat.getMainExecutor(context),
        )
    }

    DisposableEffect(lifecycleOwner) {
        onDispose {
            val future = ProcessCameraProvider.getInstance(context)
            if (future.isDone) {
                future.get().unbindAll()
            }
        }
    }

    AndroidView(
        factory = { previewView },
        modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer),
    )
}

@Composable
private fun ComparisonResult(result: LabelComparison) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2DDD5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Veredicto
            Text(
                text = result.verdict,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = Color(0xFF1C3224),
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 1. Palabras y componentes relevantes detectados
            Text(
                text = "Componentes detectados en la etiqueta:",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(6.dp))

            if (result.detectedKeywords.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    result.detectedKeywords.forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF3ECE6),
                            border = BorderStroke(1.dp, Color(0xFFDECFC3))
                        ) {
                            Text(
                                text = tag,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF7A3926),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "No se reconocieron fibras específicas (ej. poliéster, algodón) en la foto.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Conclusiones y comparación en tono sustentable
            Text(
                text = "Análisis comparativo:",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(6.dp))

            result.reasons.forEach { reason ->
                Row(
                    modifier = Modifier.padding(bottom = 6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "• ",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8E412E)
                    )
                    Text(
                        text = reason,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 20.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Beneficios por comprar 1 producto en Buen Origen (CO2 y Puntos Verdes)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFF1F6EF),
                border = BorderStroke(1.dp, Color(0xFFC7DEC4))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "🌱 Beneficios por elegir 1 unidad en Buen Origen:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF1B3D23)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // CO2 Ahorrado
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF2E6F40),
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Eco,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ahorras ${result.co2Avoided} kg de CO₂ frente a un producto convencional.",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1B3D23)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Puntos Verdes
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF8E412E),
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ganas +${result.greenPoints} Puntos Verdes canjeables en tu cuenta.",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1B3D23)
                        )
                    }
                }
            }
        }
    }
}

// Lectura de texto de la foto mediante ML Kit
private fun readLabel(
    bitmap: android.graphics.Bitmap,
    onResult: (String?, String?) -> Unit,
) {
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    val image = InputImage.fromBitmap(bitmap, 0)
    recognizer.process(image)
        .addOnSuccessListener { visionText -> onResult(visionText.text, null) }
        .addOnFailureListener { onResult(null, "No se pudo leer la etiqueta.") }
        .addOnCompleteListener { recognizer.close() }
}

data class LabelComparison(
    val detectedKeywords: List<String>,
    val reasons: List<String>,
    val verdict: String,
    val greenPoints: Int,
    val co2Avoided: Double,
)

// Catálogo de materiales comunes a buscar en etiquetas
private val knownMaterials = listOf(
    "poliéster", "poliester", "polyester",
    "algodón", "algodon", "cotton",
    "elastano", "elastane", "spandex", "lycra",
    "acrílico", "acrilico", "acrylic",
    "nylon", "poliamida", "polyamide",
    "viscosa", "viscose", "rayón", "rayon",
    "lana", "wool",
    "lino", "linen",
    "seda", "silk",
    "plástico", "plastico", "plastic",
    "microfibra", "microfiber",
    "reciclado", "recycled"
)

// Extrae palabras clave y combinaciones de porcentaje (ej. "80% algodón", "poliéster")
private fun extractRelevantKeywords(rawText: String): List<String> {
    val text = rawText.lowercase()
    val foundList = mutableListOf<String>()

    // 1. Busca patrones con porcentajes (ej. "60% algodon", "100% polyester")
    val percentRegex = Regex("""(\d{1,3}%\s*[a-záéíóúñ]+)""")
    percentRegex.findAll(text).forEach { match ->
        val matchedStr = match.value.trim()
        if (knownMaterials.any { matchedStr.contains(it) }) {
            foundList.add(matchedStr.replaceFirstChar { it.uppercase() })
        }
    }

    // 2. Si no encontró porcentajes, busca los materiales individuales
    if (foundList.isEmpty()) {
        knownMaterials.forEach { mat ->
            if (text.contains(mat)) {
                foundList.add(mat.replaceFirstChar { it.uppercase() })
            }
        }
    }

    return foundList.distinct().take(5)
}

// Comparación de la etiqueta con los datos y beneficios de Buen Origen
fun compareLabelWithProduct(product: Product?, labelText: String): LabelComparison {
    val detected = extractRelevantKeywords(labelText)
    val label = labelText.lowercase()

    val synthetics = listOf("poliéster", "poliester", "polyester", "acrílico", "acrilico", "nylon", "spandex", "plástico", "sintético")
    val hasSynthetics = synthetics.any { label.contains(it) }
    val hasCotton = label.contains("algodón") || label.contains("algodon") || label.contains("cotton")

    val reasons = buildList {
        if (hasSynthetics) {
            add("La etiqueta contiene fibras sintéticas derivadas del petróleo que desprenden microplásticos al lavarse y tardan cientos de años en degradarse.")
        } else if (hasCotton) {
            add("La etiqueta registra algodón convencional, que suele requerir gran cantidad de pesticidas y agua en su cultivo industrial.")
        }

        if (product != null) {
            add("${product.productName} está confeccionado de forma artesanal y sustentable por ${product.productProducer}, garantizando materiales limpios y trazabilidad.")
        } else {
            add("Los productos de Buen Origen utilizan materias primas 100% nobles, orgánicas o recicladas con producción local.")
        }
    }

    val co2 = if ((product?.co2Avoided ?: 0.0) > 0.0) product!!.co2Avoided else 1.8
    val points = if ((product?.greenPoints ?: 0) > 0) product!!.greenPoints else 15

    return LabelComparison(
        detectedKeywords = detected,
        reasons = reasons,
        verdict = "Buen Origen es una alternativa más sustentable",
        greenPoints = points,
        co2Avoided = co2,
    )
}
