package entity

sealed interface LoadingStatus<out T> {
    data object PreStartLoading: LoadingStatus<Nothing>
    data object Loading: LoadingStatus<Nothing>
    data class LoadingSuccess<out T>(val data: T): LoadingStatus<T>
    data class LoadingFailed(val throwable: Throwable, val reason: String = "Unknown reason"): LoadingStatus<Nothing>
}