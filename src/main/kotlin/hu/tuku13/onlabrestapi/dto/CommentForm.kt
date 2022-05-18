package hu.tuku13.onlabrestapi.dto

data class CommentForm(
    var text : String,
    val parentCommentId : Long?
)
