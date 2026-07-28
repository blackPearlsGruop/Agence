package com.ksa.agence.repository

import com.ksa.agence.network.APIEndPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Query

class MainRepo(private val api: APIEndPoint) {
    
    suspend fun userRegister(
        name: String,phone: String, accept_terms_and_conditions: Int, firebase_token: String
    ) = api.userRegister(name,phone, "password","password",accept_terms_and_conditions,firebase_token)


    suspend fun userUpdateProfile(
        name: RequestBody,phone: RequestBody, profile_image: MultipartBody.Part?, method: RequestBody
    ) = api.userUpdateProfile(name,phone, profile_image,method)

    suspend fun getCity() = api.getCity()

    suspend fun userLogin(
        phone: String, firebase_token: String
    ) = api.userLogin(phone, firebase_token)

    suspend fun activeCode(
        phone: String,otpCode:String, firebase_token: String
    ) = api.activeCode(phone,otpCode, firebase_token)

    suspend fun getNotification() = api.getNotification()

    suspend fun makePayment(idOffer: Int,payment_method:String) = api.makePayment(idOffer,payment_method)
    suspend fun getCategory() = api.getCategory()
    suspend fun getCompanyResultFilter(account_type:String,city_id:Int,
                                        categories: List<Int>,
                                       price:String,
                                      rate:String,
                                       name:String) = api.getCompanyResultFilter(account_type,city_id, categories, price, rate, name)
    suspend fun getCompany() = api.getCompany()
    suspend fun getAllFavourites() = api.getAllFavourites()
    suspend fun addFavourites(company_id:Int) = api.addFavourites(company_id)
    suspend fun getBanner() =api.banner()
    suspend fun allOfferCompany() =api.allOfferCompany()
    suspend fun allOrders(status:List<String>) =api.allOrders(status)
    suspend fun contactUs(titleMessage:String,note: String) =api.contactUs(titleMessage,note)

    suspend fun infos(type: Int) =api.infos(type)

    suspend fun showCompanies(idCompanies: Int) =api.showCompanies(idCompanies)
    suspend fun quickOrder(category_id:Int,
                           company_id:Int?,
                           offer_id:Int?,
                           service_id:Int?,
                           order_type:String,
                           order_title:String,
                           order_description:String,
                           order_duration_in_days:String
                           ) =api.quickOrder(category_id,company_id,offer_id,service_id, order_type, order_title, order_description, order_duration_in_days)


    suspend fun categoriesById(category_id:Int) =api.categoriesById(category_id)

    suspend fun acceptOffer(idOffer: Int,payment_method:String) =api.acceptOffer(idOffer,payment_method)
    suspend fun rejectOffer(idOffer: Int) =api.rejectOffer(idOffer)
    suspend fun getSingleOrder(idOrder: Int) =api.getSingleOrder(idOrder)

    suspend fun ratingCompany( order_id:Int,
                             company_id:Int,
                               rate:Float,
                              review:String) =api.ratingCompany(order_id, company_id, rate, review)


    suspend fun userLogOutApp() = api.userLogOutApp()
    suspend fun userDeleteAccount() = api.userDeleteAccount()
    suspend fun me() = api.me()

}

