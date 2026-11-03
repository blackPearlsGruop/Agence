package com.ksa.agence.member.entity.allOpportunitiesResponse

data class AllOpportunitiesResponse(
    val code: Int,
    val `data`: List<DataAllOpportunitiesResponse>?=null,
    val direct: Any?=null,
    val message: String,
    val success: Boolean
)