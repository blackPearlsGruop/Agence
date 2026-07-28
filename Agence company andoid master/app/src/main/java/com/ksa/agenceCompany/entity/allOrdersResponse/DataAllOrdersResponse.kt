package com.ksa.agenceCompany.entity.allOrdersResponse

import com.ksa.agenceCompany.entity.meResponse.DataMeResponse

data class DataAllOrdersResponse(
    val accepted_offer: Any? = null,
    val user: UserOrder? = null,
    val category: Category? = null,
    val company: DataMeResponse? = null,
    val created_at: String? = null,
    val description: String? = null,
    val has_offers: Boolean? = null,
    val id: Int,
    val is_current_company_send_offer: IsCurrentCompanySendOffer? = null,
    val is_rated_before: Boolean? = null,
    val offer: Any? = null,
    val offers: List<Offer>? = null,
    val order_duration_in_days: Int? = null,
    val order_number: String? = null,
    val order_status: String? = null,
    val order_type: String? = null,
    val payment_method: String? = null,
    val payment_status: String? = null,
    val price: Any? = null,
    val service: Any? = null,
    val tax_percentage: Int? = null,
    val title: String? = null,
    val currentPlan: CurrentPlan? = null,

    )