package com.ksa.agence.entity.quickOrderResponse

data class QuickOrderResponse(
    val code: Int?,
    val `data`: Data?,
    val direct: Any?,
    val message: String?,
    val success: Boolean?
)