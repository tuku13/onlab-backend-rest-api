package hu.tuku13.onlabrestapi.repository

import hu.tuku13.onlabrestapi.model.Like
import org.springframework.data.jpa.repository.JpaRepository

interface LikeRepository : JpaRepository<Like, Long>