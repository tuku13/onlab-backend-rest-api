package hu.tuku13.onlabrestapi.repository

import hu.tuku13.onlabrestapi.model.Message
import org.springframework.data.jpa.repository.JpaRepository

interface MessageRepository : JpaRepository<Message, Long> {
    fun getMessagesBySenderIdAndRecipientId(senderId: Long, recipientId: Long) : List<Message>
    fun getMessagesBySenderIdOrRecipientId(userId: Long, userId2: Long) : List<Message>
}