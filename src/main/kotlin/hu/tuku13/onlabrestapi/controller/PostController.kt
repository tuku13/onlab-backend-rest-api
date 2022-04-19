package hu.tuku13.onlabrestapi.controller

import hu.tuku13.onlabrestapi.dto.PostForm
import hu.tuku13.onlabrestapi.dto.PostDTO
import hu.tuku13.onlabrestapi.dto.UserForm
import hu.tuku13.onlabrestapi.model.Post
import hu.tuku13.onlabrestapi.repository.*
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

    @Autowired
    private lateinit var subscriptionRepository: SubscriptionRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var commentRepository: CommentRepository

    @Autowired
    private lateinit var likeRepository: LikeRepository

    @GetMapping("/groups/{group-id}/posts")
    fun getPosts(@PathVariable("group-id") groupId: Long): ResponseEntity<List<PostDTO>> {
        val posts = postRepository.getPostsByGroupId(groupId)
        val postDTOs = convertPostListToDTO(posts)
        return ResponseEntity.ok(postDTOs)
    }

    @GetMapping("/posts/{post-id}")
    fun getPost(@PathVariable("post-id") postId: Long): ResponseEntity<PostDTO> {
        postRepository.getById(postId).let {
            return ResponseEntity.ok(convertPostToDTO(it))
        }
    }

    @PostMapping("/groups/{group-id}/posts/new")
    fun createPost(
        @PathVariable("group-id") groupId: Long,
        @RequestBody form: PostForm
    ): ResponseEntity<Long> {
        val text = form.text ?: return ResponseEntity(HttpStatus.BAD_REQUEST)
        val title = form.title ?: return ResponseEntity(HttpStatus.BAD_REQUEST)

        val group = groupRepository.getById(groupId)

        val post = postRepository.save(
            Post(
                groupId = group.id,
                timestamp = Instant.now().toEpochMilli(),
                title = title,
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
        if (form.text == null && form.imageUrl == null && form.title == null) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val post = postRepository.getById(postId)

        post.apply {
            form.text?.let { text = it }
            form.title?.let { title = it }
            form.imageUrl?.let { imageUrl = it }
        }

        postRepository.save(post)

        return ResponseEntity.ok(post.id)
    }

    @DeleteMapping("/posts/{post-id}/delete")
    fun deletePost(
        @PathVariable("post-id") postId: Long,
        @RequestBody form: UserForm
    ): ResponseEntity<Unit> {
        val post = postRepository.getById(postId)

        if (post.userId != form.userId) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }

        postRepository.delete(post)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/users/{user-id}/posts")
    fun getUserPosts(@PathVariable("user-id") userId: Long): ResponseEntity<List<PostDTO>> {
        val posts = postRepository.getPostsByUserId(userId)

        val users = posts
            .map { it.userId }
            .toSet()
            .map { userRepository.getById(it) }

        val groups = posts
            .map { it.groupId }
            .toSet()
            .map { groupRepository.getById(it) }

        val headers = mutableListOf<PostDTO>()

        posts.forEach { post ->
            val like = likeRepository.findLikeByPostIdAndUserId(post.id, userId)

            val userLike = when {
                !like.isPresent -> 0
                like.get().value == 1 -> 1
                like.get().value == -1 -> -1
                else -> 0
            }

            headers += PostDTO(
                postId = post.id,
                userOpinion = userLike,
                userCommented = commentRepository.countByPostIdAndPostedBy(post.id, userId) > 0,
                comments = commentRepository.countByPostId(post.id),
                likes = likeRepository.getLikeByPostId(post.id).sumOf { it.value },
                groupImage = groups.find { it.id == post.groupId }?.groupImageUrl ?: "",
                groupName = groups.find { it.id == post.groupId }?.name ?: "No Group",
                title = post.title,
                text = post.text,
                postImage = post.imageUrl,
                groupId = groups.find { it.id == post.groupId }?.id ?: 0,
                userId = users.find { it.id == post.userId }?.id ?: 0,
                timestamp = post.timestamp
            )
        }

        return ResponseEntity.ok(headers)
    }

    @GetMapping("/posts/subscribed")
    fun getSubscribedGroupPosts(@RequestParam("user-id") userId: Long): ResponseEntity<List<PostDTO>> {
        val subscriptions = subscriptionRepository.getSubscriptionByUserId(userId)
        val posts = mutableListOf<Post>()
        subscriptions.forEach {
            posts += postRepository.getPostsByGroupId(it.groupId)
        }

        val postDTOs = convertPostListToDTO(posts)

        return ResponseEntity.ok(postDTOs)
    }

    private fun convertPostListToDTO(posts: List<Post>): List<PostDTO> {
        val users = posts
            .map { it.userId }
            .toSet()
            .map { userRepository.getById(it) }

        val groups = posts
            .map { it.groupId }
            .toSet()
            .map { groupRepository.getById(it) }

        val postDTOS = mutableListOf<PostDTO>()

        posts.forEach { post ->
            val like = likeRepository.findLikeByPostIdAndUserId(post.id, post.userId)

            val userLike = when {
                !like.isPresent -> 0
                like.get().value == 1 -> 1
                like.get().value == -1 -> -1
                else -> 0
            }

            postDTOS += PostDTO(
                postId = post.id,
                userOpinion = userLike,
                userCommented = commentRepository.countByPostIdAndPostedBy(post.id, post.userId) > 0,
                comments = commentRepository.countByPostId(post.id),
                likes = likeRepository.getLikeByPostId(post.id).sumOf { it.value },
                groupImage = groups.find { it.id == post.groupId }?.groupImageUrl ?: "",
                groupName = groups.find { it.id == post.groupId }?.name ?: "No Group",
                title = post.title,
                text = post.text,
                postImage = post.imageUrl,
                groupId = groups.find { it.id == post.groupId }?.id ?: 0,
                userId = users.find { it.id == post.userId }?.id ?: 0,
                timestamp = post.timestamp
            )
        }

        return postDTOS
    }

    private fun convertPostToDTO(post: Post): PostDTO {
        val group = groupRepository.getById(post.userId)

        val like = likeRepository.findLikeByPostIdAndUserId(post.id, post.userId)
        val userLike = when {
            !like.isPresent -> 0
            like.get().value == 1 -> 1
            like.get().value == -1 -> -1
            else -> 0
        }

        return PostDTO(
            postId = post.id,
            userOpinion = userLike,
            userCommented = commentRepository.countByPostIdAndPostedBy(post.id, post.userId) > 0,
            comments = commentRepository.countByPostId(post.id),
            likes = likeRepository.getLikeByPostId(post.id).sumOf { it.value },
            groupImage = group.groupImageUrl,
            groupName = group.name,
            title = post.title,
            text = post.text,
            postImage = post.imageUrl,
            groupId = post.groupId,
            userId = post.userId,
            timestamp = post.timestamp
        )
    }

    //TODO upload picture -> picture url : String
}
