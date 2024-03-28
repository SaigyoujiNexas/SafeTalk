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
    val solved: Boolean = false,
    var images: List<String> = emptyList(),
    var comments: List<Comment> = emptyList(),
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
