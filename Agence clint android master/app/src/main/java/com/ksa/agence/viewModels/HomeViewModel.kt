package com.ksa.agence.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksa.agence.common.Resource
import com.ksa.agence.common.sharedprefrence.PreferencesUtils
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.entity.allOfferCompanyResponse.AllOfferCompanyResponse
import com.ksa.agence.entity.allOrdersResponse.AllOrdersResponse
import com.ksa.agence.entity.authUserResponse.AuthUserResponse
import com.ksa.agence.entity.bannerResponse.BannerResponse
import com.ksa.agence.entity.categoriesByIdResponse.CategoriesByIdResponse
import com.ksa.agence.entity.categoriesResponse.CategoriesResponse
import com.ksa.agence.entity.companyResponse.CompanyResponse
import com.ksa.agence.entity.getAllFavouritesResponse.GetAllFavouritesResponse
import com.ksa.agence.entity.getSingleOrderResponse.GetSingleOrderResponse
import com.ksa.agence.entity.quickOrderResponse.QuickOrderResponse
import com.ksa.agence.entity.showCompaniesResponse.ShowCompaniesResponse
import com.ksa.agence.repository.MainRepo
import kotlinx.coroutines.launch

class HomeViewModel(
    private val sharedPreferences: PreferencesUtils, private val mainRepo: MainRepo
) : ViewModel() {


    val categoriesResponse: MutableLiveData<Resource<CategoriesResponse>> = MutableLiveData()
    val companyResponse: MutableLiveData<Resource<CompanyResponse>> = MutableLiveData()
    val companyResultFilterResponse: MutableLiveData<Resource<CompanyResponse>> = MutableLiveData()
    val bannerResponse: MutableLiveData<Resource<BannerResponse>> = MutableLiveData()
    val allOfferCompanyResponse: MutableLiveData<Resource<AllOfferCompanyResponse>> = MutableLiveData()
    val allOrdersResponse: MutableLiveData<Resource<AllOrdersResponse>> = MutableLiveData()
    val showCompaniesResponse: MutableLiveData<Resource<ShowCompaniesResponse>> = MutableLiveData()
    val quickOrderResponse: MutableLiveData<Resource<QuickOrderResponse>> = MutableLiveData()
    val acceptOfferResponse: MutableLiveData<Resource<AuthUserResponse>> = MutableLiveData()
    val rejectOfferResponse: MutableLiveData<Resource<AuthUserResponse>> = MutableLiveData()
    val getSingleOrderResponse: MutableLiveData<Resource<GetSingleOrderResponse>> = MutableLiveData()
    val getAllFavouritesResponse: MutableLiveData<Resource<GetAllFavouritesResponse>> = MutableLiveData()
    val addFavouritesResponse: MutableLiveData<Resource<GetAllFavouritesResponse>> = MutableLiveData()
    val ratingCompanyResponse: MutableLiveData<Resource<AuthUserResponse>> = MutableLiveData()
    val categoriesByIdResponse: MutableLiveData<Resource<CategoriesByIdResponse>> = MutableLiveData()
    val makePaymentResponse: MutableLiveData<Resource<AuthUserResponse>> = MutableLiveData()



    fun makePayment(idOffer: Int,payment_method:String) {
        if (Utilities.hasInternetConnection()) {
            makePaymentResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.makePayment(idOffer,payment_method)
                if (response.isSuccessful) {
                    makePaymentResponse.postValue(Resource.Success(response.body()!!))
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


    fun getCompanyResultFilter(account_type:String,city_id:Int,
                               categories: List<Int>,
                               price:String,
                               rate:String,
                               name:String) {
        if (Utilities.hasInternetConnection()) {
            companyResultFilterResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getCompanyResultFilter(account_type,city_id, categories, price, rate, name)
                if (response.isSuccessful) {
                    companyResultFilterResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }
    fun getCompany() {
        if (Utilities.hasInternetConnection()) {
            companyResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getCompany()
                if (response.isSuccessful) {
                    companyResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun getAllFavourites() {
        if (Utilities.hasInternetConnection()) {
            getAllFavouritesResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getAllFavourites()
                if (response.isSuccessful) {
                    getAllFavouritesResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun addFavourites(company_id:Int) {
        if (Utilities.hasInternetConnection()) {
            addFavouritesResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.addFavourites(company_id)
                if (response.isSuccessful) {
                    addFavouritesResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun getBanner() {
        if (Utilities.hasInternetConnection()) {
            bannerResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getBanner()
                if (response.isSuccessful) {
                    bannerResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun allOfferCompany() {
        if (Utilities.hasInternetConnection()) {
            allOfferCompanyResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.allOfferCompany()
                if (response.isSuccessful) {
                    allOfferCompanyResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


  fun allOrders(status:List<String>) {
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

  fun showCompanies(idCompanies:Int) {
        if (Utilities.hasInternetConnection()) {
            showCompaniesResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.showCompanies(idCompanies)
                if (response.isSuccessful) {
                    showCompaniesResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVMFav", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVMFav", " error ${response.code()}")
                }

            }
        }
    }

    fun quickOrder(category_id:Int,
                   company_id:Int?,
                   offer_id:Int?,
                   service_id:Int?,
                   order_type:String,
                      order_title:String,
                      order_description:String,
                      order_duration_in_days:String) {
        if (Utilities.hasInternetConnection()) {
            quickOrderResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.quickOrder(
                    category_id,
                    if (company_id != 0) company_id else null,
                    if (offer_id != 0) offer_id else null,
                    if (service_id != 0) service_id else null,
                    order_type,
                    order_title,
                    order_description,
                    order_duration_in_days)
                if (response.isSuccessful) {
                    quickOrderResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun categoriesById(category_id:Int) {
        if (Utilities.hasInternetConnection()) {
            categoriesByIdResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.categoriesById(category_id)
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


    fun acceptOffer(idOffer:Int,payment_method:String) {
        if (Utilities.hasInternetConnection()) {
            acceptOfferResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.acceptOffer(idOffer,payment_method)
                if (response.isSuccessful) {
                    acceptOfferResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }

    fun rejectOffer(idOffer:Int) {
        if (Utilities.hasInternetConnection()) {
            rejectOfferResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.rejectOffer(idOffer)
                if (response.isSuccessful) {
                    rejectOfferResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }
    fun getSingleOrder(idOrder:Int) {
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
        order_id:Int,
        company_id:Int,
        rate: Float,
        review:String) {
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
