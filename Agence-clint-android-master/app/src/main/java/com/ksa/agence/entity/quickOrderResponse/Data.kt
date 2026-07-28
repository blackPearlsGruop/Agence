package com.ksa.agence.entity.quickOrderResponse

import com.ksa.agence.entity.companyResponse.Category

data class Data(
    val accepted_offer: Any?,
    val category: Category?,
    val company: Any?,
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
    val price: Any?,
    val service: Any?,
    val tax_percentage: Int?,
    val title: String?
)