package hu.tuku13.onlabrestapi.repository

import hu.tuku13.onlabrestapi.model.Like
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface LikeRepository : JpaRepository<Like, Long> {

    fun getLikeByPostId(postId: Long): List<Like>

    fun getLikesByUserId(userId: Long): List<Like>

    fun findLikeByPostIdAndUserId(postId: Long, userId: Long): Optional<Like>

    fun findLikesByCommentIdAndUserId(commentId: Long, userId: Long) : Optional<Like>
}