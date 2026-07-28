package com.ksa.agenceCompany.entity.getCompanyWorksResponse

data class GetCompanyWorksResponse(
    val code: Int,
    val `data`: List<DataGetCompanyWorksResponse>?=null,
    val direct: Any?=null,
    val message: String,
    val success: Boolean
)