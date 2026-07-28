package com.ksa.agence.common


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.RectF
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.Group
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.karumi.dexter.BuildConfig
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.ksa.agence.R
import org.json.JSONObject
import java.io.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun convertTime(time: String): String {
    val f1: DateFormat = SimpleDateFormat("HH:mm") //HH for hour of the day (0 - 23)
    val d: Date = f1.parse(time)
    val f2: DateFormat = SimpleDateFormat("h:mm a", Locale.ENGLISH)
    return f2.format(d).toLowerCase() // "12:18am"
}

fun convertDate(time: String): String {
//        val inputPattern = "dd-MM-yyyy"
//        val outputPattern = "dd-MMM-yyyy"
    val inputPattern = "yyyy-MM-dd"
//        val outputPattern = "yyyy-MM-dd"
    val outputPattern = "dd-MMM-yyyy"

    val inputFormat = SimpleDateFormat(inputPattern, Locale.ENGLISH)
    val outputFormat = SimpleDateFormat(outputPattern, Locale.ENGLISH)

    var date: Date? = null
    var str: String? = null

    try {
        date = inputFormat.parse(time)
        str = outputFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    var stringDate = "${str!!.split("-")[0]} ${str!!.split("-")[1]} ,${str!!.split("-")[2]} "
    return stringDate!!
}

fun getStringImage(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream)
    val imageBytes = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(imageBytes, Base64.DEFAULT)
}


fun Group.addOnClickListener(listener: (view: View) -> Unit) {
    referencedIds.forEach { id ->
        rootView.findViewById<View>(id).setOnClickListener(listener)
    }
}

/*fun convertTime(timeString: String): Long {
    val time = timeString.split(":").toTypedArray()
    var res: Long = 0
    res += TimeUnit.MINUTES.toMillis(time[1].toLong())
    res += TimeUnit.HOURS.toMillis(time[0].toLong())
    return res
}*/


fun openMap(activity: Activity, lat: Double, lng: Double, zoom: Int) {
//    val gmmIntentUri = Uri.parse("geo:$lat,$lng?z=$zoom")
    val gmmIntentUri = Uri.parse("geo:<$lat>,<$lng>?q=<$lat>,<$lng>($zoom)&z=$zoom")
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    activity.startActivity(mapIntent)
}


fun OpenFaceewbookt(activity: Activity, url: String) {
    var uri = Uri.parse(url)
    try {
        val applicationInfo = activity.packageManager.getApplicationInfo("com.facebook.katana", 0)
        if (applicationInfo.enabled) {
            // http://stackoverflow.com/a/24547437/1048340
            uri = Uri.parse("fb://facewebmodal/f?href=$url")
        }
    } catch (ignored: PackageManager.NameNotFoundException) {
    }

    activity.startActivity(Intent(Intent.ACTION_VIEW, uri))
}

fun composeEmail(
    activity: Activity, emailAddress: String
) {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:" + emailAddress)
    intent.putExtra(Intent.EXTRA_EMAIL, emailAddress)
    if (intent.resolveActivity(activity.packageManager) != null) {
        activity.startActivity(intent)
    }
}

// داخل الدالة onCreate أو أي دالة أخرى تحتاج إلى إجراء مكالمة هاتفية
fun makePhoneCall(activity: Activity, phoneNumber: String?) {
    // below line is use to request permission in the current activity.
    // this method is use to handle error in runtime permissions
    Dexter.withActivity(activity) // below line is use to request the number of permissions which are required in our app.
        .withPermissions(
            Manifest.permission.CALL_PHONE,  // below is the list of permissions
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
        ) // after adding permissions we are calling an with listener method.
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                // this method is called when all permissions are granted
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    // do you work now
                    goToCall(activity, phoneNumber)
                }
                // check for permanent denial of any permission
                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied) {
                    // permission is denied permanently, we will show user a dialog message.
                    showSettingsDialog(activity)
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                list: List<PermissionRequest>,
                permissionToken: PermissionToken
            ) {
                // this method is called when user grants some permission and denies some of them.
                permissionToken.continuePermissionRequest()
            }
        }).withErrorListener { error: DexterError? ->
            // we are displaying a toast message for error message.

        } // below line is use to run the permissions on same thread and to check the permissions
        .onSameThread().check()

}

// below is the shoe setting dialog method which is use to display a dialogue message.
private fun showSettingsDialog(activity: Activity){
    // we are displaying an alert dialog for permissions
    val builder = AlertDialog.Builder(activity)

    // below line is the title for our alert dialog.
    builder.setTitle(activity.getString(R.string.need_pnermissions))

    // below line is our message for our dialog
    builder.setMessage(activity.getString(R.string.this_app_needs_permission_to_use_this_feature_You_can_grant_them_in_app_settings))
    builder.setPositiveButton(activity.getString(R.string.GOTO_SETTINGS)) { dialog, which ->
        // this method is called on click on positive button and on clicking shit button
        // we are redirecting our user from our app to the settings page of our app.
        dialog.cancel()
        // below is the intent from which we are redirecting our user.
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri =Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        activity.startActivityForResult(intent,101)
    }
    builder.setNegativeButton(activity.getString(R.string.no)) { dialog, which ->
        // this method is called when user click on negative button.
        dialog.cancel()
    }
    // below line is used to display our dialog
    builder.show()
}

