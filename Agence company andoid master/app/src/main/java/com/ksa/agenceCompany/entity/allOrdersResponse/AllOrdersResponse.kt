package com.ksa.agenceCompany.entity.allOrdersResponse

data class AllOrdersResponse(
    val code: Int,
    val `data`: List<DataAllOrdersResponse>? = null,
    val direct: Any? = null,
    val message: String? = null,
    val success: Boolean? = null,
    val currentPlan: CurrentPlan? = null,

    )