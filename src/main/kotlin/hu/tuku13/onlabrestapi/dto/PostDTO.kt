package hu.tuku13.onlabrestapi.dto

data class PostDTO(
    val postId: Long,
    val comments: Int,
    val likes: Int,
    val userOpinion: Int = 0,
    val userCommented: Boolean = false,
    val groupImage: String,
    val groupName: String,
    val title: String,
    val text: String,
    val postImage: String,
    val timestamp: Long,
    val groupId: Long,
    val userId: Long
)
