package com.ksa.agenceCompany.adapter

data class PaymentDistribution(
    val memberId: Int,
    val memberName: String,
    val memberInitials: String,
    val specialty: String,
    var percentage: Int = 0,
    var amount: Double = 0.0,
    val colorRes: Int
)