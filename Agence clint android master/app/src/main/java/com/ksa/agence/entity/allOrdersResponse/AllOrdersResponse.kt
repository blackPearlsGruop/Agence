package com.ksa.agence.entity.allOrdersResponse

data class AllOrdersResponse(
    val code: Int?,
    val `data`: List<DataAllOrdersResponse>?,
    val direct: Any?,
    val message: String?,
    val success: Boolean?
)