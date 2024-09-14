package com.charlesmuchogo.research.presentation.utils

data class Results<out T>(
    val status: ResultStatus,
    val data: T?,
    val message: String?,
) {
    companion object {
        fun <T> initial(): Results<T> = Results(ResultStatus.INITIAL, null, null)

        fun <T> success(data: T?): Results<T & Any> = Results(ResultStatus.SUCCESS, data, null)

        fun <T> error(msg: String = "Something went wrong. Try again"): Results<T> = Results(ResultStatus.ERROR, null, msg)

        fun <T> loading(): Results<T> = Results(ResultStatus.LOADING, null, null)
    }
}

enum class ResultStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    ERROR,
}
