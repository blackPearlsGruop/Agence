package com.ksa.agenceCompany.interfaces

interface Offer {
    fun clickItemOffer(idOffer: Int,
                       imageCompany:String,
                       nameCompany:String ,
                       dicCategory:String,
                       nameCategory:String,
                       priceOffer:String,
                       descriptionOrder:String,
                       orderNo:String)

    fun acceptedOffer(idOffer: Int,pos:Int)
    fun rejectOffer(idOffer: Int,pos:Int)


}
