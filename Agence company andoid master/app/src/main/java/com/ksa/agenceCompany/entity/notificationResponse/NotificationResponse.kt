package com.ksa.agenceCompany.entity.notificationResponse

data class NotificationResponse(
    val code: Int?,
    val `data`: List<DataNotificationResponse>?,
    val direct: Any?,
    val message: String?,
    val success: Boolean?
)