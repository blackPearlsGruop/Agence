package com.ksa.agenceCompany.entity.getSingleOpportunitiesResponse

data class GetSingleOpportunitiesResponse(
    val code: Int,
    val `data`: Data,
    val direct: Any?=null,
    val message: String,
    val success: Boolean
)