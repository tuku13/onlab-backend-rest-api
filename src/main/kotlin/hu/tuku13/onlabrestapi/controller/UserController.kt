package hu.tuku13.onlabrestapi.controller

import hu.tuku13.onlabrestapi.model.User
import hu.tuku13.onlabrestapi.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @Autowired
    lateinit var userRepository: UserRepository

    @GetMapping("/users")
    fun getUsers() : ResponseEntity<List<User>> {
        var users = userRepository.findAll()
        return ResponseEntity.ok(users)
    }

    @GetMapping("/users/{id}")
    fun getUser(@PathVariable id: Long) : ResponseEntity<User> {
        val user = userRepository.findById(id)
        return ResponseEntity.ok(user.get())
    }
}