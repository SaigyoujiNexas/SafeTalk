package entity.community

import NO_ID
import coil3.Image
import entity.User
import io.ktor.util.date.*
import kotlinx.serialization.Serializable

@Serializable
data class CommunityContent(
    val id: Int = NO_ID,
    val user: User,
    val title: String,
    val content: String,
    val date: String,
    val solved: Boolean = false,
    var images: List<String> = emptyList(),
    var comments: List<Comment> = emptyList(),
)
@Serializable
data class Comment(
    val id: Int = NO_ID,
    val user: User,
    val content: String,
    val communityId: Int,
    val fatherCommentId: Int = NO_ID,
    val images: List<String> = emptyList(),
    val date: String = "",
    val isBestAnswer: Boolean = false,
    val comments: List<Comment> = emptyList()
)
