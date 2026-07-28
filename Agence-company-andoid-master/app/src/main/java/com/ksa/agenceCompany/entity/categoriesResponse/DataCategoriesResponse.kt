package com.ksa.agenceCompany.entity.categoriesResponse

import java.io.Serializable

data class DataCategoriesResponse(
    val description: String?,
    val icon: String?,
    val images: List<String>?,
    val id: Int?,
    val is_consultant: Int?,
    val price: Int?,
    val title: String?,
    var isSelected:Boolean=false


):Serializable