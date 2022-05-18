package hu.tuku13.onlabrestapi.service

import hu.tuku13.onlabrestapi.dto.Token
import hu.tuku13.onlabrestapi.model.Account
import hu.tuku13.onlabrestapi.model.User
import hu.tuku13.onlabrestapi.repository.AccountRepository
import hu.tuku13.onlabrestapi.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService {
    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var jwtService: JwtService

    private val authenticatedUsers = mutableMapOf<String, Long>()

    fun login(username: String, password: String): Token? {
        println("login -----------")
        try {
            println("nnnnnnnnnnnnnn")
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(username, password)
            )
        } catch (e: BadCredentialsException) {
            println("asdasdasdasdasd")
            throw Exception("Incorrect username or password", e)
        }

        val user = userRepository.getUserByName(username)
        val token = jwtService.createToken(user.get().name, user.get().id)

        println(token)

        return Token(
            token = token,
            userId = user.get().id
        )
    }

    fun register(username: String, password: String, email: String): Boolean {
        return try {
            val user = userRepository.save(
                User(
                    name = username
                )
            )

            // TODO uj sql tablaba is kell szurni adatokat

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