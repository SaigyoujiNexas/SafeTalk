package repository

import client_api.CollectionService
import entity.account.CurrentUser
import entity.collection.CollectionInfo
import entity.community.CommunityInfo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object CollectionRepository: KoinComponent {
    private val collectionService : CollectionService by inject()
    suspend fun getMyCollection(): Result<List<CollectionInfo>>{
        val uid = CurrentUser!!.uid
        return collectionService.getCollections(uid)
    }
}