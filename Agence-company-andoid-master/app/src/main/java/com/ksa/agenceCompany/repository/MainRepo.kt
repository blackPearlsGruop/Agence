package com.ksa.agenceCompany.repository

import com.ksa.agenceCompany.network.APIEndPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import retrofit2.http.Query

class MainRepo(private val api: APIEndPoint) {

    suspend fun userRegister(
        account_type: RequestBody,
        name: RequestBody,
        phone: RequestBody,
        country_id: RequestBody,
        city_id: RequestBody,
        accept_terms_and_conditions: RequestBody,
        device_token: RequestBody,
        commercial_licence: MultipartBody.Part?,
        categories: List<Int>,
        nationality_id: RequestBody
    ) = api.userRegister(
        account_type,
        name,
        phone,
        country_id,
        city_id,
        accept_terms_and_conditions,
        device_token,
        commercial_licence,
        categories,
        nationality_id
    )


    suspend fun getCity() = api.getCity()


    suspend fun userUpdateProfile(
        name: RequestBody, description: RequestBody, address: RequestBody, categories: List<Int>, company_logo: MultipartBody.Part?, company_background_image: MultipartBody.Part?
    ) = api.userUpdateProfile(name,description,address,categories,company_logo,company_background_image)

    suspend fun updateConsultationPrice(consultant_price: Int
    ) = api.updateConsultationPrice(consultant_price)

    suspend fun userLogin(
        phone: String, firebase_token: String
    ) = api.userLogin(phone, firebase_token)

    suspend fun activeCode(
        phone: String, otpCode: String, firebase_token: String
    ) = api.activeCode(phone, otpCode, firebase_token)

    suspend fun getNotification() = api.getNotification()

    suspend fun sendOffer(orderID: Int, amount: Int) = api.sendOffer(orderID, amount)
    suspend fun deleteService(idService: Int) = api.deleteService(idService)
    suspend fun deleteOffer(idOffer: Int) = api.deleteOffer(idOffer)
    suspend fun sendRequest(opportunity_id: Int, price: Int) =
        api.sendRequest(opportunity_id, price)

    suspend fun subscribeToPlan(opportunity_id: Int, payment_method: String) =
        api.subscribeToPlan(opportunity_id, payment_method)

    suspend fun addWorksResponse(title:RequestBody,
                                 description:RequestBody,
                                 image: MultipartBody.Part? = null) = api.addWorksResponse(title, description, image)
    suspend fun deleteWorks(idWork: Int) = api.deleteWorks(idWork)
    suspend fun updateAvailability(_method: String) = api.updateAvailability(_method)
    suspend fun getCompanyWorks() = api.getCompanyWorks()
    suspend fun getAllPlan() = api.getAllPlan()
    suspend fun getAllSubscription() = api.getAllSubscription()
    suspend fun getSingleOpportunities(idOpportunities: Int) =
        api.getSingleOpportunities(idOpportunities)

    suspend fun allOpportunities() = api.allOpportunities()
    suspend fun getCategory() = api.getCategory()
    suspend fun getCategoryNotToken() = api.getCategoryNotToken()

    suspend fun getOffers() = api.getOffers()
    suspend fun allOrders(status: String) = api.allOrders(status)
    suspend fun contactUs(titleMessage: String, note: String) = api.contactUs(titleMessage, note)

    suspend fun infos(type: Int) = api.infos(type)


    suspend fun storeNewService(
        title: RequestBody,
        description: RequestBody,
        price: RequestBody,
        service_duration_in_days: RequestBody,
        images: List<MultipartBody.Part>
    ) = api.storeNewService(title, description, price, service_duration_in_days, images)


    suspend fun updateService(
        idService: Int,
        title: String,
        description: String,
        price: Int,
        service_duration_in_days: Int
    ) = api.updateService(idService, title, description, price, service_duration_in_days, "PUT")



    suspend fun updateOffer(
        idOffer: Int,
        title: String,
        description: String,
        price: Int,
        offer_duration_in_days: Int
    ) = api.updateOffer(idOffer, title, description, price, offer_duration_in_days, "PUT")


    suspend fun storeNewOffer(
        title: RequestBody,
        description: RequestBody,
        price: RequestBody,
        offer_duration_in_days: RequestBody,
        images: List<MultipartBody.Part>
    ) = api.storeNewOffer(title, description, price, offer_duration_in_days, images)


    suspend fun categoriesById(idService: Int) = api.categoriesById(idService)
    suspend fun offerById(idOffer: Int) = api.offerById(idOffer)

    suspend fun acceptOffer(idOffer: Int, payment_method: String) =
        api.acceptOffer(idOffer, payment_method)

    suspend fun rejectOrder(idOrder: Int) = api.rejectOrder(idOrder)
    suspend fun getSingleOrder(idOrder: Int) = api.getSingleOrder(idOrder)

    suspend fun ratingCompany(
        order_id: Int,
        company_id: Int,
        rate: Float,
        review: String
    ) = api.ratingCompany(order_id, company_id, rate, review)


    suspend fun userLogOutApp() = api.userLogOutApp()
    suspend fun userDeleteAccount() = api.userDeleteAccount()
    suspend fun me() = api.me()
    suspend fun sendWithdrawalRequest( name: String,
                                     bank_account: String, bank_account_number: String,
                                       iban_number: String) = api.sendWithdrawalRequest(name, bank_account, bank_account_number, iban_number)

}

