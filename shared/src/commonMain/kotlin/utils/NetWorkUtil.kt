package utils

import entity.BaseResponse
import io.ktor.client.call.*
import io.ktor.client.statement.*

suspend inline fun <reified T> HttpResponse.get(): Result<T>{
    if(status.value in 200..299){
        val response = body<BaseResponse<T>>()
        return if(response.isSuccess){
            Result.success(response.data!!)
        } else{
            Result.failure(Exception(response.msg))
        }
    } else {
        return Result.failure(Exception("http status code: ${status.value}"))
    }
}