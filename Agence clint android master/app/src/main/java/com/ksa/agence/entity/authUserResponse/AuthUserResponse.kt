package com.ksa.agence.entity.authUserResponse

data class AuthUserResponse(
    val code: Int?,
    val `data`: Data?=null,
    val direct: Any?,
    val message: String?,
    val success: Boolean?
)

