package org.example.project

import Greeting
import SERVER_PORT
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import entity.BaseResponse
import entity.LoginRequest
import entity.RegisterRequest
import entity.community.CommunityContent
import entity.community.NewContent
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.logging.*
import org.example.project.db.DatabaseSingleton
import org.example.project.entity.Tokens
import org.example.project.service.AccountService
import org.example.project.service.CommunityService
import org.example.project.util.audience
import org.example.project.util.issuer
import org.example.project.util.myRealm
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

const val secretKey = "114514"
fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    DatabaseSingleton.init()
    install(ContentNegotiation){
        json()
    }
    install(Authentication) {
        jwt("auth-jwt") {
            realm = myRealm
            verifier(
                JWT.require(Algorithm.HMAC256(secretKey))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential ->
                val uid = credential.payload.getClaim("uid").asInt()
                val haveValidToken = !Tokens.select{ Tokens.uid eq uid }
                    .empty()
                if (uid != null && haveValidToken){
                    JWTPrincipal(credential.payload)
                }else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(BaseResponse<String>(401, "Unauthorized", "未授权"))
            }
        }
    }
    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
        post("/account/register"){
            val registerRequest = call.receive<RegisterRequest>()
            val result = AccountService.register(registerRequest)
            call.respond(createResponse(result))
        }
        post("/account/login"){
            val loginRequest = call.receive<LoginRequest>()
            val token = AccountService.login(loginRequest)
            call.respond(createResponse(token))
        }
        get("/community/content"){
            val keyword = call.parameters["keyword"]?:""

        }
        authenticate("auth-jwt") {
            post("/community/content") {
                val token = call.principal<JWTPrincipal>()
                if (token == null) {
                    call.respond(BaseResponse<String>(401, "Unauthorized", "未授权"))
                    return@post
                }
                val uid = token.payload.getClaim("uid").asInt()
                val content = call.receive<NewContent>()
                val result = CommunityService.addCommunityContent(content, uid);
                call.respond(createResponse(result));
            }
        }
    }
}
inline fun <reified T> createResponse(result: Result<T>) : BaseResponse<T> {
    if(result.isSuccess){
        return BaseResponse(200, "success", result.getOrNull())
    }else {
        val throwable = result.exceptionOrNull()
        return BaseResponse(500, throwable?.localizedMessage?:throwable?.message?:throwable.toString(), null)
    }
}
