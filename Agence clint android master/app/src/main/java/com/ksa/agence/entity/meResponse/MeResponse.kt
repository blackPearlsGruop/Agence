package com.ksa.agence.entity.meResponse

data class MeResponse(
    val code: Int?,
    val `data`: Data?,
    val direct: Any?,
    val message: String?,
    val success: Boolean?
)