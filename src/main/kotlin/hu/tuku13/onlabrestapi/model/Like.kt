package hu.tuku13.onlabrestapi.model

import javax.persistence.*

@Entity
@Table(name = "likes")
data class Like(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    val id: Long = 0L,

    val value: Int = 0,

    val userId: Long = 0L,

    val postId: Long = 0L,

    val commentId: Long = 0L
)
