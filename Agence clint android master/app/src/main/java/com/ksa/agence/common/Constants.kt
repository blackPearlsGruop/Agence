package com.ksa.agence.common


const val USER_DATA = "user_data"
var BASE_URL = "https://staging.agence.sa/api/v1/"

const val FIRST_TIME = "first_time"
const val LOGGED = "logged"
const val QUERY_PAGE_SIZE = 15
const val  SHARED_PREFERENCES_FILE_NAME ="APPNAME"
const val  SHARED_PREFERENCES_USER ="USERSHARED"
const val DEVICE_TOKEN = "device_token"
const val USER_TOKEN = "user_token"
const val USER_TOKEN_EXPIRY = "user_token_expiry"
const val USER_ID = "user_id"
const val USER_NAME = "user_name"
const val USER_IMAGE = "user_image"
const val USER_TYPE = "user_type"
const val USER_PROFILE_IMAGE = "user_profile_image"
const val BIRTHDATE = "birthdate"
const val AVATAR = "avatar"
const val GENDER = "gender"
const val SYMBOL = "symbol"
const val COUNTRY_IMAGE = "countryImage"
const val COUNTRY_NAME = "countryName"
const val LOGIN = "LOGIN"
const val FIRST_BUYER = "firstBuyer"
const val VERIFIRD = "verified"
const val TYPE = "type"
const val MOBILE = "mobile"
const val EMAIL = "email"
const val WHATSAPP = "whatsapp"
const val COUNTRY = "country"
const val CITY = "city"
const val LICENCE_TYPE_ID = "license_type_id"
const val ENGINEER = "engineer"
const val DOCTORS = "DOCTORS"
const val COMPANY = "company"
const val GROUP_PAYER = "group"
const val FIRST = "group"
const val LANG = "LANG"
const val BEARER = "Bearer"
const val CODE200 = 200 //Success
const val CODE403 = 403 //unAuthorized
const val CODE404 = 404 //Not Found
const val CODE422 = 422 //Validation
const val CODE500 = 500 //Server
const val USER = "1"
const val MARKET = "2"
const val UPDATE_Lat_PROVIDER = 0.0f
const val UPDATE_LONG_PROVIDER = 0.0f
var COUNT_NOTIFICATION = 0

object EmitKeyWord {
    val SEND_REQUEST_ORDER = "send-request"
    val SEND_ACCEPTED_OFFER_PRICE = "accept-offer-price"
    val COONECT = "tracking"
    val GET_OFFER_ORDER = "offer_price_order"

}

object FilesExtentin {
    val CAMERA_EXTENTION_FILE = ".jpg"
    val AUDIO_EXTENTION_FILE = ".3gp"
    val VIDEO_TEMP_FILE_NAME = ".mp4"
}

object CacheFolder {
    val APP_FOLDER = "AAIT"
    val VIDEO_FOLDER = "Video"
    val IMAGE_FOLDER = "Image"
    val DOWNLOAD_FOLDER = "Download"
    val AUDIO_FOLDER = "Audio"
}

object PermissionCode {
    val STORAGE = 1
    val ORDERIMAGE = 8
    val GOOGLE = 11
    val FACEBOOK = 22

}
