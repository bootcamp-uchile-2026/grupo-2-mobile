package cl.uchile.dcc.mobile.ecotienda.model

data class CartItem(
    val productId: String,
    val productName: String,
    val price: Int,
    val imageUrl: String,
    val producerID: String,
    val productProducer: String,
    val quantity: Int
) {
    val subtotal: Int get() = price * quantity
}

data class Cart(
    val items: List<CartItem> = emptyList()
) {
    val itemCount: Int get() = items.sumOf { it.quantity }
    val total: Int get() = items.sumOf { it.subtotal }

    fun quantityOf(productId: String): Int =
        items.find { it.productId == productId }?.quantity ?: 0

    /** Si vendes por productor (varios puestos) */
    fun itemsByProducer(): Map<String, List<CartItem>> =
        items.groupBy { it.producerID }
}
