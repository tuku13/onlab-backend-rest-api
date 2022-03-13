package hu.tuku13.onlabrestapi.model

import javax.persistence.*

@Entity
@Table(name = "subscriptions")
data class Subscription(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    val id: Long = 0L,

    val userId: Long,

    val groupId: Long
)
