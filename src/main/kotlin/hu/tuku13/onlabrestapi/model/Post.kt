package hu.tuku13.onlabrestapi.model

import javax.persistence.*

@Entity
@Table(name = "posts")
data class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    val id: Long = 0L,

    var timestamp: Long = 0L,

    var title: String,

    var text: String,

    val groupId: Long,

    val userId: Long,

    @Column(name = "image")
    var imageUrl: String = ""
)
