package com.ksa.agence.entity.companyResponse

import java.io.Serializable

data class Category(
    val description: String?,
    val icon: String?,
    val id: Int?,
    val is_consultant: Int?,
    val title: String?="No Category"
): Serializable