package com.ksa.agence.interfaces

interface Order {
    fun clickItemOrder(idOrder: Int)
    fun clickItemReorder(idOrder: Int)
    fun clickItemChat(model: com.ksa.agence.entity.allOrdersResponse.DataAllOrdersResponse)
}
