package com.ksa.agence.member.entity.showCompaniesResponse

import com.ksa.agence.entity.showCompaniesResponse.User
import com.ksa.agence.member.entity.authUserResponse.Category


data class Order(
    val category: Category?,
    val id: Int?,
    val offer: Any?,
    val order_duration_in_days: Int?,
    val rate: Any?,
    val service: Any?,
    val title: String?,
    val user: User?
)