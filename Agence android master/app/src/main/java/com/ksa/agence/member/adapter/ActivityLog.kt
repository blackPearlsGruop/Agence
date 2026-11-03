package com.ksa.agence.member.adapter

data class ActivityLog(
    val id: Int,
    val memberName: String,
    val memberInitials: String,
    val action: String,
    val subject: String,
    val timeAgo: String,
    val type: ActivityType,
    val colorRes: Int,
    val timeGroup: String
)

enum class ActivityType {
    TASK, MEMBER, PAYMENT, FILE
}
