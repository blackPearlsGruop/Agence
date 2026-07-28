package com.ksa.agence.entity.bannerResponse

data class BannerResponse(
    val code: Int?,
    val `data`: List<DataBannerResponse>?,
    val direct: Any?,
    val message: String?,
    val success: Boolean?
)