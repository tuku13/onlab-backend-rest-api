package hu.tuku13.onlabrestapi.model

import javax.persistence.*

@Entity
@Table(name = "comments")
data class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    val id: Long = 0L,

    val timestamp: Long,

    var text: String,

    val parentCommentId: Long?,

    val postId: Long,

    @Column(name = "posted_by_user_id")
    val postedBy: Long
)
