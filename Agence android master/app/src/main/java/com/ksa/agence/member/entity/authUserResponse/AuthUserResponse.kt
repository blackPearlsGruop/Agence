package com.ksa.agence.member.entity.authUserResponse

data class AuthUserResponse(
    val code: Int?,
    val `data`: Data?=null,
    val direct: Any?=null,
    val message: String?,
    val success: Boolean?
)

