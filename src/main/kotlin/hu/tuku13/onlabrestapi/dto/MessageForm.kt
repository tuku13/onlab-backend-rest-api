package hu.tuku13.onlabrestapi.dto

data class MessageForm(
    val senderId: Long, // sender user id
    val recipientName: String, // recipient's name
    val text: String
)
