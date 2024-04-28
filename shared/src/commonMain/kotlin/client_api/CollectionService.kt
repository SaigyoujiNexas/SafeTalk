package client_api

import BASE_URL
import entity.collection.CollectionInfo
import entity.community.CommunityInfo
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import utils.get

class CollectionService(private val httpClient: HttpClient) {
    suspend fun addCollection(cid: Int):Result<Unit>{
        return httpClient.post{
            contentType(ContentType.Application.Json)
            url("$BASE_URL/collection")
            parameter("cid", cid)
        }.get()
    }
    suspend fun removeCollection(cid: Int):Result<Unit>{
        return httpClient.delete{
            contentType(ContentType.Application.Json)
            url("$BASE_URL/collection")
            parameter("cid", cid)
        }.get()
    }
    suspend fun getCollections(uid: Int):Result<List<CollectionInfo>>{
        return httpClient.get{
            contentType(ContentType.Application.Json)
            url("$BASE_URL/collection")
            parameter("uid", uid)
        }.get()
    }
}