package hu.tuku13.onlabrestapi.dto

data class GroupForm(
    val userId: Long,
    val groupName: String?,
    val description: String?
)
