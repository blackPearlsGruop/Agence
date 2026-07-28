package com.ksa.agenceCompany.entity.allOrdersResponse

data class Offer(
    val company: CompanyX?=null,
    val description: String?=null,
    val id: Int,
    val images: Any?=null,
    val offer_duration_in_days: Any?=null,
    val price: Any?=null,
    val title: Any?=null
)