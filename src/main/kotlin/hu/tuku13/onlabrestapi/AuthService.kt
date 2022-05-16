package hu.tuku13.onlabrestapi

import hu.tuku13.onlabrestapi.dto.Token
import hu.tuku13.onlabrestapi.model.Account
import hu.tuku13.onlabrestapi.model.User
import hu.tuku13.onlabrestapi.repository.AccountRepository
import hu.tuku13.onlabrestapi.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService {
    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    private val authenticatedUsers = mutableMapOf<String, Long>()

    fun login(username: String, password: String): Token? {
        val user = userRepository.getUserByName(username)
        val account = accountRepository.getAccountsByUserId(user.get().id)

        val hashedPassword = password

        if (account.hashedPassword != hashedPassword) {
            return null
        }

        val token = UUID.randomUUID().toString()
        authenticatedUsers[token] = account.userId

        return Token(
            token = token,
            userId = account.userId
        )
    }

    fun register(username: String, password: String, email: String): Boolean {
        return try {
            val user = userRepository.save(
                User(
                    name = username
                )
            )

            accountRepository.save(
                Account(
                    emailAddress = email,
                    userId = user.id,
                    hashedPassword = password
                )
            )

            true
        } catch (exc: Exception) {
            false
        }
    }

    fun logout(token: String) {
        authenticatedUsers.remove(token)
    }

    fun isAuthenticated(token: String) = authenticatedUsers.containsKey(token)

    fun hasPermission(token: String, userId: Long): Boolean {
        val id = authenticatedUsers[token]
            ?: return false

        return id == userId
    }

}