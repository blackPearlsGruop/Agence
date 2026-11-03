package com.ksa.agence.member.entity.meResponse

data class SendWithdrawalRequestResponse(
    val code: Int?,
    val `data`: List<Any>?,
    val direct: Any?,
    val message: String?,
    val success: Boolean?
)