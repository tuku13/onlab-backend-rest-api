package hu.tuku13.onlabrestapi.controller

import hu.tuku13.onlabrestapi.dto.PostForm
import hu.tuku13.onlabrestapi.model.Post
import hu.tuku13.onlabrestapi.repository.GroupRepository
import hu.tuku13.onlabrestapi.repository.PostRepository
import org.apache.tomcat.jni.Time
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
class PostController {

    @Autowired
    private lateinit var groupRepository: GroupRepository

    @Autowired
    private lateinit var postRepository: PostRepository

    @GetMapping("groups/{group-id}/posts")
    fun getPosts(@PathVariable("group-id") groupId: Long): ResponseEntity<List<Post>> {
        val posts = postRepository.getPostByGroupId(groupId)

        return if (posts.isEmpty()) {
            ResponseEntity.ok(emptyList())
        } else {
            ResponseEntity.ok(posts)
        }
    }

    @GetMapping("/posts/{post-id}")
    fun getPost(@PathVariable("post-id") postId: Long): ResponseEntity<Post> {
        postRepository.getById(postId).let {
            return ResponseEntity.ok(it)
        }
    }

    @PostMapping("/groups/{group-id}/posts/new")
    fun createPost(
        @PathVariable("group-id") groupId: Long,
        @RequestBody form: PostForm
    ): ResponseEntity<Long> {
        val text = form.text ?: return ResponseEntity(HttpStatus.BAD_REQUEST)

        val group = groupRepository.getById(groupId)

        val post = postRepository.save(
            Post(
                groupId = group.id,
                timestamp = Instant.now().toEpochMilli(),
                text = text,
                userId = form.userId
            ).apply {
                form.imageUrl?.let { this.imageUrl = it }
            }
        )

        return ResponseEntity.ok(post.id)
    }

    @PutMapping("/posts/{post-id}/edit")
    fun editPost(
        @PathVariable("post-id") postId: Long,
        @RequestBody form: PostForm
    ): ResponseEntity<Long> {
        if (form.text === null && form.imageUrl == null) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val post = postRepository.getById(postId)

        post.apply {
            form.text?.let { text = it }
            form.imageUrl?.let { imageUrl = it }
        }

        postRepository.save(post)

        return ResponseEntity.ok(post.id)
    }

    @DeleteMapping("posts/{post-id}/delete")
    fun deletePost(
        @PathVariable("post-id") postId: Long,
        @RequestBody userId : Long
        ): ResponseEntity<Unit> {
        val post = postRepository.getById(postId)

        if(post.userId != userId) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        postRepository.delete(post)
        return ResponseEntity(HttpStatus.OK)
    }

    //TODO upload picture -> picture url : String
}