package com.ksa.agence.member.entity.allSubscriptionResponse

data class AllSubscriptionResponse(
    val code: Int,
    val `data`: List<DataAllSubscriptionResponse>,
    val direct: Any?=null,
    val message: String,
    val success: Boolean
)