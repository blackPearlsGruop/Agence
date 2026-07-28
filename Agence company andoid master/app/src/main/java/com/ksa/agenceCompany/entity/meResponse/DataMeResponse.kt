package com.ksa.agenceCompany.entity.meResponse

data class DataMeResponse(
    val account_type: String?,
    val address: String?,
    val availability: Int?,
    val avg_rate: Int?,
    val bank_account: BankAccount?,
    val categories: List<Category>?,
    val company_background_image: String?,
    val company_logo: String?,
    val consultant_price: Int?,
    val created_at: String?,
    val currentPlan: CurrentPlan?,
    val default_lang: String?,
    val description: String?,
    val device_token: String?,
    val enable_notification: Int?,
    val id: Int?,
    val is_added_favourite: Boolean?,
    val is_subscribed_to_free_plan: Int?,
    val nationality_id: NationalityId?,
    val notification_count: NotificationCount?,
    val plan_end_at: String?,
    val price_start_from: Int?,
    val rate_count: Int?,
    val title: String?
)