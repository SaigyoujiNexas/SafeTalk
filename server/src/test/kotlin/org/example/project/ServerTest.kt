package org.example.project

import entity.BaseResponse
import entity.LoginRequest
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ServerTest {
    @Test fun logIn(){
        testApplication {
            install(ContentNegotiation){
                json()
            }
            val response = client.post{
                url("http://localhost/account/login")
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(tel = "15691690591", passwd = "114514"))
            }
            val body = response.body<BaseResponse<String>>()
            assertEquals(body.code, 200);
        }
    }
    @Test fun uploadImage(){
    }
    private fun withServer(block: TestApplicationEngine.() -> Unit) {
    }
}