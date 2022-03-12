package hu.tuku13.onlabrestapi.dto

data class CommentForm(
    val userId: Long,
    var text : String,
    val parentCommentId : Long?
)
