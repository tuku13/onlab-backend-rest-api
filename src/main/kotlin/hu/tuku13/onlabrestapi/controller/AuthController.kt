package hu.tuku13.onlabrestapi.controller

import hu.tuku13.onlabrestapi.Constants.MIN_PASSWORD_LENGTH
import hu.tuku13.onlabrestapi.Constants.MIN_USERNAME_LENGTH
import hu.tuku13.onlabrestapi.dto.RegistrationForm
import hu.tuku13.onlabrestapi.model.Account
import hu.tuku13.onlabrestapi.model.User
import hu.tuku13.onlabrestapi.repository.AccountRepository
import hu.tuku13.onlabrestapi.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController {

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @PostMapping("/register")
    fun register(@RequestBody form: RegistrationForm) : ResponseEntity<Unit> {
        require(form.password == form.confirmPassword)
        require(form.password.length >= MIN_PASSWORD_LENGTH)
        require(form.username.length >= MIN_USERNAME_LENGTH)

        return try {
            val user = userRepository.save(User(
                name = form.username
            ))

            accountRepository.save(Account(
                emailAddress = form.emailAddress,
                userId = user.id,
                hashedPassword = form.password
            ))
            ResponseEntity(HttpStatus.CREATED)
        } catch(exc: Exception) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}