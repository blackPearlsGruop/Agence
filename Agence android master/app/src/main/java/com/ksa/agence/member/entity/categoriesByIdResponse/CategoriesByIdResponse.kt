package com.ksa.agence.member.entity.categoriesByIdResponse

data class CategoriesByIdResponse(
    val code: Int,
    val `data`: Data?=null,
    val direct: Any?=null,
    val message: String,
    val success: Boolean
)