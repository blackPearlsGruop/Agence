package com.ksa.agence.entity.getSingleOrderResponse

import com.ksa.agence.entity.allOfferCompanyResponse.DataAllOfferCompanyResponse
import com.ksa.agence.entity.allOfferCompanyResponse.User
import com.ksa.agence.entity.companyResponse.Category
import com.ksa.agence.entity.companyResponse.DataCompanyResponse
import java.io.Serializable

data class Data(
    val accepted_offer: Any?,
    val category: Category?,
    val company: DataCompanyResponse?,
    val user: User?,
    val offers: List<DataAllOfferCompanyResponse>?=null,
    val created_at: String?,
    val description: String?,
    val id: Int?,
    val offer: Any?,
    val order_duration_in_days: Int?,
    val order_number: String?,
    val order_status: String?,
    val order_type: String?,
    val payment_method: String?,
    val payment_status: String?,
    val price: Int?,
    val service: Any?,
    val tax_percentage: Int?,
    val is_rated_before: Boolean?,
    val title: String?
): Serializable