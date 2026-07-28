package com.ksa.agenceCompany.entity.categoriesByIdResponse

import com.ksa.agenceCompany.entity.authUserResponse.Company

data class Data(
    val company: Company?,
    val description: String?,
    val id: Int,
    val images: List<String>?=null,
    val price: Int,
    val service_duration_in_days: Int?=null,
    val offer_duration_in_days: Int?=null,
    val title: String?
)