fun goToCall(activity: Activity, phoneNumber: String?) {
    if (phoneNumber != null && phoneNumber.isNotEmpty()) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        if (intent.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(intent)
        }
    }
}

fun openLink(activity: Activity, link: String) {
    try {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(link)
        activity.startActivity(i)

    } catch (e: java.lang.Exception) {
    }

}

fun openEmail(activity: Activity, link: String) {
    val i = Intent(Intent.ACTION_SENDTO)
    i.putExtra(Intent.EXTRA_TEXT, link)
    activity.startActivity(i)
}

fun shareLinkApp(activity: Activity) {
    val appPackageName = BuildConfig.APPLICATION_ID
    val appName = activity.getString(R.string.app_name)
    val shareBodyText =
        activity.getString(R.string.let_me_recommend_you_this_application) + "\n" + "https://play.google.com/store/apps/details?id=$appPackageName"
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TITLE, appName)
        putExtra(Intent.EXTRA_TEXT, shareBodyText)
    }
    activity.startActivity(Intent.createChooser(sendIntent, null))
}

fun shareApp(context: Context, referralCode: String, userName: String) {
    val sendIntent = Intent()
    sendIntent.action = Intent.ACTION_SEND
    sendIntent.putExtra(
        Intent.EXTRA_TEXT, String.format(
            "%s %s %s \n%s",
            userName,
            "\n" + context.getString(R.string.invite_you_using_refer_code),
            " $referralCode ",
            "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n"
        )
    )
    sendIntent.type = "text/plain"
    sendIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(sendIntent)
}


fun openWhatsApp(activity: Activity, mobileNumber: String) {
    val url = "https://api.whatsapp.com/send?phone=+$mobileNumber"
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    activity.startActivity(intent)
}


fun formatTime(time: String): String {
    val millis = convertTime(time)
    return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(millis))
}

fun Group.enabled(isEnabled: Boolean) {
    referencedIds.forEach { id ->
        rootView.findViewById<View>(id).isEnabled = isEnabled
    }
}

fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

inline fun <T : Any> guardLet(vararg elements: T?, closure: () -> Nothing): List<T> {
    return if (elements.all { it != null }) {
        elements.filterNotNull()
    } else {
        closure()
    }
}

inline fun <T : Any> ifLet(vararg elements: T?, closure: (List<T>) -> Unit) {
    if (elements.all { it != null }) {
        closure(elements.filterNotNull())
    }
}

fun getLocationText(location: Location?): String? {
    return if (location == null) "Unknown location" else "(" + location.latitude + ", " + location.longitude + ")"
}

fun formattedDay(): String {
    val formatterMonth = SimpleDateFormat("MMMM", Locale.getDefault())
    val formatterDayName = SimpleDateFormat("EEE", Locale.getDefault())
    val formatterDay = SimpleDateFormat("dd", Locale.getDefault())
    val formatterYear = SimpleDateFormat("yyyy", Locale.getDefault())
    return formatterDayName.format(Date()) + ", " + formatterDay.format(Date()) + " " + formatterMonth.format(
        Date()
    ) + " " + formatterYear.format(Date())

}


fun CreateDate(expireAt: String): String {
    var time = expireAt
    val simpleDataFormate: SimpleDateFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'kk:mm:ss", Locale("en")
    )
    val simpleDataFormate2: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("en"))
    var date: Date = Date()
    date = simpleDataFormate.parse(time)
    time = simpleDataFormate2.format(date)
    return time
}

fun CreateDateWithName(expireAt: String): String {
    var time = expireAt
    val simpleDataFormate: SimpleDateFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'kk:mm:ss", Locale("en")
    )
    val simpleDataFormate2: SimpleDateFormat = SimpleDateFormat("dd MMM yyyy", Locale("en"))
    var date: Date = Date()
    date = simpleDataFormate.parse(time)
    time = simpleDataFormate2.format(date)
    return time
}

fun CreateTime(expireAt: String): String {
    var time = expireAt
    val simpleDataFormate: SimpleDateFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'kk:mm:ss", Locale("en")
    )
    val simpleDataFormate2: SimpleDateFormat = SimpleDateFormat("hh:mm a", Locale("en"))
    var date: Date = Date()
    date = simpleDataFormate.parse(time)
    time = simpleDataFormate2.format(date)
    return time
}

fun getDateWithFormate(expireAt: Date): String {
    var time = ""
    val simpleDataFormate: SimpleDateFormat = SimpleDateFormat(
        "yyyy-MM-dd", Locale("en")
    )
    val simpleDataFormate2: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("en"))
    var date: Date = Date()
    time = simpleDataFormate2.format(expireAt)
    return time
}


