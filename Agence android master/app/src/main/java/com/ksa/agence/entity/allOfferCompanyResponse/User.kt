package com.ksa.agence.entity.allOfferCompanyResponse

import java.io.Serializable

data class User(
    val created_at: String?,
    val default_lang: String?,
    val device_token: String?,
    val enable_notification: Int?,
    val id: Int?,
    val name: String?,
    val notification_count: Int?,
    val phone: String?,
    val profile_image: String?,
    val updated_at: String?
): Serializable