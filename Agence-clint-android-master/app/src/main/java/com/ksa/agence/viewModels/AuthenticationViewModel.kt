package com.ksa.agence.viewModels

import android.provider.Settings
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ksa.agence.app.AgenceApp
import com.ksa.agence.common.Resource
import com.ksa.agence.common.sharedprefrence.PreferencesUtils
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.entity.authUserResponse.AuthResponse
import com.ksa.agence.entity.authUserResponse.AuthUserResponse
import com.ksa.agence.entity.meResponse.MeResponse
import com.ksa.agence.repository.MainRepo
import com.ksa.agenceCompany.entity.cityResponse.CityResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AuthenticationViewModel(
    private val sharedPreferences: PreferencesUtils, private val mainRepo: MainRepo
) : ViewModel() {

    lateinit var deviceID: String

    var firebaseToken: String = "Device Token From Firebase"
    val loginResponse: MutableLiveData<Resource<AuthResponse>> = MutableLiveData()
    val activeCodeResponse: MutableLiveData<Resource<AuthUserResponse>> = MutableLiveData()
    val registerResponse: MutableLiveData<Resource<AuthUserResponse>> = MutableLiveData()
    val userUpdateProfileResponse: MutableLiveData<Resource<AuthUserResponse>> = MutableLiveData()
    val resendCodeResponse: MutableLiveData<Resource<AuthUserResponse>> = MutableLiveData()
    val meResponse: MutableLiveData<Resource<MeResponse>> = MutableLiveData()
    val editProfileResponse: MutableLiveData<Resource<AuthUserResponse>> = MutableLiveData()
    val userLogOutAppResponse: MutableLiveData<Resource<AuthUserResponse>> = MutableLiveData()
    val userDeleteAccountResponse: MutableLiveData<Resource<AuthUserResponse>> = MutableLiveData()
    val cityResponse: MutableLiveData<Resource<CityResponse>> = MutableLiveData()


    init {
        getFirebaseToken()
        getDeviceID()
    }

    fun getDeviceID() {
        deviceID = Settings.Secure.getString(
            AgenceApp.context!!.contentResolver, Settings.Secure.ANDROID_ID
        )
        Log.d("device_ID", deviceID)
    }

    fun getDeviceName(): String {
        val deviceName = android.os.Build.MODEL
        Log.i("TestName", deviceName)
        val device = android.os.Build.BRAND
        Log.i("TestName", device)
        return device + deviceName
    }


    fun getCity() {
        if (Utilities.hasInternetConnection()) {
            cityResponse.postValue(Resource.Loading())

            viewModelScope.launch {
                val response = mainRepo.getCity()
                if (response.isSuccessful) {
                    cityResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }


            }
        }
    }

    fun userLogin(
        phone: String
    ) {
        if (Utilities.hasInternetConnection()) {
            loginResponse.postValue(Resource.Loading())

            viewModelScope.launch {
                val response = mainRepo.userLogin(
                    phone, firebaseToken
                )
                if (response.isSuccessful) {
                    loginResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }


            }
        }
    }

    fun userRegister(
        name: String, phone: String, accept_terms_and_conditions: Int
    ) {
        if (Utilities.hasInternetConnection()) {
            registerResponse.postValue(Resource.Loading())

            viewModelScope.launch {
                val response = mainRepo.userRegister(
                    name, phone, accept_terms_and_conditions, firebaseToken
                )
                if (response.isSuccessful) {
                    registerResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }


            }
        }
    }

    fun userUpdateProfile(
        name: RequestBody,
        phone: RequestBody,
        profile_image: MultipartBody.Part?,
        method: RequestBody
    ) {
        if (Utilities.hasInternetConnection()) {
            userUpdateProfileResponse.postValue(Resource.Loading())

            viewModelScope.launch {
                val response = mainRepo.userUpdateProfile(name, phone, profile_image, method)
                if (response.isSuccessful) {
                    userUpdateProfileResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }


            }
        }
    }

    fun activeCode(
        phone: String, otpCode: String
    ) {
        if (Utilities.hasInternetConnection()) {
            activeCodeResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.activeCode(
                    phone, otpCode, firebaseToken
                )
                if (response.isSuccessful) {
                    activeCodeResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }


            }
        }
    }

    fun userLogOutApp() {
        if (Utilities.hasInternetConnection()) {
            userLogOutAppResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.userLogOutApp(

                )
                if (response.isSuccessful) {
                    userLogOutAppResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }


            }
        }
    }

    fun userDeleteAccount() {
        if (Utilities.hasInternetConnection()) {
            userDeleteAccountResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.userDeleteAccount(

                )
                if (response.isSuccessful) {
                    userDeleteAccountResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }


            }
        }
    }


    fun me() {
        if (Utilities.hasInternetConnection()) {
            meResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.me()
                if (response.isSuccessful) {
                    meResponse.postValue(Resource.Success(response.body()!!))
                    // handling if repsonse is succesfully
                    Log.i("TestLoginterVM", "${response.body()}")
                } else {
                    Resource.Error(response.message())
                    Log.i("TestLoginterVM", " error ${response.code()}")
                }


            }
        }
    }


    fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TestFireBase", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result

            firebaseToken = token
            Log.w("TestFireBase", token)


        })
    }
}
