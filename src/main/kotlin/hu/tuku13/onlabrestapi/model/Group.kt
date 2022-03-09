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
    val name: String = "",

    val description: String = "",

    val groupImageUrl: String = "",

    @Column(name = "created_by_user_id")
    val createdBy: Long = 0L

)
