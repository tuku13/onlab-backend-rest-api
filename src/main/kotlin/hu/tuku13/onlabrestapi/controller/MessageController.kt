package hu.tuku13.onlabrestapi.controller

import hu.tuku13.onlabrestapi.dto.Contact
import hu.tuku13.onlabrestapi.dto.GetMessageForm
import hu.tuku13.onlabrestapi.dto.LikeForm
import hu.tuku13.onlabrestapi.dto.MessageForm
import hu.tuku13.onlabrestapi.model.Message
import hu.tuku13.onlabrestapi.model.User
import hu.tuku13.onlabrestapi.model.partnerId
import hu.tuku13.onlabrestapi.repository.MessageRepository
import hu.tuku13.onlabrestapi.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
class MessageController {
    @Autowired
    private lateinit var messageRepository: MessageRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @PostMapping("/messages/send")
    fun sendMessage(
        @RequestBody form: MessageForm
    ): ResponseEntity<Long> {
        val senderId = SecurityContextHolder.getContext().authentication.principal as Long
        val recipient = userRepository.getUserByName(form.recipientName)
        val sender = userRepository.findById(senderId)

        if (!recipient.isPresent || !sender.isPresent) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        if(recipient.get().id == sender.get().id) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }

        val message = messageRepository.save(
            Message(
                timestamp = Instant.now().toEpochMilli(),
                text = form.text,
                senderId = sender.get().id,
                recipientId = recipient.get().id
            )
        )

        return ResponseEntity.ok(message.id)
    }

    @PostMapping("/contacts")
    fun listContacts(
        @RequestParam("user-id") userId: Long
    ): ResponseEntity<List<Contact>> {
        val messages = messageRepository.getMessagesBySenderIdOrRecipientId(userId, userId)
        if(messages.isEmpty()) {
            return ResponseEntity.ok(emptyList())
        }

        val partnerIds = messages.map { it.partnerId(userId) }.toSet()

        val users = mutableListOf<User>()
        partnerIds.forEach { users.add(userRepository.getById(it)) }

        val groupedMessages = messages.groupBy { it.partnerId(userId) }

        val contacts = mutableListOf<Contact>()
        groupedMessages.forEach { (partnerId, partnerMessages) ->
            val sortedMessages = partnerMessages.sortedByDescending { it.timestamp }
            contacts.add(Contact(
                userId = partnerId,
                profileImageUrl = users.find { it.id == partnerId }?.profileImage ?: "",
                name = users.find { it.id == partnerId }?.name ?: "",
                lastMessage = sortedMessages[0].text,
                timestamp = sortedMessages[0].timestamp
            ))
        }

        return ResponseEntity.ok(contacts)
    }

    @PostMapping("/messages")
    fun listMessages(
        @RequestBody form: GetMessageForm
    ): ResponseEntity<List<Message>> {
        val messages = mutableListOf<Message>()

        val sentMessages = messageRepository.getMessagesBySenderIdAndRecipientId(form.from, form.to)
        val receivedMessages = messageRepository.getMessagesBySenderIdAndRecipientId(form.to, form.from)

        messages += sentMessages
        messages += receivedMessages

        return ResponseEntity.ok(messages.sortedByDescending { it.timestamp })
    }
}