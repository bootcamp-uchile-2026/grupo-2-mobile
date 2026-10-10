package cl.uchile.dcc.mobile.ecotienda.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.uchile.dcc.mobile.ecotienda.data.Product
import cl.uchile.dcc.mobile.ecotienda.data.database.ProductRepository
import cl.uchile.dcc.mobile.ecotienda.ui.screen.EcoTiendaApp
import cl.uchile.dcc.mobile.ecotienda.ui.theme.EcoTiendaTheme

// MainActivity liviana: se mudó el contenedor de repos a AppContainer
// y la obtención de ViewModels se maneja automáticamente en cada ViewModel
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val configDatabase = ProductRepository(applicationContext)
//        configDatabase.addProduct(
//            Product(
//                id = "1",
//                productName = "Producto 1",
//                description = "Descripción del producto 1",
//                price = 1000,
//                stock = 10,
//                category = "Categoría A",
//                imageUrl = "https://example.com/producto1.jpg",
//                productProducer = "Productor 1",
//                producerID = "P1"
//            )
//        )

        setContent {
            EcoTiendaTheme {
                EcoTiendaApp()
            }
        }
    }
}