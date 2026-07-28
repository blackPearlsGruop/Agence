package com.ksa.agence.entity.categoriesResponse

data class CategoriesResponse(
    val code: Int?,
    val `data`: List<DataCategoriesResponse>?,
    val direct: Any?,
    val message: String?,
    val success: Boolean?
)