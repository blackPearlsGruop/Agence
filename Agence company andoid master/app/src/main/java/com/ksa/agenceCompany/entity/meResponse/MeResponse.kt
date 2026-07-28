package com.ksa.agenceCompany.entity.meResponse

data class MeResponse(
    val code: Int?,
    val `data`: DataMeResponse?,
    val direct: Any?,
    val message: String?,
    val success: Boolean?
)