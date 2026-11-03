package com.ksa.agence.entity.allOfferCompanyResponse

import java.io.Serializable

data class AllOfferCompanyResponse(
    val code: Int?,
    val `data`: List<DataAllOfferCompanyResponse>?=null,
    val direct: Any?,
    val message: String?,
    val success: Boolean?
):Serializable