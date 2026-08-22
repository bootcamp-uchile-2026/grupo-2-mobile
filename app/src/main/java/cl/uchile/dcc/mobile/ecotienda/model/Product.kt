package cl.uchile.dcc.mobile.ecotienda.model

data class Product(
    var id: String,
    var price: Int,
    var description: String,
    var productName: String,
    val imageUrl: String
)