package entity
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val tel: String = "",
    val email: String = "",
    val passwd: String = "",
    val verifyCode: String = "",
)