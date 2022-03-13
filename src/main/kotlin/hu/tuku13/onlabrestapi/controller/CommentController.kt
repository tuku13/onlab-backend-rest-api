package hu.tuku13.onlabrestapi.controller

import hu.tuku13.onlabrestapi.dto.CommentForm
import hu.tuku13.onlabrestapi.dto.UserForm
import hu.tuku13.onlabrestapi.model.Comment
import hu.tuku13.onlabrestapi.repository.CommentRepository
import hu.tuku13.onlabrestapi.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
class CommentController {

    @Autowired
    private lateinit var commentRepository: CommentRepository

    @Autowired
    private lateinit var postRepository: PostRepository

    @GetMapping("/posts/{post-id}/comments")
    fun getAllComments(@PathVariable("post-id") postId: Long): ResponseEntity<List<Comment>> {
        val comments = commentRepository.getCommentByPostId(postId)
        return ResponseEntity.ok(comments)
    }

    @GetMapping("/comments/{parent-comment-id}/children")
    fun getChildComments(@PathVariable("parent-comment-id") parentCommentId: Long): ResponseEntity<List<Comment>> {
        val comments = commentRepository.getCommentByParentCommentId(parentCommentId)
        return ResponseEntity.ok(comments)
    }

    @GetMapping("/comments/{comment-id}")
    fun getComment(@PathVariable("comment-id") commentId: Long): ResponseEntity<Comment> {
        val comment = commentRepository.findById(commentId)

        return if(comment.isEmpty) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity.ok(comment.get())
        }
    }

    @PostMapping("posts/{post-id}/comments/new")
    fun createComment(
        @PathVariable("post-id") postId: Long,
        @RequestBody form: CommentForm
    ): ResponseEntity<Long> {
        val postExist = postRepository.existsById(postId)

        if (!postExist) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val comment = commentRepository.save(
            Comment(
                text = form.text,
                timestamp = Instant.now().toEpochMilli(),
                parentCommentId = form.parentCommentId,
                postedBy = form.userId,
                postId = postId
            )
        )

        return ResponseEntity.ok(comment.id)
    }

    @PutMapping("comments/{comment-id}/edit")
    fun editComment(
        @PathVariable("comment-id") commentId: Long,
        @RequestBody form: CommentForm
    ) : ResponseEntity<Unit> {
        val comment = commentRepository.getById(commentId)

        commentRepository.save(
            comment.apply { text = form.text }
        )

        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("comments/{comment-id}/delete")
    fun deleteComment(
        @PathVariable("comment-id") commentId: Long,
        @RequestBody form: UserForm
    ) : ResponseEntity<Unit> {
        val comment = commentRepository.getById(commentId)

        if(comment.postedBy != form.userId) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        commentRepository.delete(comment)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("posts/{post-id}/comments/count")
    fun countComments(@PathVariable("post-id") postId: Long) : ResponseEntity<Int> {
        return  ResponseEntity.ok(
            postRepository.countById(postId)
        )
    }
}