package hu.tuku13.onlabrestapi.repository

import hu.tuku13.onlabrestapi.model.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {

    fun getPostsByGroupId(groupId: Long): List<Post>

    fun countById(postId: Long): Int

    fun getPostsByUserId(userId: Long): List<Post>

}