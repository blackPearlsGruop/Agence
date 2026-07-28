package com.ksa.agence.entity.getAllFavouritesResponse

import com.ksa.agence.entity.companyResponse.Category

data class DataGetAllFavouritesResponse(
    val account_type: String?,
    val address: String?,
    val availability: Int?,
    val avg_rate: Double?,
    val categories: List<Category>?,
    val company_background_image: String?,
    val company_logo: String?,
    val consultant_price: Int?,
    val created_at: String?,
    val currentPlan: Any?,
    val default_lang: String?,
    val description: String?,
    val enable_notification: Int?,
    val id: Int?,
    val is_added_favourite: Boolean?,
    val is_subscribed_to_free_plan: Int?,
    val plan_end_at: Any?,
    val price_start_from: Int?,
    val rate_count: Int?,
    val title: String?
)