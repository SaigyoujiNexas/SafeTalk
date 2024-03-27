package entity

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String,
    val uid: Int,
    val email: String = "",
    val phone: String = "",
    val avatar: String = "https://s2.loli.net/2024/01/29/K9Pc4wosiGeWvlu.png",
)
