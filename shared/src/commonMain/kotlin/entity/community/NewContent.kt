package entity.community

import kotlinx.serialization.Serializable

@Serializable
data class NewContent(
    val title: String = "",
    val content: String = "",
    val images: List<ByteArray> = emptyList(),
    var uid: Long = -1,
)
