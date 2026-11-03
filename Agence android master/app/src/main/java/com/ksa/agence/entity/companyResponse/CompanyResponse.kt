package com.ksa.agence.entity.companyResponse

data class CompanyResponse(
    val code: Int?,
    val `data`: List<DataCompanyResponse>?,
    val direct: Any?,
    val message: String?,
    val success: Boolean?
)