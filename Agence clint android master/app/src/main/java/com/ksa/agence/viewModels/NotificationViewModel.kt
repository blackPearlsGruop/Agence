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
import com.ksa.agence.entity.bannerResponse.BannerResponse
import com.ksa.agence.entity.categoriesResponse.CategoriesResponse
import com.ksa.agence.entity.companyResponse.CompanyResponse
import com.ksa.agence.entity.notificationResponse.NotificationResponse
import com.ksa.agence.entity.quickOrderResponse.QuickOrderResponse
import com.ksa.agence.entity.showCompaniesResponse.ShowCompaniesResponse
import com.ksa.agence.repository.MainRepo
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val sharedPreferences: PreferencesUtils, private val mainRepo: MainRepo
) : ViewModel() {


    val notificationResponse: MutableLiveData<Resource<NotificationResponse>> = MutableLiveData()




    fun getNotification() {
        if (Utilities.hasInternetConnection()) {
            notificationResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.getNotification()
                if (response.isSuccessful) {
                    notificationResponse.postValue(Resource.Success(response.body()!!))
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
