package com.ksa.agenceCompany.entity.authUserResponse

data class AuthUserResponse(
    val code: Int?,
    val `data`: Data?=null,
    val direct: Any?=null,
    val message: String?,
    val success: Boolean?
)

