package com.ksa.agence.member.entity.getSingleOpportunitiesResponse

data class GetSingleOpportunitiesResponse(
    val code: Int,
    val `data`: Data,
    val direct: Any?=null,
    val message: String,
    val success: Boolean
)