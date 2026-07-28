package com.ksa.agenceCompany.entity.authUserResponse

data class AuthResponse(
    val code: Int,
    val `data`: List<Any>?=null,
    val direct: Any?=null,
    val message: String,
    val success: Boolean
)