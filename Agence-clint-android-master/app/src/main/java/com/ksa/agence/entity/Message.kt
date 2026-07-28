package com.ksa.agence.entity
data class Message(
    val messageType: String = "",
    val messageVoice: String = "",
    val messageImage: String = "",
    val messageText: String = "",
    val isSent: Boolean = false,
    val isReceived: Boolean = false, // حالة استلام الرسالة
    val isRead: Boolean = false, // حالة قراءة الرسالة
    val senderId: Int = 0 ,// معرف المرسل
    val receivedId: Int = 0 ,// معرف المرسل
    val messageKey: String? = null, // إضافة خاصية مفتاح الرسالة
    val timestamp: Long = 0L // تعيين قيمة افتراضية
)


data class AllListChatCompany(
    var idOrder: Int =0,
    val idCompany: Int =0,
    val nameCompany: String ="",
    val imageCompany: String = "",
    val categoryName: String = "",
    val orderNumber: String = "",
    )
data class ListChatUser(
    var idOrder: Int =0,
    val idUser: Int =0,
    val nameUser: String ="",
    val imageUser: String = "",
    val categoryName: String = "",
    val orderNumber: String = "",

    )
