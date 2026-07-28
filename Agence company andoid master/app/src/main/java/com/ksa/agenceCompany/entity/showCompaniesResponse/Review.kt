package com.ksa.agenceCompany.entity.showCompaniesResponse

import com.ksa.agenceCompany.entity.authUserResponse.Company

data class Review(
    val company: Company?,
    val id: Int?,
    val rate: Any? = null,
    val review: String?,
    val user: UserX?
)