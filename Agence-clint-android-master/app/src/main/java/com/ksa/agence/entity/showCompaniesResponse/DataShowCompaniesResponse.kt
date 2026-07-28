package com.ksa.agence.entity.showCompaniesResponse

import com.ksa.agence.entity.companyResponse.Category
import com.ksa.agence.entity.companyResponse.CurrentPlan

data class DataShowCompaniesResponse(
val account_type: String?,
    val order_number: String?=null,
    val address: String?,
    val availability: Int?,
    val avg_rate: Float?,
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
    val is_added_favourite: Boolean?,
    val is_subscribed_to_free_plan: Int?,
    val offers: List<OfferShowCompaniesResponse>?,
    val orders: List<Order>?,
    val plan_end_at: String?,
    val price_start_from: Int?,
    val rate_count: Int?,
    val reviews: List<Review>?,
    val services: List<ServiceShowCompaniesResponse>?,
    val title: String?,
    val works: List<WorkShowCompaniesResponse>?
)