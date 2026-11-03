package com.ksa.agence.member.entity.subscribeToPlanResponse

data class SubscribeToPlanResponse(
    val code: Int,
    val `data`: Data?=null,
    val direct: Any?=null,
    val message: String,
    val success: Boolean
)