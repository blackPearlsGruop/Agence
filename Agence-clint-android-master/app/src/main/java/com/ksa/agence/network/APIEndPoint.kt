package com.ksa.agence.network

import com.ksa.agence.entity.allOfferCompanyResponse.AllOfferCompanyResponse
import com.ksa.agence.entity.allOrdersResponse.AllOrdersResponse
import com.ksa.agence.entity.authUserResponse.AuthResponse
import com.ksa.agence.entity.authUserResponse.AuthUserResponse
import com.ksa.agence.entity.bannerResponse.BannerResponse
import com.ksa.agence.entity.categoriesByIdResponse.CategoriesByIdResponse
import com.ksa.agence.entity.categoriesResponse.CategoriesResponse
import com.ksa.agence.entity.companyResponse.CompanyResponse
import com.ksa.agence.entity.getAllFavouritesResponse.GetAllFavouritesResponse
import com.ksa.agence.entity.getSingleOrderResponse.GetSingleOrderResponse
import com.ksa.agence.entity.infoResponse.InfoResponse
import com.ksa.agence.entity.meResponse.MeResponse
import com.ksa.agence.entity.notificationResponse.NotificationResponse
import com.ksa.agence.entity.quickOrderResponse.QuickOrderResponse
import com.ksa.agence.entity.showCompaniesResponse.ShowCompaniesResponse
import com.ksa.agenceCompany.entity.cityResponse.CityResponse
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


    @POST("user/auth/register")
    suspend fun userRegister(
        @Query("name") name: String,
        @Query("phone") phone: String,
        @Query("password") password: String,
        @Query("password_confirmation") password_confirmation: String,
        @Query("accept_terms_and_conditions") accept_terms_and_conditions: Int,
        @Query("device_token") device_token: String
    ): Response<AuthUserResponse>


    @GET("city")
    suspend fun getCity(): Response<CityResponse>


    @POST("user/auth/login")
    suspend fun userLogin(
        @Query("phone") phone: String,
        @Query("device_token") device_token: String
    ): Response<AuthResponse>


    @POST("user/profile/update")
    @Multipart
    suspend fun userUpdateProfile(
        @Part("name") name: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part image: MultipartBody.Part? = null,
        @Part("_method") method: RequestBody
    ): Response<AuthUserResponse>




    @POST("user/auth/verify-otp")
    suspend fun activeCode(
        @Query("phone") phone: String,
        @Query("otp_code") otpCode: String,
        @Query("device_token") device_token: String
    ): Response<AuthUserResponse>


    @GET("user/notification")
    suspend fun getNotification(): Response<NotificationResponse>


    @POST("user/accept-company-offer/{idOffer}")
    suspend fun makePayment(@Path("idOffer")idOffer:Int,
                            @Query("payment_method")payment_method:String): Response<AuthUserResponse>


    @GET("category")
    suspend fun getCategory(): Response<CategoriesResponse>


    @GET("companies")
    suspend fun getCompanyResultFilter(@Query("account_type")account_type:String,
                                       @Query("city_id")city_id:Int,
                                       @Query("categories[]") categories: List<Int>,
                                       @Query("price")price:String,
                                       @Query("rate")rate:String,
                                       @Query("name")name:String): Response<CompanyResponse>


    @GET("companies")
    suspend fun getCompany(): Response<CompanyResponse>

    @GET("user/favourite")
    suspend fun getAllFavourites(): Response<GetAllFavouritesResponse>


    @GET("user/favourite")
    suspend fun addFavourites(@Query("company_id")company_id:Int): Response<GetAllFavouritesResponse>

    @GET("banner")
    suspend fun banner(): Response<BannerResponse>


    @GET("user/company-offer")
    suspend fun allOfferCompany(): Response<AllOfferCompanyResponse>


    @POST("user/contact-us")
    suspend fun contactUs(
        @Query("title") title: String,
        @Query("description") description: String
    ): Response<AuthUserResponse>


    @GET("page/{type}")
    suspend fun infos(@Path("type") type: Int): Response<InfoResponse>

    @GET("user/order")
    suspend fun allOrders(@Query("status[]") status: List<String>): Response<AllOrdersResponse>

    @GET("companies/{idCompanies}")
    suspend fun showCompanies(@Path("idCompanies") idCompanies: Int): Response<ShowCompaniesResponse>


    @POST("user/order")
    suspend fun quickOrder(
        @Query("category_id") category_id: Int,
        @Query("company_id") company_id: Int?,
        @Query("offer_id") offer_id: Int?,
        @Query("service_id") service_id: Int?,
        @Query("order_type") order_type: String,
        @Query("order_title") order_title: String,
        @Query("order_description") order_description: String,
        @Query("order_duration_in_days") order_duration_in_days: String,
    ): Response<QuickOrderResponse>


    @POST("user/accept-company-offer/{idOffer}")
    suspend fun acceptOffer(
        @Path("idOffer") idOffer: Int,
        @Query("payment_method") payment_method: String
    ): Response<AuthUserResponse>


    @POST("user/reject-company-offer/{idOffer}")
    suspend fun rejectOffer(@Path("idOffer") idOffer: Int): Response<AuthUserResponse>



    @GET("user/order/{idOrder}")
    suspend fun getSingleOrder(@Path("idOrder") idOrder: Int): Response<GetSingleOrderResponse>



    @DELETE("user/profile/logout")
    suspend fun userLogOutApp(): Response<AuthUserResponse>

    @DELETE("user/profile/delete-account")
    suspend fun userDeleteAccount(): Response<AuthUserResponse>


    @GET("user/profile/me")
    suspend fun me(): Response<MeResponse>



    @POST("user/rate-company")
    suspend fun ratingCompany(@Query("order_id") order_id:Int,
                              @Query("company_id") company_id:Int,
                              @Query("rate") rate:Float,
                              @Query("review") review:String): Response<AuthUserResponse>




    @GET("category/{idCategorie}")
    suspend fun categoriesById(@Path("idCategorie") idCategorie:Int): Response<CategoriesByIdResponse>


}