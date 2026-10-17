package cl.uchile.dcc.mobile.ecotienda.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(
    entities = [Product::class, Producer::class, User::class, CartItem::class],
    version = 1,
    exportSchema = false
)

abstract class BuenOrigenDatabase : RoomDatabase() {
    abstract val productDAO: ProductDAO
    abstract val producerDAO: ProducerDAO
    abstract val userDAO: UserDAO
    abstract val cartItemDAO: CartItemDAO

    companion object {
        @Volatile
        private var INSTANCE: BuenOrigenDatabase? = null

        fun getInstance(context: Context): BuenOrigenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BuenOrigenDatabase::class.java,
                    "buen_origen_database"
                )
//                    .addMigrations(MIGRATION_1_TO_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

//        val MIGRATION_1_TO_2 = object : Migration(1, 2) {
//            override fun migrate(connection: SupportSQLiteDatabase) {
//                connection.execSQL("ALTER TABLE product_registry ADD COLUMN tipo_id TEXT")
//                // Implement the migration logic here
//                // For example, if you want to add a new column to the Product table:
//                // database.execSQL("ALTER TABLE product_registry ADD COLUMN new_column_name TEXT")
//            }
//        }
    }
}