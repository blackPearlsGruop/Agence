package com.ksa.agence.entity.authUserResponse

data class Data(
    val access_token: String?=null,
    val url: String?=null,
    val user: User?=null,
    val phone: List<String>?=null

)