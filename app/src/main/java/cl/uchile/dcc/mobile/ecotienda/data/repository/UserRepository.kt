package cl.uchile.dcc.mobile.ecotienda.data.repository

import cl.uchile.dcc.mobile.ecotienda.data.database.User
import cl.uchile.dcc.mobile.ecotienda.data.database.UserDAO
import cl.uchile.dcc.mobile.ecotienda.data.network.ApiRepository
import cl.uchile.dcc.mobile.ecotienda.data.network.UserApiService
import cl.uchile.dcc.mobile.ecotienda.data.network.dto.UserDto
import cl.uchile.dcc.mobile.ecotienda.data.network.dto.toEntity

class UserRepository(
    private val userDao: UserDAO,
    private val apiService: UserApiService = ApiRepository.userApiService
) {
    suspend fun registerUser(user: User): Boolean {
        if (userDao.emailExists(user.email)) return false
        val localSuccess = userDao.addUser(user) != -1L

        try {
            val parts = user.name.split(" ", limit = 2)
            val firstName = parts.getOrNull(0) ?: user.name
            val lastName = parts.getOrNull(1) ?: ""

            val dto = UserDto(
                id = user.id.toString(),
                username = user.email.substringBefore("@"),
                email = user.email,
                firstName = firstName,
                lastName = lastName,
                profileImageUrl = "",
                password = user.password
            )
            apiService.createUser(dto)
        } catch (_: Exception) {
            // Se guardó en Room incluso si la API falla
        }

        return localSuccess
    }

    suspend fun loginUser(email: String, pass: String): User? {
        // Intentar autenticar comprobando API
        try {
            val users = apiService.getUsers()
            val match = users.find { it.email.equals(email, ignoreCase = true) && it.password == pass }
            if (match != null) {
                val entity = match.toEntity()
                if (!userDao.emailExists(entity.email)) {
                    userDao.addUser(entity)
                }
                return entity
            }
        } catch (_: Exception) {
            // Caer a Room si falla la red
        }

        return userDao.loginUser(email, pass)
    }
}
