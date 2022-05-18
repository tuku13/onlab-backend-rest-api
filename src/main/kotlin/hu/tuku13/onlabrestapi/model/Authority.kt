package hu.tuku13.onlabrestapi.model

import javax.persistence.*

@Entity
@Table(name = "authorities")
data class Authority(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    val id: Long = 0L,

    val username: String = "",

    val authority: String = "",
)
