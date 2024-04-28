package org.example.project

import Greeting
import SERVER_PORT
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import di.netWorkModule
import entity.BaseResponse
import entity.LoginRequest
import entity.RegisterRequest
import entity.community.NewComment
import entity.community.NewContent
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
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
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.builtins.ByteArraySerializer
import org.example.project.db.DatabaseSingleton
import org.example.project.entity.Tokens
import org.example.project.service.AccountService
import org.example.project.service.CollectionService
import org.example.project.service.CommunityService
import org.example.project.util.audience
import org.example.project.util.issuer
import org.example.project.util.myRealm
import org.example.project.util.secret
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.ktor.plugin.Koin

const val secretKey = "114514"
fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module).start(wait = true)
//    aSocket(SelectorManager(Dispatchers.IO))
}

fun Application.module() {
    DatabaseSingleton.init()
    install(ContentNegotiation){
        json()
    }
    install(Koin){
        modules(netWorkModule)
    }
    install(Authentication) {
        jwt("auth-jwt") {
            realm = myRealm
            verifier(JWT
                .require(Algorithm.HMAC256(secret))
                .withAudience(*audience)
                .withIssuer(issuer)
                .build())
            validate{ credential ->
                val uid = credential.payload.getClaim("uid").asInt()
                val haveValidToken = transaction {
                    !Tokens.select { Tokens.uid eq uid }.empty()
                }
                println("No validate success")
                if (uid != null && haveValidToken){
                    println("validate success")
                    JWTPrincipal(credential.payload)
                }else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, BaseResponse<String>(401, "Unauthorized", "未授权"))
            }
        }
    }
    routing {
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
        authenticate("auth-jwt") {
            get("/community/content") {
                val keyword = call.parameters["keyword"] ?: ""
                val res = CommunityService.getCommunityInfo(keyword)
                call.respond(createResponse(res))
            }
            get("/user/current_info"){
                val token = call.principal<JWTPrincipal>()
                token?.let {
                    val uid = it.payload.getClaim("uid").asInt()
                    val user = AccountService.getAccountInfo(uid)
                    call.respond(createResponse(user))
                }?:call.respond(BaseResponse.fastFailed())
            }
            get("/community/detail"){
                val cid = call.parameters["cid"]?:""
                if(cid.isEmpty()){
                    call.respond(BaseResponse.fastFailed())
                }
                call.respond(createResponse(CommunityService.getContentDetail(cid.toInt())))
            }
            post("/community/content") {
                val token = call.principal<JWTPrincipal>()
                if (token == null) {
                    call.respond(BaseResponse<String>(401, "Unauthorized", "未授权"))
                    return@post
                }
                val uid = token.payload.getClaim("uid").asInt()
                val content = call.receive<NewContent>()
                println("start insert");
                val result = CommunityService.addCommunityContent(content, uid);
                call.respond(createResponse(result));
            }
            post("/community/comment") {
                val uid = call.principal<JWTPrincipal>()!!.payload.getClaim("uid").asInt()
                val comment: NewComment = call.receive()
                val result = CommunityService.addComment(comment, uid)
                call.respond(createResponse(result))
            }
            post("/collection"){
                val cid = call.parameters["cid"]?:""
                if(cid.isEmpty()){
                    call.respond(BaseResponse.fastFailed())
                }
                val uid = call.principal<JWTPrincipal>()!!.payload.getClaim("uid").asInt()
                val result = CollectionService.addCollection(uid, cid.toInt())
                call.respond(createResponse(result))
            }
            delete("/collection"){
                val cid = call.parameters["cid"]?:""
                if(cid.isEmpty())
                    call.respond(BaseResponse.fastFailed())
                val uid = call.principal<JWTPrincipal>()!!.payload.getClaim("uid").asInt()
                val result = CollectionService.removeCollection(uid, cid.toInt())
                call.respond(createResponse(result))
            }
            get("/collection"){
                val uid = call.parameters["uid"]?:""
                if(uid.isEmpty())
                    call.respond(BaseResponse.fastFailed())
                val currentUserId = call.principal<JWTPrincipal>()!!.payload.getClaim("uid").asInt()
                val result = CollectionService.getCollections(uid.toInt(), currentUserId)
                call.respond(createResponse(result))
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
