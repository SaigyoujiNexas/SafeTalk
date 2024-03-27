package entity

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val tel: String = "",
    val email: String = "",
    val verifyCode: String,
    val name: String,
    val passwd: String,
)