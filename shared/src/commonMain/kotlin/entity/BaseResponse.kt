package entity

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val code: Int,
    val msg: String,
    val data: T? = null,
){
    val isSuccess: Boolean
        get() = code == 200
}