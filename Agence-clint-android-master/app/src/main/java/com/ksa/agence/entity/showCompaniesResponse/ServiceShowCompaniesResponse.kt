package com.ksa.agence.entity.showCompaniesResponse

data class ServiceShowCompaniesResponse(
    val description: String?,
    val id: Int?,
    val images: List<String?>?,
    val price: Int?,
    val service_duration_in_days: Int?,
    val title: String?
)