package com.ksa.agence.member.entity.showCompaniesResponse

import com.ksa.agence.entity.showCompaniesResponse.UserX
import com.ksa.agence.member.entity.authUserResponse.Company

data class Review(
    val company: Company?,
    val id: Int?,
    val rate: Any? = null,
    val review: String?,
    val user: UserX?
)