package hu.tuku13.onlabrestapi.dto

data class RegistrationForm(
    val username: String,
    val emailAddress: String,
    val password: String,
    val confirmPassword: String
)
