package entity.community

import NO_ID
import kotlinx.serialization.Serializable

@Serializable
data class NewContent(
    val title: String = "",
    val content: String = "",
    val images: List<ByteArray> = emptyList(),
    var uid: Long = NO_ID.toLong(),
)
@Serializable
data class NewComment(
    val content: String,
    val communityId: Int,
    val fatherCommentId: Int = NO_ID,
    val images: List<ByteArray> = emptyList(),
)
