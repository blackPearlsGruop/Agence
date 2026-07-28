package com.ksa.agenceCompany.viewModels

import android.provider.Settings
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ksa.agenceCompany.AgenceCompanyApp
import com.ksa.agenceCompany.common.Resource
import com.ksa.agenceCompany.common.sharedprefrence.PreferencesUtils
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.common.util.Utilities.Companion.convertToRequestBody
import com.ksa.agenceCompany.entity.allOrdersResponse.AllOrdersResponse
import com.ksa.agenceCompany.entity.authUserResponse.AuthResponse
import com.ksa.agenceCompany.entity.authUserResponse.AuthUserResponse
import com.ksa.agenceCompany.entity.categoriesResponse.CategoriesResponse
import com.ksa.agenceCompany.entity.cityResponse.CityResponse
import com.ksa.agenceCompany.entity.meResponse.MeResponse
import com.ksa.agenceCompany.entity.meResponse.SendWithdrawalRequestResponse
import com.ksa.agenceCompany.repository.MainRepo
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
    val registerResponse: MutableLiveData<Resource<AuthResponse>> = MutableLiveData()
    val userUpdateProfileResponse: MutableLiveData<Resource<AuthResponse>> = MutableLiveData()
    val updateConsultationPriceResponse: MutableLiveData<Resource<AuthResponse>> = MutableLiveData()
    val resendCodeResponse: MutableLiveData<Resource<AuthUserResponse>> = MutableLiveData()
    val meResponse: MutableLiveData<Resource<MeResponse>> = MutableLiveData()
    val sendWithdrawalRequestResponse: MutableLiveData<Resource<SendWithdrawalRequestResponse>> = MutableLiveData()
    val editProfileResponse: MutableLiveData<Resource<AuthUserResponse>> = MutableLiveData()
    val userLogOutAppResponse: MutableLiveData<Resource<AllOrdersResponse>> = MutableLiveData()
    val userDeleteAccountResponse: MutableLiveData<Resource<AuthUserResponse>> = MutableLiveData()
    val cityResponse: MutableLiveData<Resource<CityResponse>> = MutableLiveData()
    val categoriesNotTokenResponse: MutableLiveData<Resource<CategoriesResponse>> = MutableLiveData()


    init {
         getFirebaseToken()
        getDeviceID()
    }

    fun getDeviceID() {
        deviceID = Settings.Secure.getString(
            AgenceCompanyApp.context!!.contentResolver, Settings.Secure.ANDROID_ID
        )
        Log.d("device_ID", deviceID)
    }

    fun getDeviceName(): String {
        var deviceName = android.os.Build.MODEL
        Log.i("TestName", deviceName)
        var device = android.os.Build.BRAND
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


    fun getCategoryNotToken() {
        if (Utilities.hasInternetConnection()) {
            categoriesNotTokenResponse.postValue(Resource.Loading())

            viewModelScope.launch {
                val response = mainRepo.getCategoryNotToken()
                if (response.isSuccessful) {
                    categoriesNotTokenResponse.postValue(Resource.Success(response.body()!!))
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
                    phone,
                    firebaseToken
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
        account_type: RequestBody,
        name: RequestBody,
        phone: RequestBody,
        country_id: RequestBody,
        city_id: RequestBody,
        accept_terms_and_conditions: RequestBody,
        commercial_licence: MultipartBody.Part?,
        categories: List<Int>,
        nationality_id: RequestBody) {
        if (Utilities.hasInternetConnection()) {
            registerResponse.postValue(Resource.Loading())

            viewModelScope.launch {
                val response = mainRepo.userRegister(
                    account_type,name,phone,country_id,city_id,accept_terms_and_conditions,convertToRequestBody(firebaseToken),commercial_licence,categories,nationality_id                )
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

    fun userUpdateProfile(name: RequestBody, description: RequestBody, address: RequestBody, categories: List<Int>, company_logo: MultipartBody.Part?, company_background_image: MultipartBody.Part?) {
        if (Utilities.hasInternetConnection()) {
            userUpdateProfileResponse.postValue(Resource.Loading())

            viewModelScope.launch {
                val response = mainRepo.userUpdateProfile(name,description,address,categories,company_logo,company_background_image)
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
    fun updateConsultationPrice(consultant_price: Int) {
        if (Utilities.hasInternetConnection()) {
            updateConsultationPriceResponse.postValue(Resource.Loading())

            viewModelScope.launch {
                val response = mainRepo.updateConsultationPrice(consultant_price)
                if (response.isSuccessful) {
                    updateConsultationPriceResponse.postValue(Resource.Success(response.body()!!))
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
                    phone,
                    otpCode,
                    firebaseToken
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
    fun sendWithdrawalRequest(name: String,
           bank_account: String, bank_account_number: String,
           iban_number: String) {
        if (Utilities.hasInternetConnection()) {
            sendWithdrawalRequestResponse.postValue(Resource.Loading())
            viewModelScope.launch {
                val response = mainRepo.sendWithdrawalRequest(name, bank_account, bank_account_number, iban_number)
                if (response.isSuccessful) {
                    sendWithdrawalRequestResponse.postValue(Resource.Success(response.body()!!))
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