fun getDate(expireAt: String): Date {
    val simpleDataFormate2: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("en"))
    var date: Date = Date()
    date = simpleDataFormate2.parse(expireAt)
    return date
}


fun CreateDateWithTime(expireAt: String): String {
    var time = expireAt
    val simpleDataFormate: SimpleDateFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'hh:mm:ss", Locale("en")
    )
    val simpleDataFormate2: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale("en"))
    var date: Date = Date()
    date = simpleDataFormate.parse(time)
    time = simpleDataFormate2.format(date)
    return time
}


fun ResizeImg(pathOfInputImage: String): FileOutputStream? {
    var file: FileOutputStream? = null
    try {
        var inWidth = 0
        var inHeight = 0
        var `in`: InputStream? = FileInputStream(pathOfInputImage)

// decode image size (decode metadata only, not the whole image)
        var options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(`in`, null, options)
        `in`?.close()
        `in` = null

// save width and height
        inWidth = options.outWidth
        inHeight = options.outHeight

// decode full image pre-resized
        `in` = FileInputStream(pathOfInputImage)
        options = BitmapFactory.Options()
        // calc rought re-size (this is no exact resize)
        options.inSampleSize = Math.max(inWidth / 300, inHeight / 300)
        // decode full image
        val roughBitmap = BitmapFactory.decodeStream(`in`, null, options)

// calc exact destination size
        val m = Matrix()
        val inRect = RectF(
            0F, 0F, roughBitmap!!.width.toFloat(), roughBitmap!!.height.toFloat()
        )
        val outRect = RectF(0F, 0F, 300F, 300F)
        m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER)
        val values = FloatArray(9)
        m.getValues(values)

// resize bitmap
        val resizedBitmap = Bitmap.createScaledBitmap(
            roughBitmap!!,
            (roughBitmap!!.width * values[0]).toInt(),
            (roughBitmap!!.height * values[4]).toInt(),
            true
        )

// save image
        try {
            val out = FileOutputStream(pathOfInputImage)
            file = out

            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
        } catch (e: Exception) {
            Log.e("Image", e.message, e)
        }
    } catch (e: IOException) {
        Log.e("Image", e.message, e)
    }

    return file
}

//fun GetErrorResponse( throwable : HttpException) : ErrorResponse {
//    val body: ResponseBody = (throwable as HttpException).response()?.errorBody()!!
//    val jObjError: JSONObject = JSONObject(body.string())
//    val gson = Gson()
//    val jsonString = gson.toJson(jObjError)
//    Timber.e("xxxxx $jsonString")
//
//    val erorResponse=gson.fromJson(jObjError.toString(), ErrorResponse::class.java)
//    Timber.e("xxxxx ${erorResponse.toString()}")
//    return erorResponse;
//}
//
//fun LogOut(context: Context,preferencesUtils: PreferencesUtils){
//    preferencesUtils.clear()
//    preferencesUtils.login=false
//    val intent=Intent(context,ChoosdeTypeActivity::class.java)
//    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//    context.startActivity(intent)
//}
//@mViewDataBindingAdapter("image")
//fun loadImage(view: ImageView, imageUrl: String?) {
//    val circularProgressDrawable = CircularProgressDrawable(RoshanApp.context!!)
//    circularProgressDrawable.strokeWidth = 5f
//    circularProgressDrawable.centerRadius = 30f
//    circularProgressDrawable.start()
//
//    Glide.with(view.context)
//        .applyDefaultRequestOptions(
//            RequestOptions().placeholder(circularProgressDrawable).error(R.drawable.roshn_logo_ar)
//        )
//        .load(imageUrl).apply(RequestOptions().centerInside())
//        .into(view)
//}


//@mViewDataBindingAdapter("image")
//fun loadUserImage(view: ImageView, imageUrl: String?) {
//
//    val circularProgressDrawable = CircularProgressDrawable(RoshanApp.context!!)
//    circularProgressDrawable.strokeWidth = 5f
//    circularProgressDrawable.centerRadius = 30f
//    circularProgressDrawable.start()
//
//    Glide.with(view.context)
//        .applyDefaultRequestOptions(
//            RequestOptions().placeholder(circularProgressDrawable).error(R.drawable.ic_side1)
//        )
//        .load(imageUrl).apply(RequestOptions().centerInside())
//        .into(view)
//}
//}
fun setTextInputLayoutError(view: TextInputLayout, error: String?) {
    view.error = error
}

var isALog = BuildConfig.DEBUG
fun onPrintLog(o: Any?) {
    if (isALog) {
        Log.e("Response >>>>", Gson().toJson(o))
    }

}

fun onConvertObjToJson(o: Any): JSONObject? {
    val obj: JSONObject
    try {

        obj = JSONObject(Gson().toJson(o))

        Log.d("My App", obj.toString())
        return obj


    } catch (t: Throwable) {
        Log.e("My App", "Could not parse malformed JSON: \"")
        return null

    }

}






