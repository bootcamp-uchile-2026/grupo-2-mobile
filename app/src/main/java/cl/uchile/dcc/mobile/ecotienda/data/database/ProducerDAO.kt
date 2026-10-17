package cl.uchile.dcc.mobile.ecotienda.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProducerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProducer(producer: Producer): Long

    @Query("SELECT * FROM producer_registry")
    suspend fun getAllProducer(): List<Producer>

    @Query("SELECT * FROM producer_registry WHERE id = :id LIMIT 1")
    suspend fun getProducerById(id: String): Producer?

    @Update
    suspend fun updateProducer(producer: Producer)

    @Delete
    suspend fun deleteProducer(producer: Producer)
}
