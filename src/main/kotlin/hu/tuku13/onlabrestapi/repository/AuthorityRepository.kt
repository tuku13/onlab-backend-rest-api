package hu.tuku13.onlabrestapi.repository

import hu.tuku13.onlabrestapi.model.Authority
import org.springframework.data.jpa.repository.JpaRepository

interface AuthorityRepository : JpaRepository<Authority, Long> {
}