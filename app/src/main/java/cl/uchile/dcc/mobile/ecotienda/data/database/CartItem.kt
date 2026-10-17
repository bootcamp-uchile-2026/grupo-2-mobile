package cl.uchile.dcc.mobile.ecotienda.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = false)
    val productId: String,
    @ColumnInfo(name = "product_name")
    val productName: String,
    @ColumnInfo(name = "price")
    val price: Int,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @ColumnInfo(name = "producer_id")
    val producerID: String,
    @ColumnInfo(name = "product_producer")
    val productProducer: String,
    @ColumnInfo(name = "quantity")
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

    // Agrupar productos por por productor (varios puestos)?
    fun itemsByProducer(): Map<String, List<CartItem>> =
        items.groupBy { it.producerID }
}
