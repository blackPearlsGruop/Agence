package com.ksa.agence.member.interfaces

interface Order {
    fun clickItemOrder(idOrder: Int)
    fun clickItemSendOffer(idOrder: Int)
    fun clickItemRejectOrder(idOrder: Int)
}
