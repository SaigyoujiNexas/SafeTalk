package org.example.project.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import org.koin.core.component.KoinComponent
import java.io.File
import java.net.ConnectException
import java.util.*

object ImageService: KoinComponent {
    suspend fun uploadImage(imageData: ByteArray): Result<String>{
        var fileName = UUID.randomUUID().toString().replace("-", "");
        fileName += ".jpeg"
        val file = File(fileName);
        if(!file.exists()){
            withContext(Dispatchers.IO) {
                file.createNewFile()
                file.writeBytes(imageData);
            };
        }
        return HttpClient(){
            install(ContentNegotiation){
                json()
            }
        }.let {client ->
            val response = client.post {
                url("https://sm.ms/api/v2/upload")
                header("Authorization", "ZYS161F0LIXdjMVgXdaCUt3FlHv8Xu44")
                contentType(ContentType.MultiPart.FormData)
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("smfile",file.readBytes(),
                                Headers.build {
                                    append(HttpHeaders.ContentType,  "application/octet-stream")
                                    append(HttpHeaders.ContentDisposition, "filename=$fileName")
                                })
                        }
                    )
                )
            }.also { println(it.bodyAsText()) }
                .body<ImageUploadInfo>()

            file.delete()
            if(response.success){
                return@let Result.success(response.data.url)
            }else{
                return Result.failure(ConnectException("upload Image Failed"))
            }
        }
    }
}
@Serializable
data class ImageUploadInfo @OptIn(ExperimentalSerializationApi::class) constructor(
    val success: Boolean,
    val code: String,
    val message: String,
    val data: ImageUploadData,
    @JsonNames("RequestId")
    val requestId: String
)
@Serializable
data class ImageUploadData @OptIn(ExperimentalSerializationApi::class) constructor(
    @JsonNames("file_id")
    val fileId: Int,
    val width: Int,
    val height : Int,
    @JsonNames("filename")
    val fileName: String,
    @JsonNames("storename")
    val storeName: String,
    val size: Long,
    val path: String,
    val hash: String,
    val url:String,
    val delete: String,
    val page: String,
)
