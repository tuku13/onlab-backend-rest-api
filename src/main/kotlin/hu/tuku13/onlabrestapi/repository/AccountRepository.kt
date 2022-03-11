package hu.tuku13.onlabrestapi.repository

import hu.tuku13.onlabrestapi.model.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


interface AccountRepository : JpaRepository<Account, Long> {
    fun getAccountsByUserId(id: Long) : Account
}