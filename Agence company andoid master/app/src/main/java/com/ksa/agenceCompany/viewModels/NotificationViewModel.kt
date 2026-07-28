package com.ksa.agenceCompany.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksa.agenceCompany.common.Resource
import com.ksa.agenceCompany.common.sharedprefrence.PreferencesUtils
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.entity.notificationResponse.NotificationResponse
import com.ksa.agenceCompany.repository.MainRepo
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
