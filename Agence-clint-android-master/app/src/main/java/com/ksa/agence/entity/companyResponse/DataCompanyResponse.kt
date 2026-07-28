package com.ksa.agence.entity.companyResponse

import java.io.Serializable

data class DataCompanyResponse(
    val account_type: String?,
    val address: String?,
    val availability: Int?,
    val avg_rate: Double?,
    val categories: List<Category>?,
    val company_background_image: String?,
    val company_logo: String?,
    val consultant_price: Int?,
    val created_at: String?,
    val currentPlan: CurrentPlan?,
    val default_lang: String?,
    val description: String?,
    val enable_notification: Int?,
    val id: Int?,
    var is_added_favourite: Boolean?,
    val is_subscribed_to_free_plan: Int?,
    val plan_end_at: String?,
    val price_start_from: Int?,
    val rate_count: Int?,
    val title: String?
): Serializable