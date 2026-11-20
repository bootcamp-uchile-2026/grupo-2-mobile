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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.asImageBitmap
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
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
            ),
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = product?.productName?.let { "Etiqueta contra $it" }
                ?: "Saca una foto de la etiqueta de otro producto.",
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
                                        errorMessage = error ?: "No se leyó texto en la etiqueta."
                                    } else {
                                        comparison = compareLabelWithProduct(product, text)
                                    }
                                }
                            }

                            override fun onError(exception: ImageCaptureException) {
                                reading = false
                                errorMessage = "No se pudo tomar la foto."
                            }
                        },
                    )
                },
                enabled = !reading && imageCapture != null,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                ),
            ) {
                Text(if (reading) "Leyendo etiqueta..." else "Leer etiqueta")
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
    ) {
        Text(
            text = result.verdict,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        result.reasons.forEach { reason ->
            Text(
                text = reason,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 6.dp),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Texto de la etiqueta",
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = result.labelText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Nuestro producto",
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = result.oursText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

// Lectura de texto de la foto
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
    val labelText: String,
    val oursText: String,
    val reasons: List<String>,
    val verdict: String,
)

private val buenOrigenSignals = listOf(
    "orgánico", "organico", "reciclado", "natural", "biodegradable",
    "local", "artesanal", "algodón", "algodon", "comercio justo",
)

private val weakerSignals = listOf(
    "plástico", "plastico", "poliéster", "poliester",
    "sintético", "sintetico", "importado",
)

// Comparación de la etiqueta con el texto descriptivo o materiales del producto
fun compareLabelWithProduct(product: Product?, labelText: String): LabelComparison {
    val oursText = listOfNotNull(
        product?.productName,
        product?.productProducer,
        product?.description,
    ).filter { it.isNotBlank() }.joinToString(". ").ifBlank { "Producto Buen Origen" }

    val label = labelText.lowercase()
    val ours = oursText.lowercase()
    val reasons = buildList {
        val weak = weakerSignals.filter { label.contains(it) }
        if (weak.isNotEmpty()) {
            add("La etiqueta menciona ${weak.joinToString()}.")
        }
        val oursOnly = buenOrigenSignals.filter { ours.contains(it) && !label.contains(it) }
        if (oursOnly.isNotEmpty()) {
            add("Buen Origen declara ${oursOnly.joinToString()} y la etiqueta no.")
        }
        if (isEmpty()) {
            add("Buen Origen publica el origen y la elaboración. La etiqueta leída no muestra esa trazabilidad.")
        }
    }

    return LabelComparison(
        labelText = labelText.trim(),
        oursText = oursText,
        reasons = reasons,
        verdict = "Buen Origen es mejor opción",
    )
}
