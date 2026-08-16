package cl.uchile.dcc.mobile.ecotienda.ui.screen

import cl.uchile.dcc.mobile.ecotienda.model.DefaultData
import cl.uchile.dcc.mobile.ecotienda.model.Product

data class ProductScreenState(
    // vista de productos
    val price: Int = 0,
    val description: String = "",
    val productProducer: String = "",
    val productName: String = "",
    val imageUrl: String = "",
    val stock: Int = 0,
    val category: String = "",
    val producerID: String = "",
    val product: List<Product> = DefaultData.Product


)
