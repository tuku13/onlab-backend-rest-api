package hu.tuku13.onlabrestapi.model

import javax.persistence.*

@Entity
@Table(name = "likes")
data class Like(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    val id: Long = 0L,

    var value: Int,

    val userId: Long,

    val postId: Long? = null,

    val commentId: Long? = null
)
