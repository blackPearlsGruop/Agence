package com.ksa.agence.common


sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<String>(message: String) : Resource<String>( message )
    class Loading<T> : Resource<T>()
}