package hu.tuku13.onlabrestapi.repository

import hu.tuku13.onlabrestapi.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Long> {

    fun getUserByName(name: String) : Optional<User>

}