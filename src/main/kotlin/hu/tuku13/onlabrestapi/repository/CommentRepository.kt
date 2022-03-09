package hu.tuku13.onlabrestapi.repository

import hu.tuku13.onlabrestapi.model.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long>