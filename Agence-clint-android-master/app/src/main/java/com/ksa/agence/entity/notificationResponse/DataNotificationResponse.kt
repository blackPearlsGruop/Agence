package com.ksa.agence.entity.notificationResponse

data class DataNotificationResponse(
    val created_at: String?,
    val created_at_for_humans: String?,
    val description: String?,
    val id: String?,
    val is_read: Boolean?,
    val read_at: Any?,
    val read_at_for_humans: Any?,
    val title: String?
)