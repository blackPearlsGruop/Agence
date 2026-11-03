package com.ksa.agence.interfaces

interface Company {
    fun clickItemCompany(idCompany: Int,flag:String)
    fun clickItemAddCompanyFav(idCompany: Int,pos:Int)
    fun clickItemShowService(idService: Int)
}
