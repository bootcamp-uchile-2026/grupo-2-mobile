package cl.uchile.dcc.mobile.ecotienda.data

import java.util.UUID

data class Product(
    var id: String = UUID.randomUUID().toString(),
    var price: Int,
    var description: String,
    var productProducer: String,
    var productName: String,
    val imageUrl: String,
    val stock: Int,
    val category: String,
    val producerID: String, // Referencia a ID de productor

)