package com.ksa.agence.member.entity.showCompaniesResponse

data class ShowCompaniesResponse(
    val code: Int?,
    val `data`: DataShowCompaniesResponse?,
    val direct: Any?,
    val message: String?,
    val success: Boolean?
)