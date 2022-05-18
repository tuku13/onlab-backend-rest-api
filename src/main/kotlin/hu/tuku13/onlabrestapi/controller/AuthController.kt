package hu.tuku13.onlabrestapi.controller

import hu.tuku13.onlabrestapi.service.AuthService
import hu.tuku13.onlabrestapi.Constants.MIN_PASSWORD_LENGTH
import hu.tuku13.onlabrestapi.Constants.MIN_USERNAME_LENGTH
import hu.tuku13.onlabrestapi.dto.LoginForm
import hu.tuku13.onlabrestapi.dto.RegistrationForm
import hu.tuku13.onlabrestapi.dto.Token
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController {
    @Autowired
    private lateinit var authService: AuthService

    @PostMapping("/login")
    fun login(
        @RequestBody form: LoginForm
    ) : ResponseEntity<Token> {
        val token = authService.login(form.username, form.password)
            ?: return ResponseEntity(HttpStatus.UNAUTHORIZED)

        return ResponseEntity.ok(token)
    }

    @PostMapping("/register")
    fun register(
        @RequestBody form: RegistrationForm)
    : ResponseEntity<Boolean> {
        require(form.password == form.confirmPassword)
        require(form.password.length >= MIN_PASSWORD_LENGTH)
        require(form.username.length >= MIN_USERNAME_LENGTH)

        val hashedPassword = form.password

        val registrationResult = authService.register(
            username = form.username,
            email = form.emailAddress,
            password = hashedPassword
        )

        return ResponseEntity.ok(registrationResult)
    }
}