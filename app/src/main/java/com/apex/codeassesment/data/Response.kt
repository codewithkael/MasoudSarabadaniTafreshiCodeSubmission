package com.apex.codeassesment.data

sealed class Response<T> {
    data class Success<T>(val data:T): Response<T>()
    data class Failure<T>(val data:T): Response<T>()
}
