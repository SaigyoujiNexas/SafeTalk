package entity.community

import NO_ID
import entity.account.User
import kotlinx.serialization.Serializable

@Serializable
data class CommunityDetail(
    val id: Int = NO_ID,
    val user: User,
    val title: String,
    val content: String,
    val date: String,
    val solved: Boolean = false,
    val deleted: Boolean = false,
    val deleteReason: String = "",
    var images: List<String> = emptyList(),
    var comments: List<Comment> = emptyList(),
)
@Serializable
data class CommunityInfo(
    val id: Int = NO_ID,
    val userName: String,
    val userAvatar: String,
    val time: String,
    val title: String,
    val solved: Boolean = false,
    val isCollected: Boolean = false,
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
