package hu.tuku13.onlabrestapi.controller

import hu.tuku13.onlabrestapi.model.Subscription
import hu.tuku13.onlabrestapi.repository.SubscriptionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
class SubscriptionController {

    @Autowired
    private lateinit var subscriptionRepository: SubscriptionRepository

    @GetMapping("/users/{user-id}/subscriptions")
    fun getSubscriptions(@PathVariable("user-id") userId: Long): ResponseEntity<List<Subscription>> {
        val subscriptions = subscriptionRepository.getSubscriptionByUserId(userId)
        return ResponseEntity.ok(subscriptions)
    }

    @GetMapping("/groups/{group-id}/subscription")
    fun isUserAlreadySubscribed(
        @PathVariable("group-id") groupId: Long,
        @RequestParam userId: Long
    ): ResponseEntity<Boolean> {
        val subscription = subscriptionRepository.findSubscriptionByUserIdAndGroupId(userId, groupId)

        return if(!subscription.isPresent) {
            ResponseEntity.ok(false)
        } else {
            ResponseEntity.ok(true)
        }
    }

    @PostMapping("/groups/{group-id}/subscribe")
    fun subscribe(
        @PathVariable("group-id") groupId: Long,
    ): ResponseEntity<Unit> {
        val userId = SecurityContextHolder.getContext().authentication.principal as Long
        val subscription = subscriptionRepository.findSubscriptionByUserIdAndGroupId(userId, groupId)

        if (!subscription.isPresent) {
            subscriptionRepository.save(
                Subscription(
                    groupId = groupId,
                    userId = userId
                )
            )
        }
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("/groups/{group-id}/unsubscribe")
    fun unsubscribe(
        @PathVariable("group-id") groupId: Long,
    ): ResponseEntity<Unit> {
        val userId = SecurityContextHolder.getContext().authentication.principal as Long
        val subscription = subscriptionRepository.findSubscriptionByUserIdAndGroupId(userId, groupId)

        if (subscription.isPresent) {
            subscriptionRepository.delete(subscription.get())
        }

        return ResponseEntity(HttpStatus.OK)
    }
}