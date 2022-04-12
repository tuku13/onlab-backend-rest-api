package hu.tuku13.onlabrestapi.repository

import hu.tuku13.onlabrestapi.model.Subscription
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SubscriptionRepository : JpaRepository<Subscription, Long> {

    fun findSubscriptionByUserIdAndGroupId(userId: Long, groupId: Long): Optional<Subscription>
    fun getSubscriptionByUserId(userId: Long) : List<Subscription>
    fun countByGroupId(groupId: Long): Int
}