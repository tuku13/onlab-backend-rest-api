package hu.tuku13.onlabrestapi.model

import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    val id: Long = 0L,

    val name: String = "",

    val bio: String = "",

    @Column(name = "profile_image")
    val profileImage: String = ""
)
