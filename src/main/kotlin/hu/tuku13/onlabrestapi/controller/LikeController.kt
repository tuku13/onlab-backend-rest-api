package hu.tuku13.onlabrestapi.controller

import hu.tuku13.onlabrestapi.dto.LikeForm
import hu.tuku13.onlabrestapi.model.Like
import hu.tuku13.onlabrestapi.repository.CommentRepository
import hu.tuku13.onlabrestapi.repository.LikeRepository
import hu.tuku13.onlabrestapi.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
class LikeController {
    @Autowired
    private lateinit var likeRepository: LikeRepository

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var commentRepository: CommentRepository

    @GetMapping("/posts/{post-id}/likes")
    fun getLikes(@PathVariable("post-id") postId: Long): ResponseEntity<List<Like>> {
        val likes = likeRepository.getLikeByPostId(postId)
        return ResponseEntity.ok(likes)
    }

    @GetMapping("/posts/{post-id}/likes/count")
    fun getLikeSum(@PathVariable("post-id") postId: Long): ResponseEntity<Int> {
        val likes = likeRepository.getLikeByPostId(postId)

        return if (likes.isEmpty()) {
            ResponseEntity.ok(0)
        } else {
            val sum = likes.sumOf { it.value }
            ResponseEntity.ok(sum)
        }
    }

    @GetMapping("/likes/{like-id}")
    fun getLike(@PathVariable("like-id") likeId: Long): ResponseEntity<Like> {
        val like = likeRepository.findById(likeId)

        return if (!like.isPresent) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity.ok(like.get())
        }
    }

    @GetMapping("/users/{user-id}/likes")
    fun getUserLikes(@PathVariable("user-id") userId: Long): ResponseEntity<List<Like>> {
        val likes = likeRepository.getLikesByUserId(userId)
        return ResponseEntity.ok(likes)
    }

    @PostMapping("/posts/{post-id}/like")
    fun createLikeOnPost(
        @PathVariable("post-id") postId: Long,
        @RequestBody form: LikeForm
    ) : ResponseEntity<Unit> {
        val userId = SecurityContextHolder.getContext().authentication.principal as Long
        val post = postRepository.findById(postId)

        if(!post.isPresent) {
            println("Post not found")
            return ResponseEntity(HttpStatus.OK)
        }

        when (form.value) {
            0 -> {
                val like = likeRepository.getLikeByPostIdAndUserId(postId, userId)
                return if(like == null) {
                    println("Like not found")
                    ResponseEntity(HttpStatus.OK)
                } else {
                    like.value = 0
                    likeRepository.save(like)
                    println("Like changed to 0")
                    return ResponseEntity(HttpStatus.OK)
                }
            }
            -1, 1 -> {
                val like = likeRepository.getLikeByPostIdAndUserId(postId, userId)

                return if(like != null) {
                    like.value = if (form.value == 1) 1 else -1
                    val savedLike = likeRepository.save(like)

                    println("Like changed to ${like.value}")
                    return ResponseEntity(HttpStatus.OK)
                } else {
                    val newLike = likeRepository.save(
                        Like(
                            value = form.value,
                            postId = postId,
                            userId = userId,
                        )
                    )
                    println("New like, value = ${newLike.value}")
                    return ResponseEntity(HttpStatus.OK)
                }
            }
            else -> {
                println("Bad request")
                return ResponseEntity(HttpStatus.BAD_REQUEST)
            }
        }
    }

    @PostMapping("/comments/{comment-id}/like")
    fun createLikeOnComment(
        @PathVariable("comment-id") commentId: Long,
        @RequestBody form: LikeForm
    ) : ResponseEntity<Long> {
        val userId = SecurityContextHolder.getContext().authentication.principal as Long
        val comment = commentRepository.findById(commentId)

        if(!comment.isPresent) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        when (form.value) {
            0 -> {
                val like = likeRepository.findLikesByCommentIdAndUserId(commentId, userId)
                return if(!like.isPresent) {
                    ResponseEntity(HttpStatus.NOT_FOUND)
                } else {
                    likeRepository.delete(like.get())
                    ResponseEntity(HttpStatus.OK)
                }
            }
            -1, 1 -> {
                val oldLike = likeRepository.findLikesByCommentIdAndUserId(commentId, userId)
                if(oldLike.isPresent) {
                    return ResponseEntity(HttpStatus.CONFLICT)
                }

                val like = likeRepository.save(
                    Like(
                        value = form.value,
                        commentId = commentId,
                        userId = userId,
                    )
                )
                return ResponseEntity.ok(like.id)
            }
            else -> {
                return ResponseEntity(HttpStatus.BAD_REQUEST)
            }
        }
    }

    @PutMapping("/likes/{like-id}/edit")
    fun editLike(
        @PathVariable("like-id") likeId: Long,
        @RequestBody form: LikeForm
    ) : ResponseEntity<Unit> {
        val oldLike = likeRepository.findById(likeId)

        if(!oldLike.isPresent) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        return when (form.value) {
            0 -> {
                likeRepository.deleteById(likeId)
                ResponseEntity(HttpStatus.OK)
            }
            -1, 1 -> {
                val like = likeRepository.getById(likeId)
                likeRepository.save(
                    like.apply {
                        value = form.value
                    }
                )
                ResponseEntity(HttpStatus.OK)
            }
            else -> {
                ResponseEntity(HttpStatus.BAD_REQUEST)
            }
        }
    }

    @DeleteMapping("/likes/{like-id}/delete")
    fun deleteLike(
        @PathVariable("like-id") likeId: Long,
    ) : ResponseEntity<Unit> {
        val userId = SecurityContextHolder.getContext().authentication.principal as Long
        val like = likeRepository.findById(likeId)

        if(!like.isPresent) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        like.get().let {
            if(it.userId != userId) {
                return ResponseEntity(HttpStatus.FORBIDDEN)
            }
            likeRepository.deleteById(likeId)
        }

        return ResponseEntity(HttpStatus.OK)
    }
}