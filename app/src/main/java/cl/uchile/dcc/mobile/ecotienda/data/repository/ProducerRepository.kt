package cl.uchile.dcc.mobile.ecotienda.data.database_open_helper

import android.content.ContentValues
import android.content.Context
import cl.uchile.dcc.mobile.ecotienda.data.database.Producer

class ProducerRepository(
    private val context: Context
) {
    private val dbHelper = BuenOrigenOpenHelper(context)

    fun addProducer(producer: Producer) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("id", producer.id)
            put("name", producer.name)
            put("description", producer.description)
            put("imageUrl", producer.imageUrl)
            put("history", producer.history)
            put("location", producer.location)
        }
        db.insert(BuenOrigenOpenHelper.TABLE_PRODUCER_REGISTRY, null, values)
        db.close()
    }

    fun getAllProducers(): List<Producer> {
        val producers = mutableListOf<Producer>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            BuenOrigenOpenHelper.TABLE_PRODUCER_REGISTRY,
            null, null, null, null, null, null
        )
        with(cursor) {
            while (moveToNext()) {
                val id = getString(getColumnIndexOrThrow("id"))
                val name = getString(getColumnIndexOrThrow("name"))
                val description = getString(getColumnIndexOrThrow("description"))
                val imageUrl = getString(getColumnIndexOrThrow("imageUrl"))
                val history = getString(getColumnIndexOrThrow("history"))
                val location = getString(getColumnIndexOrThrow("location"))

                producers.add(
                    Producer(
                        id = id,
                        name = name,
                        description = description,
                        imageUrl = imageUrl ?: "",
                        history = history ?: "",
                        location = location ?: ""
                    )
                )
            }
        }
        cursor.close()
        db.close()
        return producers
    }

    fun getProducerById(producerId: String): Producer? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            BuenOrigenOpenHelper.TABLE_PRODUCER_REGISTRY,
            null,
            "id = ?",
            arrayOf(producerId),
            null, null, null
        )
        var producer: Producer? = null
        with(cursor) {
            if (moveToFirst()) {
                val id = getString(getColumnIndexOrThrow("id"))
                val name = getString(getColumnIndexOrThrow("name"))
                val description = getString(getColumnIndexOrThrow("description"))
                val imageUrl = getString(getColumnIndexOrThrow("imageUrl"))
                val history = getString(getColumnIndexOrThrow("history"))
                val location = getString(getColumnIndexOrThrow("location"))

                producer = Producer(
                    id = id,
                    name = name,
                    description = description,
                    imageUrl = imageUrl ?: "",
                    history = history ?: "",
                    location = location ?: ""
                )
            }
        }
        cursor.close()
        db.close()
        return producer
    }
}
