package hu.tuku13.onlabrestapi.controller

import hu.tuku13.onlabrestapi.dto.UserForm
import hu.tuku13.onlabrestapi.model.Subscription
import hu.tuku13.onlabrestapi.repository.SubscriptionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

    @PostMapping("/groups/{group-id}/subscribe")
    fun subscribe(
        @PathVariable("group-id") groupId: Long,
        @RequestBody form: UserForm
    ): ResponseEntity<Unit> {
        val subscription = subscriptionRepository.findSubscriptionByUserIdAndGroupId(form.userId, groupId)

        if (subscription.isEmpty) {
            subscriptionRepository.save(
                Subscription(
                    groupId = groupId,
                    userId = form.userId
                )
            )
        }
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("/groups/{group-id}/unsubscribe")
    fun unsubscribe(
        @PathVariable("group-id") groupId: Long,
        @RequestBody form: UserForm
    ): ResponseEntity<Unit> {
        val subscription = subscriptionRepository.findSubscriptionByUserIdAndGroupId(form.userId, groupId)

        if (subscription.isPresent) {
            subscriptionRepository.delete(subscription.get())
        }

        return ResponseEntity(HttpStatus.OK)
    }
}