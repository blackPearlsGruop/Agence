package com.ksa.agence.entity.authUserResponse

data class AuthResponse(
    val code: Int?,
    val `data`: List<Any>?=null,
    val direct: Any?,
    val message: String?,
    val success: Boolean?
)

