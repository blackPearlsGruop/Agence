package com.ksa.agence.entity.categoriesByIdResponse

import com.ksa.agence.entity.companyResponse.DataCompanyResponse

data class Data(
    val companies: List<DataCompanyResponse>?,
    val description: String?,
    val icon: String?,
    val id: Int?,
    val is_consultant: Int?,
    val title: String?
)