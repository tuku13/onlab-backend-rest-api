package hu.tuku13.onlabrestapi.repository

import hu.tuku13.onlabrestapi.model.Group
import org.springframework.data.jpa.repository.JpaRepository

interface GroupRepository : JpaRepository<Group, Long>