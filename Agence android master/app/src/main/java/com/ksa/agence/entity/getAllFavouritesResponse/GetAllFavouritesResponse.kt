package com.ksa.agence.entity.getAllFavouritesResponse

data class GetAllFavouritesResponse(
    val code: Int?,
    val `data`: List<DataGetAllFavouritesResponse>?,
    val direct: Any?,
    val message: String?,
    val success: Boolean?
)