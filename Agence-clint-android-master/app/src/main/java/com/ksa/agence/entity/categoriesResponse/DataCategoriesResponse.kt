package com.ksa.agence.entity.categoriesResponse

data class DataCategoriesResponse(
    val description: String?,
    val icon: String?,
    val id: Int?,
    val is_consultant: Int?,
    val title: String?,
    var isSelected:Boolean=false

)