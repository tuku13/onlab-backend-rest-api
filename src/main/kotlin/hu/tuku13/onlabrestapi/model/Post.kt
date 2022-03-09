package hu.tuku13.onlabrestapi.model

import javax.persistence.*

@Entity
@Table(name = "posts")
data class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    val id: Long = 0L,

    val timestamp: Long = 0L,

    val text: String = "",

    @Column(name = "image")
    val imageUrl: String = ""
)
