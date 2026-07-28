package com.ksa.agence.entity.allOrdersResponse

import com.ksa.agence.entity.companyResponse.Category
import com.ksa.agence.entity.companyResponse.CompanyResponse
import com.ksa.agence.entity.companyResponse.DataCompanyResponse
import java.io.Serializable

data class DataAllOrdersResponse(
    val accepted_offer: Any?,
    val category: Category?,
    val company: DataCompanyResponse?,
    val created_at: String?,
    val description: String?,
    val id: Int?,
    val offer: Any?,
    val order_duration_in_days: Int?,
    val order_number: String?,
    val order_status: String?,
    val order_type: String?,
    val payment_method: Any?,
    val payment_status: Any?,
    val price: Int?,
    val service: Any?,
    val tax_percentage: Int?,
    val has_offers: Boolean?,
    val title: String?
): Serializable