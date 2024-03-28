package client_api

import BASE_URL
import entity.community.CommunityContent
import entity.community.NewContent
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import utils.get

class CommunityService(val httpClient: HttpClient) {
    suspend fun postContent(newContent: NewContent): Result<Unit>{
        return httpClient.post{
            contentType(ContentType.Application.Json)
            url("$BASE_URL/community/content")
            setBody(newContent)
        }.get()
    }

    suspend fun getAllContent(): Result<List<CommunityContent>>{
        return httpClient.get {
            contentType(ContentType.Application.Json)
            url("$BASE_URL/community/content")
        }.get()
    }
}