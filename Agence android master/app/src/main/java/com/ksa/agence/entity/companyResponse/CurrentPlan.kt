package com.ksa.agence.entity.companyResponse

import java.io.Serializable

data class CurrentPlan(
    val description: String?,
    val duration_in_days: Int?,
    val id: Int?,
    val is_free_plan: Int?,
    val number_of_offers: Int?,
    val number_of_orders: Int?,
    val number_of_quick_orders: Int?,
    val number_of_services: Int?,
    val payment_method: String?,
    val payment_status: String?,
    val plan_end_at: String?,
    val price: Int?,
    val title: String?,
    val transaction_file: String?
): Serializable