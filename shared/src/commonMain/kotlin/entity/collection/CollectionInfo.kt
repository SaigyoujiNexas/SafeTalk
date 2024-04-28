package entity.collection

import NO_ID
import entity.community.CommunityInfo
import kotlinx.serialization.Serializable

@Serializable
data class CollectionInfo(
    val id: Int = NO_ID,
    val contentInfo: CommunityInfo,
    val date: Long,
)