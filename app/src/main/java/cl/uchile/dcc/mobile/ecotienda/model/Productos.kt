package cl.uchile.dcc.mobile.ecotienda.model

data class Productos(
    var id: Int,
    var precio: Int,
    var productorId: Int,
    var descripcion: String,
    var nombreProducto: String
)
