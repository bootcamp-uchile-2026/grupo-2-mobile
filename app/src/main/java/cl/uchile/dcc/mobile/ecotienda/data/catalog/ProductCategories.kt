package cl.uchile.dcc.mobile.ecotienda.data.catalog

// Ocho categorías principales. GET /categories de la API 3.0 responde 500,
// así que el nombre sale de este mapa, alineado con los categoryId del catálogo.
object ProductCategories {
    private val names = mapOf(
        1 to "Alimentos",
        2 to "Cuidado personal",
        3 to "Huerto y jardinería",
        4 to "Limpieza del hogar",
        5 to "Aromas y bienestar",
        6 to "Ropa y accesorios",
        7 to "Mascotas",
        8 to "Decoración y artesanías",
    )

    fun nameOf(categoryId: Int): String = names[categoryId] ?: "Otros"
}
