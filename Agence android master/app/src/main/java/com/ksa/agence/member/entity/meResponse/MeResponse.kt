package com.ksa.agence.member.entity.meResponse

data class MeResponse(
    val code: Int?,
    val `data`: DataMeResponse?,
    val direct: Any?,
    val message: String?,
    val success: Boolean?
)