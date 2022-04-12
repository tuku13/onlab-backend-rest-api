package hu.tuku13.onlabrestapi.model

import javax.persistence.*

@Entity
@Table(name = "groups")
data class Group(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    val id: Long = 0L,

    @Column(name = "group_name")
    var name: String = "",

    var description: String = "",

    var groupImageUrl: String = "",

    @Column(name = "created_by_user_id")
    val createdBy: Long = 0L
)
