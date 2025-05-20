package utils

sealed class Loading {

    data object Idle : Loading()

    open class Progress(
        val progress: Float = -1f,
        val message: String = "",
    ) : Loading() {
        fun isInteterminate() = progress == -1f
    }

    class Retry(
        val attempt: Int = 0,
    ) : Progress()

    open class Result : Loading()

    data class Success(val message: String = "") : Result()

    data class Error(
        val message: String = "",
        val error: Throwable? = null,
    ) : Result()

    object Empty : Result()
}