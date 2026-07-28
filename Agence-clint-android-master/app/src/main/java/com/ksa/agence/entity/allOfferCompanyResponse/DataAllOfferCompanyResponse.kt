package com.ksa.agence.entity.allOfferCompanyResponse

import com.ksa.agence.entity.allOrdersResponse.DataAllOrdersResponse
import com.ksa.agence.entity.companyResponse.DataCompanyResponse
import java.io.Serializable

data class DataAllOfferCompanyResponse(
    val company: DataCompanyResponse?,
    val id: Int?,
    val offer_status: String?,
    val order: DataAllOrdersResponse?,
    val price: Int?,
    val user: User?): Serializable