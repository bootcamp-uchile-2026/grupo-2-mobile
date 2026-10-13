package cl.uchile.dcc.mobile.ecotienda.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "product_registry")
data class Product(
    @PrimaryKey(autoGenerate = false)
    var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "price")
    var price: Int,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "product_producer")
    var productProducer: String, // Nombre para mostrar
    @ColumnInfo(name = "product_name")
    var productName: String,
    @ColumnInfo(name = "image_url")
    var imageUrl: String,
    @ColumnInfo(name = "stock")
    var stock: Int,
    @ColumnInfo(name = "category")
    var category: String,
    @ColumnInfo(name = "producer_id")
    var producerID: String, // Referencia a ID de productor

)