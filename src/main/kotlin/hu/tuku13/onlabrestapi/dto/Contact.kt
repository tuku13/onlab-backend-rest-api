package hu.tuku13.onlabrestapi.dto

data class Contact(
    val userId: Long,
    val name: String,
    val profileImageUrl: String,
    val lastMessage: String,
    val timestamp: Long
)
