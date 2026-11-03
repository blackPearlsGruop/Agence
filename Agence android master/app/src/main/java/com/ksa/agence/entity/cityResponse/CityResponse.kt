package com.ksa.agenceCompany.entity.cityResponse

data class CityResponse(
    val code: Int?,
    val `data`: List<DataCityResponse>?,
    val direct: Any?,
    val message: String?,
    val success: Boolean?
)