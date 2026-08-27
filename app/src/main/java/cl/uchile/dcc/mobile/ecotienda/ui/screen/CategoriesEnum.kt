package cl.uchile.dcc.mobile.ecotienda.ui.screen

enum class Category(val title: String, val icon: String? = null) {
    TODOS("Todos"),
    MERCADO("Mercado"),
    CUIDADOPERSONAL("Cuidado Personal"),
    HUERTOENCASA("Huerto en Casa"),
    HOGAR("Cuidado del Hogar"),
    BIENESTAR("Bienestar"),
    MODA("Moda Natural"),
    MASCOTAS("Mascotas"),
    ARTESANIAS("Artesanías"),

}

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val category: Category,
    val description: String = ""
)