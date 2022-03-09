package hu.tuku13.onlabrestapi.repository

import hu.tuku13.onlabrestapi.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>