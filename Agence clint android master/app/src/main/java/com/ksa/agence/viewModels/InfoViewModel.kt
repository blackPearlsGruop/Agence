package com.ksa.agence.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksa.agence.common.Resource
import com.ksa.agence.common.sharedprefrence.PreferencesUtils
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.entity.authUserResponse.AuthUserResponse
import com.ksa.agence.entity.infoResponse.InfoResponse
import com.ksa.agence.repository.MainRepo
import kotlinx.coroutines.launch

class InfoViewModel(
    private val sharedPreferences: PreferencesUtils, private val mainRepo: MainRepo
) : ViewModel() {

       val infoResponse: MutableLiveData<Resource<InfoResponse>> = MutableLiveData()
    val contactUsResponse: MutableLiveData<Resource<AuthUserResponse>> = MutableLiveData()


    fun info(type: Int) {
        if (Utilities.hasInternetConnection()) {
            infoResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.infos(type)
                if (response.isSuccessful) {
                    infoResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }

            }
        }
    }


    fun contactUs(titleMessage: String, note: String) {
        if (Utilities.hasInternetConnection()) {
            contactUsResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.contactUs(titleMessage, note)
                if (response.isSuccessful) {
                    contactUsResponse.postValue(Resource.Success(response.body()!!))
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