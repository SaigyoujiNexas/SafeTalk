package di

import client_api.CommunityService
import client_api.LoginService
import com.russhwolf.settings.get
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import org.koin.dsl.module
import kotlin.math.sign

val netWorkModule = module {
    single<HttpClient> {
        HttpClient() {
            install(ContentNegotiation) {
                json()
            }
        }.also { client ->
            client.plugin(HttpSend).intercept { request ->
                val token: String? = settings.settings["token"]
                token?.let {
                    if (it.isNotEmpty())
                        request.headers["Authorization"] = "Bearer $it"
                }
                execute(request)
            }
        }
    }
    single<LoginService> {
        LoginService(get())
    }

    single<CommunityService> {
        CommunityService(get())
    }
}