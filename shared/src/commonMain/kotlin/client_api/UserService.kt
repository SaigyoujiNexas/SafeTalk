package client_api

import BASE_URL
import entity.account.User
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import utils.get

class UserService(val httpClient: HttpClient) {
    suspend fun getCurrentUserInfo(token: String): Result<User>{
        val domain = "$BASE_URL/user/current_info"
        return httpClient.get {
            url(domain)
            contentType(ContentType.Application.Json)
        }.get<User>()
    }
}