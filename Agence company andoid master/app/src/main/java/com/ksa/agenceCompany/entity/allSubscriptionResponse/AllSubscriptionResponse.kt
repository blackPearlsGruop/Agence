package com.ksa.agenceCompany.entity.allSubscriptionResponse

data class AllSubscriptionResponse(
    val code: Int,
    val `data`: List<DataAllSubscriptionResponse>,
    val direct: Any?=null,
    val message: String,
    val success: Boolean
)