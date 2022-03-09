package hu.tuku13.onlabrestapi.repository

import hu.tuku13.onlabrestapi.model.Account
import org.springframework.data.jpa.repository.JpaRepository


interface AccountRepository : JpaRepository<Account, Long>