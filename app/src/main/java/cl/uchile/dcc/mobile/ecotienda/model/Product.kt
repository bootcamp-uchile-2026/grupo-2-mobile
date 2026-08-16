package cl.uchile.dcc.mobile.ecotienda.model

import java.util.UUID

data class Product(
    var id: String = UUID.randomUUID().toString() + "ETV1",
    var price: Int,
    var description: String,
    var productProducer: String,
    var productName: String,
    val imageUrl: String,
    val stock: Int,
    val category: String,
    val producerID: String, // Referencia a ID de productor

)