package com.ksa.agence.entity.showCompaniesResponse

import com.ksa.agence.entity.companyResponse.CompanyResponse

data class Review(
    val company: CompanyResponse?,
    val id: Int?,
    val rate: Any?=null,
    val review: String?,
    val user: UserX?
)