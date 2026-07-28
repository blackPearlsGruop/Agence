package com.ksa.agenceCompany.entity.categoriesByIdResponse

data class CategoriesByIdResponse(
    val code: Int,
    val `data`: Data?=null,
    val direct: Any?=null,
    val message: String,
    val success: Boolean
)