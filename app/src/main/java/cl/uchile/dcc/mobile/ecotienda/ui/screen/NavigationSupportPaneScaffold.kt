package cl.uchile.dcc.mobile.ecotienda.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.NavigableSupportingPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProductCatalogWithFilters() {
    val navigator = rememberSupportingPaneScaffoldNavigator()
    val scope = rememberCoroutineScope()
    val backNavigationBehavior = BackNavigationBehavior.PopUntilScaffoldValueChange

    NavigableSupportingPaneScaffold(
        navigator = navigator,
        mainPane = {
            AnimatedPane(
                modifier = Modifier
                    .safeContentPadding()
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Scaffold(
                    floatingActionButton = {
                        // Solo mostramos el FAB si el panel de soporte está oculto (en pantallas pequeñas)
                        if (navigator.scaffoldValue[SupportingPaneScaffoldRole.Supporting] == PaneAdaptedValue.Hidden) {
                            FloatingActionButton(
                                onClick = {
                                    scope.launch {
                                        navigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                                    }
                                }
                            ) {
                                Icon(Icons.Default.FilterList, contentDescription = "Filtros")
                            }
                        }
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Lista de Productos",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = "Aquí aparecerá el contenido de tu catálogo principal.",
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        
                        if (navigator.scaffoldValue[SupportingPaneScaffoldRole.Supporting] != PaneAdaptedValue.Hidden) {
                            Text(
                                text = "Los filtros están visibles al lado.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    }
                }
            }
        },
        supportingPane = {
            AnimatedPane(
                modifier = Modifier
                    .safeContentPadding()
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    // Botón de cerrar (X)
                    // Se muestra si el panel está expandido ocupando pantalla (en móviles)
                    if (navigator.scaffoldValue[SupportingPaneScaffoldRole.Supporting] == PaneAdaptedValue.Expanded) {
                        IconButton(
                            modifier = Modifier.align(Alignment.End),
                            onClick = {
                                scope.launch {
                                    navigator.navigateBack(backNavigationBehavior)
                                }
                            }
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar filtros")
                        }
                    }

                    Text(
                        text = "Filtros",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Aquí irían los componentes de filtro
                    Text("• Categoría")
                    Text("• Rango de Precio")
                    Text("• Calificación")
                    
                    Button(
                        onClick = { /* Aplicar filtros */ },
                        modifier = Modifier.padding(top = 24.dp)
                    ) {
                        Text("Aplicar Filtros")
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true, widthDp = 400, name = "Vista Móvil")
@Composable
fun CatalogPreviewMobile() {
    MaterialTheme {
        ProductCatalogWithFilters()
    }
}

@Preview(showBackground = true, widthDp = 900, name = "Vista Tablet")
@Composable
fun CatalogPreviewTablet() {
    MaterialTheme {
        ProductCatalogWithFilters()
    }
}
