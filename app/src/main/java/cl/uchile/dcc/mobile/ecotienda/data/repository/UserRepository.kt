package cl.uchile.dcc.mobile.ecotienda.data.repository

import cl.uchile.dcc.mobile.ecotienda.data.database.User
import cl.uchile.dcc.mobile.ecotienda.data.database.UserDAO

class UserRepository(
    private val userDao: UserDAO
) {
    suspend fun registerUser(user: User): Boolean {
        if (userDao.emailExists(user.email)) return false
        return userDao.addUser(user) != -1L
    }

    suspend fun loginUser(email: String, pass: String): User? {
        return userDao.loginUser(email, pass)
    }
}
