package entity.community

import coil3.Image
import entity.User
import kotlinx.serialization.Serializable

@Serializable
data class CommunityContent(
    val id: Int = -1,
    val user: User? = null,
    val title: String,
    val content: String,
    val date: String,
    val solved: Boolean,
    val images: List<String>,
    val comments: List<Comment>,
)
@Serializable
data class Comment(
    val id: Int,
    val user: User,
    val content: String,
    val images: List<String>,
    val date: String,
    val isBestAnswer: Boolean,

)
