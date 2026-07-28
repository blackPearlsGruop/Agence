package com.ksa.agenceCompany.interfaces

import java.text.FieldPosition

interface Home {
    fun clickItemShowService(idService: Int)
    fun clickItemOpportunitiesDetails(idOpportunities: Int)
    fun clickItemUpdateService(idService: Int)
    fun clickItemDeleteService(idService: Int,position: Int)
}
