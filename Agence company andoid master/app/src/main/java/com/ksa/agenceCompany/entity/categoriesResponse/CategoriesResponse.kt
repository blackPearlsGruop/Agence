package com.ksa.agenceCompany.entity.categoriesResponse

import java.io.Serializable

data class CategoriesResponse(
    val code: Int?,
    val `data`: List<DataCategoriesResponse>?,
    val direct: Any?,
    val message: String?,
    val success: Boolean?
): Serializable