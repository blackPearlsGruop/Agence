package com.ksa.agenceCompany.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksa.agenceCompany.common.Resource
import com.ksa.agenceCompany.common.sharedprefrence.PreferencesUtils
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.entity.allOpportunitiesResponse.AllOpportunitiesResponse
import com.ksa.agenceCompany.entity.allOrdersResponse.AllOrdersResponse
import com.ksa.agenceCompany.entity.allSubscriptionResponse.AllSubscriptionResponse
import com.ksa.agenceCompany.entity.authUserResponse.AuthResponse
import com.ksa.agenceCompany.entity.authUserResponse.AuthUserResponse
import com.ksa.agenceCompany.entity.categoriesByIdResponse.CategoriesByIdResponse
import com.ksa.agenceCompany.entity.categoriesResponse.CategoriesResponse
import com.ksa.agenceCompany.entity.getCompanyWorksResponse.GetCompanyWorksResponse
import com.ksa.agenceCompany.entity.getSingleOpportunitiesResponse.GetSingleOpportunitiesResponse
import com.ksa.agenceCompany.entity.getSingleOrderResponse.GetSingleOrderResponse
import com.ksa.agenceCompany.entity.showCompaniesResponse.ShowCompaniesResponse
import com.ksa.agenceCompany.entity.subscribeToPlanResponse.SubscribeToPlanResponse
import com.ksa.agenceCompany.repository.MainRepo
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class HomeViewModel(
    private val sharedPreferences: PreferencesUtils, private val mainRepo: MainRepo
) : ViewModel() {


    val sendOfferResponse: MutableLiveData<Resource<AllOpportunitiesResponse>> = MutableLiveData()
    val sendRequestResponse: MutableLiveData<Resource<AllOpportunitiesResponse>> = MutableLiveData()
    val allOpportunitiesResponse: MutableLiveData<Resource<AllOpportunitiesResponse>> =
        MutableLiveData()
    val getSingleOpportunitiesResponse: MutableLiveData<Resource<GetSingleOpportunitiesResponse>> =
        MutableLiveData()
    val planResponse: MutableLiveData<Resource<AllSubscriptionResponse>> = MutableLiveData()
    val subscribeToPlanResponse: MutableLiveData<Resource<SubscribeToPlanResponse>> =
        MutableLiveData()
    val subscriptionResponse: MutableLiveData<Resource<AllSubscriptionResponse>> = MutableLiveData()
    val categoriesResponse: MutableLiveData<Resource<CategoriesResponse>> = MutableLiveData()
    val offersResponse: MutableLiveData<Resource<CategoriesResponse>> = MutableLiveData()
    val allOrdersResponse: MutableLiveData<Resource<AllOrdersResponse>> = MutableLiveData()
    val showCompaniesResponse: MutableLiveData<Resource<ShowCompaniesResponse>> = MutableLiveData()
    val storeNewServiceResponse: MutableLiveData<Resource<AuthUserResponse>> = MutableLiveData()
    val updateServiceResponse: MutableLiveData<Resource<AuthUserResponse>> = MutableLiveData()
    val updateOfferResponse: MutableLiveData<Resource<AuthUserResponse>> = MutableLiveData()
    val rejectOrderResponse: MutableLiveData<Resource<AuthUserResponse>> = MutableLiveData()
    val getSingleOrderResponse: MutableLiveData<Resource<GetSingleOrderResponse>> =
        MutableLiveData()
    val ratingCompanyResponse: MutableLiveData<Resource<AuthUserResponse>> = MutableLiveData()
    val categoriesByIdResponse: MutableLiveData<Resource<CategoriesByIdResponse>> = MutableLiveData()
    val offerByIdResponse: MutableLiveData<Resource<CategoriesByIdResponse>> = MutableLiveData()
    val deleteServiceResponse: MutableLiveData<Resource<CategoriesByIdResponse>> = MutableLiveData()
    val deleteOfferResponse: MutableLiveData<Resource<CategoriesByIdResponse>> = MutableLiveData()
    val getCompanyWorksResponse: MutableLiveData<Resource<GetCompanyWorksResponse>> = MutableLiveData()
    val addWorksResponse: MutableLiveData<Resource<GetCompanyWorksResponse>> = MutableLiveData()
    val deleteWorksResponse: MutableLiveData<Resource<GetCompanyWorksResponse>> = MutableLiveData()
    val updateAvailabilityResponse: MutableLiveData<Resource<AuthResponse>> = MutableLiveData()


    fun updateAvailability() {
        if (Utilities.hasInternetConnection()) {
            updateAvailabilityResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.updateAvailability("PUT")
                if (response.isSuccessful) {
                    updateAvailabilityResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun deleteWorks(idWork: Int) {
        if (Utilities.hasInternetConnection()) {
            deleteWorksResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.deleteWorks(idWork)
                if (response.isSuccessful) {
                    deleteWorksResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun addWorks(title:RequestBody,
                 description:RequestBody,
                 image: MultipartBody.Part? = null) {
        if (Utilities.hasInternetConnection()) {
            addWorksResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.addWorksResponse(title, description, image)
                if (response.isSuccessful) {
                    addWorksResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun getCompanyWorks() {
        if (Utilities.hasInternetConnection()) {
            getCompanyWorksResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getCompanyWorks()
                if (response.isSuccessful) {
                    getCompanyWorksResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }



    fun subscribeToPlan(plan_id: Int, payment_method: String) {
        if (Utilities.hasInternetConnection()) {
            subscribeToPlanResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.subscribeToPlan(plan_id, payment_method)
                if (response.isSuccessful) {
                    subscribeToPlanResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun deleteService(idService: Int) {
        if (Utilities.hasInternetConnection()) {
            deleteServiceResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.deleteService(idService)
                if (response.isSuccessful) {
                    deleteServiceResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun deleteOffer(idOffer: Int) {
        if (Utilities.hasInternetConnection()) {
            deleteOfferResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.deleteOffer(idOffer)
                if (response.isSuccessful) {
                    deleteOfferResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun sendOffer(orderID: Int, amount: Int) {
        if (Utilities.hasInternetConnection()) {
            sendOfferResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.sendOffer(orderID, amount)
                if (response.isSuccessful) {
                    sendOfferResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun sendRequest(opportunity_id: Int, price: Int) {
        if (Utilities.hasInternetConnection()) {
            sendRequestResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.sendRequest(opportunity_id, price)
                if (response.isSuccessful) {
                    sendRequestResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun getAllPlan() {
        if (Utilities.hasInternetConnection()) {
            planResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getAllPlan()
                if (response.isSuccessful) {
                    planResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun getSingleOpportunities(idOpportunities: Int) {
        if (Utilities.hasInternetConnection()) {
            getSingleOpportunitiesResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getSingleOpportunities(idOpportunities)
                if (response.isSuccessful) {
                    getSingleOpportunitiesResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun getAllSubscription() {
        if (Utilities.hasInternetConnection()) {
            subscriptionResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getAllSubscription()
                if (response.isSuccessful) {
                    subscriptionResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun allOpportunities() {
        if (Utilities.hasInternetConnection()) {
            allOpportunitiesResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.allOpportunities()
                if (response.isSuccessful) {
                    allOpportunitiesResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun getCategory() {
        if (Utilities.hasInternetConnection()) {
            categoriesResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getCategory()
                if (response.isSuccessful) {
                    categoriesResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun getOffers() {
        if (Utilities.hasInternetConnection()) {
            offersResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getOffers()
                if (response.isSuccessful) {
                    offersResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun allOrders(status: String) {
        if (Utilities.hasInternetConnection()) {
            allOrdersResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.allOrders(status)
                if (response.isSuccessful) {
                    allOrdersResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun storeNewService(
        title: RequestBody,
        description: RequestBody,
        price: RequestBody,
        service_duration_in_days: RequestBody,
        images: List<MultipartBody.Part>
    ) {
        if (Utilities.hasInternetConnection()) {
            storeNewServiceResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.storeNewService(
                    title,
                    description,
                    price,
                    service_duration_in_days,
                    images
                )
                if (response.isSuccessful) {
                    storeNewServiceResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun updateOffer(
        idOffer: Int,
        title: String,
        description: String,
        price: Int,
        offer_duration_in_days: Int ) {
        if (Utilities.hasInternetConnection()) {
            updateOfferResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.updateOffer(
                    idOffer,
                    title,
                    description,
                    price,
                    offer_duration_in_days
                )
                if (response.isSuccessful) {
                    updateOfferResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun updateService(
        idService: Int,
        title: String,
        description: String,
        price: Int,
        offer_duration_in_days: Int ) {
        if (Utilities.hasInternetConnection()) {
            updateServiceResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.updateService(
                    idService,
                    title,
                    description,
                    price,
                    offer_duration_in_days
                )
                if (response.isSuccessful) {
                    updateServiceResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun storeNewOffer(
        title: RequestBody,
        description: RequestBody,
        price: RequestBody,
        offer_duration_in_days: RequestBody,
        images: List<MultipartBody.Part>
    ) {
        if (Utilities.hasInternetConnection()) {
            storeNewServiceResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.storeNewOffer(
                    title,
                    description,
                    price,
                    offer_duration_in_days,
                    images
                )
                if (response.isSuccessful) {
                    storeNewServiceResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun offerById(idOffers: Int) {
        if (Utilities.hasInternetConnection()) {
            offerByIdResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.offerById(idOffers)
                if (response.isSuccessful) {
                    offerByIdResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun categoriesById(idService: Int) {
        if (Utilities.hasInternetConnection()) {
            categoriesByIdResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.categoriesById(idService)
                if (response.isSuccessful) {
                    categoriesByIdResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun rejectOrder(idOrder: Int) {
        if (Utilities.hasInternetConnection()) {
            rejectOrderResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.rejectOrder(idOrder)
                if (response.isSuccessful) {
                    rejectOrderResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun getSingleOrder(idOrder: Int) {
        if (Utilities.hasInternetConnection()) {
            getSingleOrderResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getSingleOrder(idOrder)
                if (response.isSuccessful) {
                    getSingleOrderResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun ratingCompany(
        order_id: Int,
        company_id: Int,
        rate: Float,
        review: String
    ) {
        if (Utilities.hasInternetConnection()) {
            ratingCompanyResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.ratingCompany(order_id, company_id, rate, review)
                if (response.isSuccessful) {
                    ratingCompanyResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

}
