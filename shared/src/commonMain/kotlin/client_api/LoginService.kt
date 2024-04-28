package client_api

import BASE_URL
import entity.LoginRequest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import utils.get

class LoginService(private val httpClient: HttpClient) {
    suspend fun login(tel: String = "", email: String = "", verifyCode: String = "", password: String = ""): Result<String> {
        if(tel.isEmpty() && email.isEmpty()) {
            return Result.failure(IllegalArgumentException("tel or email must be provided"))
        }
        if(verifyCode.isEmpty() && password.isEmpty()) {
            return Result.failure(IllegalArgumentException("verifyCode or password must be provided"))
        }
        val loginRequest = LoginRequest(tel, email, password, verifyCode)
        httpClient.post {
            contentType(ContentType.Application.Json)
            url("$BASE_URL/account/login")
            setBody(loginRequest)
        }.get<String?>()
            .onSuccess {
                return if(!it.isNullOrEmpty()) Result.success(it) else Result.failure(NoSuchElementException("no token"))
            }.onFailure {
                return Result.failure(it)
            }
        return Result.failure(IllegalArgumentException("can not touch here"))
    } }