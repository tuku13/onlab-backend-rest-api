package hu.tuku13.onlabrestapi.model

import javax.persistence.*

@Entity
@Table(name = "messages")
data class Message(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    val id: Long = 0L,

    val timestamp: Long,

    val text: String,

    @Column(name = "from_id")
    val senderId: Long,

    @Column(name = "to_id")
    val recipientId: Long,
)

fun Message.partnerId(userId: Long): Long = if (senderId == userId) recipientId else senderId