package hu.tuku13.onlabrestapi.dto

data class PostForm(
    val userId: Long,
    var text: String?,
    var imageUrl: String?
)