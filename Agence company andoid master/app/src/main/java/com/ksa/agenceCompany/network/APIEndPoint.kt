package com.ksa.agenceCompany.network

import com.ksa.agenceCompany.entity.allOpportunitiesResponse.AllOpportunitiesResponse
import com.ksa.agenceCompany.entity.allOrdersResponse.AllOrdersResponse
import com.ksa.agenceCompany.entity.allSubscriptionResponse.AllSubscriptionResponse
import com.ksa.agenceCompany.entity.authUserResponse.AuthResponse
import com.ksa.agenceCompany.entity.authUserResponse.AuthUserResponse
import com.ksa.agenceCompany.entity.categoriesByIdResponse.CategoriesByIdResponse
import com.ksa.agenceCompany.entity.categoriesResponse.CategoriesResponse
import com.ksa.agenceCompany.entity.cityResponse.CityResponse
import com.ksa.agenceCompany.entity.getCompanyWorksResponse.GetCompanyWorksResponse
import com.ksa.agenceCompany.entity.getSingleOpportunitiesResponse.GetSingleOpportunitiesResponse
import com.ksa.agenceCompany.entity.getSingleOrderResponse.GetSingleOrderResponse
import com.ksa.agenceCompany.entity.infoResponse.InfoResponse
import com.ksa.agenceCompany.entity.meResponse.MeResponse
import com.ksa.agenceCompany.entity.meResponse.SendWithdrawalRequestResponse
import com.ksa.agenceCompany.entity.notificationResponse.NotificationResponse
import com.ksa.agenceCompany.entity.showCompaniesResponse.ShowCompaniesResponse
import com.ksa.agenceCompany.entity.subscribeToPlanResponse.SubscribeToPlanResponse
import com.ksa.agenceCompany.ui.fragment.auth.MyProfileFragment
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface APIEndPoint {


    @GET("city")
    suspend fun getCity(): Response<CityResponse>


    @POST("company/auth/register")
    @Multipart
    suspend fun userRegister(
        @Part("account_type") account_type: RequestBody,
        @Part("name") name: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("country_id") country_id: RequestBody,
        @Part("city_id") city_id: RequestBody,
        @Part("accept_terms_and_conditions") accept_terms_and_conditions: RequestBody,
        @Part("device_token") device_token: RequestBody,
        @Part image: MultipartBody.Part? = null,
        @Part("categories[]") categories: List<Int>,
        @Part("nationality_id") nationality_id: RequestBody,
    ): Response<AuthResponse>


    @POST("company/auth/login")
    suspend fun userLogin(
        @Query("phone") phone: String,
        @Query("device_token") device_token: String
    ): Response<AuthResponse>

    @POST("company/profile/update")
    @Multipart
    suspend fun userUpdateProfile(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("address") address: RequestBody,
        @Part("categories[]") categories: List<Int>,
        @Part image: MultipartBody.Part? = null,
        @Part company_background_image: MultipartBody.Part? = null,
    ): Response<AuthResponse>


    @POST("company/profile/update-consultation-price")
    suspend fun updateConsultationPrice(
        @Query("consultant_price") consultant_price: Int,
    ): Response<AuthResponse>

    @POST("company/auth/verify-otp")
    suspend fun activeCode(
        @Query("phone") phone: String,
        @Query("otp_code") otpCode: String,
        @Query("device_token") device_token: String
    ): Response<AuthUserResponse>


    @GET("company/notification")
    suspend fun getNotification(): Response<NotificationResponse>


    @GET("company/service")
    suspend fun getCategory(): Response<CategoriesResponse>


  @GET("category")
    suspend fun getCategoryNotToken(): Response<CategoriesResponse>


    @GET("company/offer")
    suspend fun getOffers(): Response<CategoriesResponse>


    @GET("company/plans")
    suspend fun getAllSubscription(): Response<AllSubscriptionResponse>


    @GET("company/opportunities/{idOpportunities}")
    suspend fun getSingleOpportunities(@Path("idOpportunities") idOpportunities: Int): Response<GetSingleOpportunitiesResponse>


    @GET("plan")
    suspend fun getAllPlan(): Response<AllSubscriptionResponse>


    @POST("company/work")
    @Multipart
    suspend fun addWorksResponse(@Part("title")title:RequestBody,
                                 @Part("description")description:RequestBody,
                                 @Part image: MultipartBody.Part? = null): Response<GetCompanyWorksResponse>


    @GET("company/work")
    suspend fun getCompanyWorks(): Response<GetCompanyWorksResponse>

    @DELETE("company/work/{idWork}")
    suspend fun deleteWorks(@Path("idWork")idWork:Int): Response<GetCompanyWorksResponse>
    @POST("company/profile/availability")
    suspend fun updateAvailability(@Query("_method")_method:String): Response<AuthResponse>


    @DELETE("company/service/{idService}")
    suspend fun deleteService(
        @Path("idService") idService: Int): Response<CategoriesByIdResponse>



    @DELETE("company/offer/{idOffer}")
    suspend fun deleteOffer(
        @Path("idOffer") idOffer: Int): Response<CategoriesByIdResponse>


    @POST("company/opportunities")
    suspend fun sendRequest(
        @Query("opportunity_id") opportunity_id: Int,
        @Query("price") price: Int
    ): Response<AllOpportunitiesResponse>


    @POST("company/order")
    suspend fun sendOffer(
        @Query("order_id") orderID: Int,
        @Query("price") amount: Int
    ): Response<AllOpportunitiesResponse>


    @POST("company/subscribe-to-plan/{plan_id}")
    suspend fun subscribeToPlan(
        @Path("plan_id") plan_id: Int,
        @Query("payment_method") payment_method: String
    ): Response<SubscribeToPlanResponse>


    @GET("company/opportunities")
    suspend fun allOpportunities(): Response<AllOpportunitiesResponse>


    @POST("company/contact-us")
    suspend fun contactUs(
        @Query("title") title: String,
        @Query("description") description: String
    ): Response<AuthUserResponse>


    @GET("page/{type}")
    suspend fun infos(@Path("type") type: Int): Response<InfoResponse>

    @GET("company/order")
    suspend fun allOrders(@Query("status[]") status: String): Response<AllOrdersResponse>

    @GET("companies/{idCompanies}")
    suspend fun showCompanies(@Path("idCompanies") idCompanies: Int): Response<ShowCompaniesResponse>


    @POST("company/service")
    @Multipart
    suspend fun storeNewService(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("service_duration_in_days") service_duration_in_days: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Response<AuthUserResponse>



    @POST("company/service/{idService}")
    suspend fun updateService(
        @Path("idService") idService: Int,
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("price") price: Int,
        @Query("service_duration_in_days") service_duration_in_days: Int,
        @Query("_method") _method: String,
    ): Response<AuthUserResponse>



    @POST("company/offer/{idOffer}")
    suspend fun updateOffer(
        @Path("idOffer") idOffer: Int,
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("price") price: Int,
        @Query("offer_duration_in_days") offer_duration_in_days: Int,
        @Query("_method") _method: String,
    ): Response<AuthUserResponse>



    @POST("company/offer")
    @Multipart
    suspend fun storeNewOffer(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("offer_duration_in_days") offer_duration_in_days: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Response<AuthUserResponse>


    @POST("company/accept-company-offer/{idOffer}")
    suspend fun acceptOffer(
        @Path("idOffer") idOffer: Int,
        @Query("payment_method") payment_method: String
    ): Response<AuthUserResponse>


    @DELETE("company/order/{idOrder}")
    suspend fun rejectOrder(@Path("idOrder") idOrder: Int): Response<AuthUserResponse>


    @GET("company/order/{idOrder}")
    suspend fun getSingleOrder(@Path("idOrder") idOrder: Int): Response<GetSingleOrderResponse>


    @DELETE("company/profile/logout")
    suspend fun userLogOutApp(): Response<AllOrdersResponse>

    @DELETE("user/profile/delete-account")
    suspend fun userDeleteAccount(): Response<AuthUserResponse>


    @GET("company/profile/me")
    suspend fun me(): Response<MeResponse>


    @POST("company/rate-company")
    suspend fun ratingCompany(
        @Query("order_id") order_id: Int,
        @Query("company_id") company_id: Int,
        @Query("rate") rate: Float,
        @Query("review") review: String
    ): Response<AuthUserResponse>


    @GET("company/service/{idService}")
    suspend fun categoriesById(@Path("idService") idService: Int): Response<CategoriesByIdResponse>


    @GET("company/offer/{idOffer}")
    suspend fun offerById(@Path("idOffer") idService: Int): Response<CategoriesByIdResponse>



    @POST("company/send-withdrawal-request")
    suspend fun sendWithdrawalRequest(@Query("name") name: String,
                                      @Query("bank_account") bank_account: String,
                                      @Query("bank_account_number") bank_account_number: String,
                                      @Query("iban_number") iban_number: String): Response<SendWithdrawalRequestResponse>


}