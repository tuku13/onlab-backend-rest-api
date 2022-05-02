package hu.tuku13.onlabrestapi.dto


data class GroupDTO(
    val id: Long,
    val name: String,
    val description: String,
    val groupImageUrl: String ,
    val createdBy: Long,
    val members: Int
)

