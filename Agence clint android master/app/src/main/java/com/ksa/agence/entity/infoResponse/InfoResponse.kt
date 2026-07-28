package com.ksa.agence.entity.infoResponse

data class InfoResponse(
    val code: Int?,
    val `data`: Data?,
    val direct: Any?,
    val message: String?,
    val success: Boolean?
)