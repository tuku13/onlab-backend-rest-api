package hu.tuku13.onlabrestapi.model

import javax.persistence.*

@Entity
@Table(name = "accounts")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    val id: Long = 0L,

    val emailAddress: String = "",

    val hashedPassword: String = "",

    val userId: Long = 0L
)
