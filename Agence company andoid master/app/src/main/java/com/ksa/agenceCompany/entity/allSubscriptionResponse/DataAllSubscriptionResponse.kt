package com.ksa.agenceCompany.entity.allSubscriptionResponse

data class DataAllSubscriptionResponse(
    val description: String?=null,
    val duration_in_days: Int?=null,
    val id: Int,
    val is_free_plan: Int?=null,
    val number_of_offers: Int?=null,
    val number_of_orders: Int?=null,
    val number_of_quick_orders: Int?=null,
    val number_of_services: Int?=null,
    val payment_method: String?=null,
    val payment_status: String?=null,
    val plan_end_at: String?=null,
    val price: Int?=null,
    val title: String?=null,
    val features: List<Features>?=null,
    val transaction_file: String?=null
)