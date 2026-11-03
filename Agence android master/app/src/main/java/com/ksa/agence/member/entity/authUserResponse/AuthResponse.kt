package com.ksa.agence.member.entity.authUserResponse

data class AuthResponse(
    val code: Int,
    val `data`: List<Any>?=null,
    val direct: Any?=null,
    val message: String,
    val success: Boolean
)