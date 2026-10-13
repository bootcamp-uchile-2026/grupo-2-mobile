package cl.uchile.dcc.mobile.ecotienda.data.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [Product::class, Producer::class, User::class, CartItem::class],
    version = 1,
    exportSchema = false
)

abstract class BuenOrigenDAO: RoomDatabase() {
    abstract val productDAO: ProductDAO
    abstract val producerDAO: ProducerDAO
    abstract val userDAO: UserDAO
    abstract val cartItemDAO: CartItemDAO

    companion object {
        
    }
}