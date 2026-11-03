package com.ksa.agence.entity.getSingleOrderResponse

import java.io.Serializable

data class GetSingleOrderResponse(
    val code: Int?,
    val `data`: Data?,
    val direct: Any?,
    val message: String?,
    val success: Boolean?
):Serializable