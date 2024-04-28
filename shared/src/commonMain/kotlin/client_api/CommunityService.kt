package client_api

import BASE_URL
import entity.community.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import utils.get

class CommunityService(private val httpClient: HttpClient) {
    suspend fun postContent(newContent: NewContent): Result<Unit>{
        return httpClient.post{
            contentType(ContentType.Application.Json)
            url("$BASE_URL/community/content")
            setBody(newContent)
        }.get()
    }

    suspend fun getAllContent(): Result<List<CommunityInfo>>{
        return httpClient.get {
            contentType(ContentType.Application.Json)
            url("$BASE_URL/community/content")
        }.get()
    }
    suspend fun getContentDetail(cid: Int): Result<CommunityDetail>{
        return httpClient.get {
            contentType(ContentType.Application.Json)
            parameter("cid", cid)
            url("$BASE_URL/community/detail")
        }.get()
    }
    suspend fun sendComment(newComment: NewComment): Result<Unit>{
        return httpClient.post {
            url("$BASE_URL/community/comment")
            contentType(ContentType.Application.Json)
            setBody(newComment)
        }.get()
    }
}