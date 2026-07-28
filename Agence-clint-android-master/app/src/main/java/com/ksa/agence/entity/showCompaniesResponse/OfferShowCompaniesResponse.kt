package com.ksa.agence.entity.showCompaniesResponse

data class OfferShowCompaniesResponse(
    val description: String?,
    val id: Int?,
    val images: List<String?>?,
    val offer_duration_in_days: Int?,
    val price: Int?,
    val title: String?
)