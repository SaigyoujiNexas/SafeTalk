package di

import client_api.CollectionService
import client_api.CommunityService
import client_api.LoginService
import client_api.UserService
import com.russhwolf.settings.get
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import org.koin.dsl.module
import settings.settings
import kotlin.math.sign

val netWorkModule = module {
    single<HttpClient> {
        HttpClient() {
            install(ContentNegotiation) {
                json()
            }
            install(Auth){
                bearer {
                    loadTokens {
                        BearerTokens(settings["token", ""], settings["token", ""])
                    }
                }
            }
        }
//            .also { client ->
//            client.plugin(HttpSend).intercept { request ->
//                val token: String? = settings["token"]
//                token?.let {
//                    if (it.isNotEmpty())
//                        request.headers["Authorization"] = "Bearer $it"
//                }
//                execute(request)
//            }
//        }
    }
    single<LoginService> {
        LoginService(get())
    }

    single<CommunityService> {
        CommunityService(get())
    }
    single<UserService> {
        UserService(get())
    }
    single<CollectionService> {
        CollectionService(get())
    }
}