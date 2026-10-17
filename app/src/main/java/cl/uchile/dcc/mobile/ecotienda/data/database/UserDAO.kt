package cl.uchile.dcc.mobile.ecotienda.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDAO {

    // CREATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User): Long

    // READ
    @Query("SELECT * FROM user_registry")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM user_registry WHERE id = :id LIMIT 1")
    suspend fun getOneUser(id: Long): User?

    @Query("SELECT * FROM user_registry WHERE LOWER(email) = LOWER(:email) AND password = :pass LIMIT 1")
    suspend fun loginUser(email: String, pass: String): User?

    // Registrar el email una sola vez
    @Query("SELECT EXISTS(SELECT 1 FROM user_registry WHERE LOWER(email) = LOWER(:email))")
    suspend fun emailExists(email: String): Boolean


    // Update
    @Update
    suspend fun updateUser(user: User)

    // Delete
    @Delete
    suspend fun deleteUser(user: User)
}
