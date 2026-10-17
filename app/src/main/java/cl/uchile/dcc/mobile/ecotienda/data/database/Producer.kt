package cl.uchile.dcc.mobile.ecotienda.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "producer_registry")
data class Producer(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @ColumnInfo(name = "history")
    val history: String,
    @ColumnInfo(name = "location")
    val location: String,
)