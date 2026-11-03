package com.ksa.agence.common.util

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.AlertDialog
import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.ksa.agence.R
import com.ksa.agence.app.AgenceApp
import com.ksa.agence.common.isALog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.aviran.cookiebar2.CookieBar
import java.io.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import org.aviran.cookiebar2.BuildConfig


abstract class Utilities(var context: Context) {
    var bitmap: Bitmap? = null
    var image_str: String? = null

    var isALog = BuildConfig.DEBUG


    /**
     * PERMISSIONS
     */
    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */




    private fun showSettingsDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS") { dialog, which ->
            dialog.cancel()
            openSettings(context)
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        (context as Activity).startActivityForResult(intent, 101)
    }


    private fun openCamera(context: Context) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        (context as Activity).startActivityForResult(intent, 100)
    }

    private fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            val runningProcesses = am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
        } else {
            val taskInfo = am.getRunningTasks(1)
            var componentInfo: ComponentName? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                componentInfo = taskInfo[0].topActivity
            }
            if (componentInfo!!.packageName == context.packageName) {
                isInBackground = false
            }
        }
        return isInBackground
    }

    fun getStringImage(bmp: Bitmap?): String {
        val baos = ByteArrayOutputStream()
        bmp!!.compress(Bitmap.CompressFormat.JPEG, 85, baos)
        val imageBytes = baos.toByteArray()

        //"data:image/jpeg;base64,"
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    // from camera
    fun onCaptureImageResult(data: Intent?): Bitmap? {
        if (data != null && data.hasExtra("data")) {
            val thumbnail = data.extras!!["data"] as Bitmap?
            val bytes = ByteArrayOutputStream()
            if (thumbnail != null) {
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 85, bytes)
                var width = thumbnail.width
                var height = thumbnail.height
                val bitmapRatio = width.toFloat() / height.toFloat()
                if (bitmapRatio > 0) {
                    width = 300
                    height = (width / bitmapRatio).toInt()
                } else {
                    height = 300
                    width = (height * bitmapRatio).toInt()
                }
                bitmap = Bitmap.createScaledBitmap(thumbnail, width, height, true)
            }


            //   avatar.setImageBitmap(bitmap);
            image_str = getStringImage(bitmap)
            return bitmap
        }
        return null
    }


    fun DialogUplaodImage(context: Activity) {
//        val item = arrayOf(context.resources.getString(com.waiteg.etamn.R.string.takePhoto),context.resources.getString(
//            com.waiteg.etamn.R.string.chooseFromGallery),context.resources.getString(
//            com.waiteg.etamn.R.string.Cancel))
//        val builder= AlertDialog.Builder(context)
//        builder.setTitle(context.resources.getString(com.waiteg.etamn.R.string.upladeImg))
//        builder.setItems(item, DialogInterface.OnClickListener { dialog, item ->
//            if (item == 0) {
//                takePhoto()
//            } else if (item == 1) {
//                chooseImageGallery()
//            } else if (item == 2) {
//                dialog.dismiss()
//            }
//        })
//        builder.create()
//        builder.show()

    }

    fun chooseImageGallery() {
        val galleryintent = Intent(Intent.ACTION_PICK)
        galleryintent.type = "image/*"
        (context as Activity).startActivityForResult(galleryintent, 0)
    }

    fun takePhoto() {
        val cameraIntent = Intent()
        cameraIntent.action = MediaStore.ACTION_IMAGE_CAPTURE
        (context as Activity).startActivityForResult(cameraIntent, 1)
    }


    companion object {

        private var dialogNoInternet: Dialog? = null
        open var image_url: String? = null


        private const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123

        //Email Pattern
        private const val EMAIL_PATTERN =
            ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        private const val FULL_NAME =
            "^[a-zA-Z\\s-ءاأإآؤئبتثجحخدذرزسشصضطظعغفقكلمنهويةى]{4,}(?: [a-zA-Z\\s-ءاأإآؤئبتثجحخدذرزسشصضطظعغفقكلمنهويةى]+){1,3}$"
        private const val MY_PERMISSIONS_REQUEST_Write_EXTERNAL_STORAGE = 108
        private var pattern: Pattern? = null
        private var matcher: Matcher? = null

        /**
         * Validate Email with regular expression
         *
         * @ email
         * Return true for Valid Email and false for Invalid Email
         */


        public fun hasInternetConnection(): Boolean {
            val connectivityManager = AgenceApp.context!!.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val activeNetwork = connectivityManager.activeNetwork ?: return false
                val capabilities =
                    connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
                return when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                connectivityManager.activeNetworkInfo?.run {
                    return when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
            return false
        }

        fun isValidMobile(phone: String): Boolean {
            val check: Boolean
            check = !Pattern.matches("^\\+[0-9]$", phone) && phone.length > 6
            return check
        }

        /**
         * Checks for Null String object
         *
         * @ txt
         * return true for not null and false for null String object
         */
        fun isNotNull(txt: String?): Boolean {
            return txt != null && txt.trim { it <= ' ' }.length > 0
        }

        fun passLength(s1: String): Boolean {
            return s1.length < 8
        }

        fun userNameLength(s1: String): Boolean {
            return s1.length > 3
        }

        fun isMatching(s1: String, s2: String): Boolean {
            return s1 == s2
        }

        fun stringEncryption(email_phone: String): String {
            return email_phone.replace("(^[^@]{3}|(?!^)\\G)[^@]".toRegex(), "$1*")
        }

        /**
         * UI UTILS
         */
        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if (activity.currentFocus != null) {
                // or use  getWindow().getDecorView().getRootView().getWindowToken()
                inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
            }
        }

        fun setupUI(view: View, activity: Activity) {

            // Set up touch listener for non-text box views to hide keyboard.
            if (view !is EditText) {
                view.setOnTouchListener { v, event ->
                    hideSoftKeyboard(activity)
                    false
                }
            }

            //If a layout container, iterate over children and seed recursion.
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    val innerView = view.getChildAt(i)
                    setupUI(innerView, activity)
                }
            }
        }

        /**
         * DIALOGS
         */
        fun dialog_error(context: Context?, content: String?) {}
        fun dialog_Show(context: Context?) {}
        fun dialog_success(context: Context?) {}
        fun checkPermission_Write(context: Context?): Boolean {
            val currentAPIVersion = Build.VERSION.SDK_INT
            return if (currentAPIVersion >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            (context as Activity?)!!, Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    ) {
                        val alertBuilder = AlertDialog.Builder(context)
                        alertBuilder.setCancelable(true)
                        alertBuilder.setTitle("Permission necessary")
                        alertBuilder.setMessage("External storage permission is necessary")
                        alertBuilder.setPositiveButton(R.string.yes) { dialog, which ->
                            ActivityCompat.requestPermissions(
                                (context as Activity?)!!, arrayOf(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ), MY_PERMISSIONS_REQUEST_Write_EXTERNAL_STORAGE
                            )
                        }
                        val alert = alertBuilder.create()
                        alert.show()
                    } else {
                        ActivityCompat.requestPermissions(
                            (context as Activity?)!!,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_Write_EXTERNAL_STORAGE
                        )
                    }
                    false
                } else {
                    true
                }
            } else {
                true
            }
        }

        fun checkPermission(context: Context?): Boolean {
            val currentAPIVersion = Build.VERSION.SDK_INT
            return if (currentAPIVersion >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        context!!, Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            (context as Activity?)!!, Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    ) {
                        val alertBuilder = AlertDialog.Builder(context)
                        alertBuilder.setCancelable(true)
                        alertBuilder.setTitle("Permission necessary")
                        alertBuilder.setMessage("External storage permission is necessary")
                        alertBuilder.setPositiveButton(R.string.yes) { dialog, which ->
                            ActivityCompat.requestPermissions(
                                (context as Activity?)!!, arrayOf(
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                ), MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                            )
                        }
                        val alert = alertBuilder.create()
                        alert.show()
                    } else {
                        ActivityCompat.requestPermissions(
                            (context as Activity?)!!,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                        )
                    }
                    false
                } else {
                    true
                }
            } else {
                true
            }
        }

        // circle images
        // normal sqare images
        fun calculateNoOfColumns(context: Context): Int {
            val displayMetrics = context.resources.displayMetrics
            val dpWidth = displayMetrics.widthPixels / displayMetrics.density
            return (dpWidth / 80).toInt()
        }

        fun ScrollToPosition(scrollView: NestedScrollView, view: View) {
            scrollView.smoothScrollTo(0, view.top)
        }

        fun getText(text: TextInputEditText): String {
            return text.text.toString().trim { it <= ' ' }
        }

        fun setError(view: TextInputLayout, text: String?): Boolean {
            view.error = text
            return false
        }


        fun getSharedPreferences(ctx: Context): SharedPreferences {
            return ctx.getSharedPreferences("Agence", Context.MODE_PRIVATE)
        }

        fun cacheString(ctx: Context, k: String?, v: String?) {
            val prefs = getSharedPreferences(ctx)
            prefs.edit().putString(k, v).apply()
        }

        fun getCachedString(ctx: Context, k: String?, defaultValue: String?): String? {
            val prefs = getSharedPreferences(ctx)
            return prefs.getString(k, defaultValue)
        }

        fun cacheInteger(ctx: Context, k: String?, v: Int) {
            val prefs = getSharedPreferences(ctx)
            prefs.edit().putInt(k, v).apply()
        }

        fun getCachedInteger(ctx: Context, k: String?, defaultValue: Int): Int {
            val prefs = getSharedPreferences(ctx)
            return prefs.getInt(k, defaultValue)
        }

        fun cacheBoolean(ctx: Context, k: String?, v: Boolean) {
            val prefs = getSharedPreferences(ctx)
            prefs.edit().putBoolean(k, v).apply()
        }

        fun getCachedBoolean(ctx: Context, k: String?, defaultValue: Boolean): Boolean {
            val prefs = getSharedPreferences(ctx)
            return prefs.getBoolean(k, defaultValue)
        } //    public static void LogOut(Context context){
        //        Utilities.cacheBoolean(context,Const.LOGIN,false);
        //
        //        Intent intent=new Intent(context, LoginRegisterActivity.class);
        //        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        //        context.startActivity(intent);
        //    }
        //
        //
        //
        //    public static void DialogSelectLanguage(final Context context)
        //    {
        //        final String items[] = {"English", "عربي", context.getResources().getString(R.string.cancel)};
        //
        //        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        //        ab.setTitle(context.getResources().getString(R.string.chooseimage));
        //        ab.setItems(items, new DialogInterface.OnClickListener() {
        //            public void onClick(DialogInterface d, int choice) {
        //                if (choice == 0) {
        //                    Utilities.cacheString(context,Const.LANG,"en");
        //                } else if (choice == 1) {
        //                    Utilities.cacheString(context,Const.LANG,"ar");
        //                } else if (choice == 2) {
        //                    d.dismiss();
        //                }
        //            }
        //        });
        //        ab.show();
        //    }


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

            var stringDate =
                "${str!!.split("-")[0]} ${str!!.split("-")[1]} ,${str!!.split("-")[2]} "
            return stringDate!!
        }


        fun convertFileToMultipart(image: File?, key: String): MultipartBody.Part? {
            if (image != null) {
                val requestFile: RequestBody =
                    image.asRequestBody("multipart/form-data ; charset=UTF-8".toMediaTypeOrNull())
                val body1 = MultipartBody.Part.createFormData(key, image.getName(), requestFile)
                return body1
            } else return null

        }

        fun convertFileToMultipartList(image: ArrayList<File>): List<MultipartBody.Part> {
            val im: ArrayList<MultipartBody.Part> = ArrayList()
            image.forEach {
                val requestFile: RequestBody =
                    it.asRequestBody("multipart/form-data ; charset=UTF-8".toMediaTypeOrNull())
                val body1 = MultipartBody.Part.createFormData("images[]", it.name, requestFile)
                im.add(body1)
            }
            return im
        }


        fun convertToMultiPart1(context: Context, uri: Uri, file: File): MultipartBody.Part {
            val requestFile = RequestBody.create(
                context.contentResolver.getType(uri)!!.toMediaTypeOrNull(), file
            )
            return MultipartBody.Part.createFormData("filess[]", file!!.name, requestFile)
        }


        fun ConverToMultiPart2(file: File): MultipartBody.Part {
            val requestFile: RequestBody =
                file.asRequestBody("multipart/form-data ; charset=UTF-8".toMediaTypeOrNull())
            val body1 = MultipartBody.Part.createFormData("filess[]", file.name, requestFile)
            return body1
        }

//        fun ConvertToMultiPartPDF(
//            context: Context,
//            pdfFile: ArrayList<MultiPartItem>
//        ): ArrayList<MultipartBody.Part> {
//            var multiPartList: ArrayList<MultipartBody.Part> = ArrayList()
//
// //           // for loop for first list
////            for (i in 0 until imagesFile.size) {
////                var item = imagesFile[i]
////                val requestFile = RequestBody.create(
////                    context.contentResolver.getType(item.uri!!)!!.toMediaTypeOrNull(),
////                    item.file!!
////                )
////                val multiPart =
////                    MultipartBody.Part.createFormData("images[]", item.file!!.name, requestFile)
////                multiPartList.add(multiPart)
////            }
//
//            // for loop for first list
//            for (i in 0 until pdfFile.size) {
//                var item = pdfFile[i]
//                val requestFile = RequestBody.create(
//                    context.contentResolver.getType(item.uri!!)!!.toMediaTypeOrNull(),
//                    item.file!!
//                )
//                val multiPart =
//                    MultipartBody.Part.createFormData("images[]", item.file!!.name, requestFile)
//                multiPartList.add(multiPart)
//            }
//            return multiPartList
//        }


        fun convertToRequestBody(part: String): RequestBody {
            val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            return part.toRequestBody(mediaType)

//            try {
//                if (part != "") {
//                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), part)
//                } else {
//                    null
//                }
//            } catch (e: Exception) {
//                null
//            }
//            return data.toRequestBody(mediaType)
        }

        fun ResizeImage(path: String?): FileOutputStream? {
            var file: FileOutputStream? = null
            try {
                var inWidth = 0
                var inHeight = 0
                var `in`: InputStream? = FileInputStream(path)
                var options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeStream(`in`, null, options)
                `in`!!.close()
                `in` = null
                inWidth = options.outWidth
                inHeight = options.outHeight
                `in` = FileInputStream(path)
                options = BitmapFactory.Options()
                // calc rought re-size (this is no exact resize)
                options.inSampleSize = Math.max(inWidth / 300, inHeight / 300)
                val roughBitmap = BitmapFactory.decodeStream(`in`, null, options)
                val out = FileOutputStream(path)
                roughBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, out)
                file = out
            } catch (e: java.lang.Exception) {
            }
            return file
        }


        fun isLocal(url: String?): Boolean {
            return if (url != null && !url.startsWith("http://") && !url.startsWith("https://")) {
                true
            } else false
        }


//        fun openImagePickerGallery(activity: Activity, imageView: ImageView?) {
//            ImagePicker.with(activity)
//                .galleryOnly()
//                .crop()	    			//Crop image(Optional), Check Customization for more option
//                .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
//                .start()
//        }


        fun onPermission(activity: Activity) {
            val perms = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CONTACTS,
//                Manifest.permission.RECORD_AUDIO
            )
            ActivityCompat.requestPermissions(
                activity!!, perms, 100
            )
        }


        fun logOutApp(activity: Activity) {
            AgenceApp.pref.clearSharedPref()
            //   val mainIntent = Intent(activity, AuthActivity::class.java)
            //  activity.startActivity(mainIntent)
            activity.finishAffinity()
            //  Animatoo.animateZoom(activity);
        }





        fun onLoadImageFromUrl(
            context: Context,
            URl: String?,
            imageView: ImageView?,
//            ProgressBar: View
        ) {
            Glide.with(context).load(URl)
                .error(R.mipmap.logo_app).into(imageView!!)
//            ProgressBar.visibility = View.GONE


//        Glide.with(context)
//                .load(URl)
//                .transition(withCrossFade(new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()))
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        ProgressBar.setVisibility(View.GONE);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        ProgressBar.setVisibility(View.GONE);
//                        return false;
//                    }
//
//                })
//                .error(R.drawable.logo)
//                .into(imageView);
        }




        fun showDialogNoInternet(activity: Activity) {
            dialogNoInternet = Dialog(activity, R.style.customDialogTheme)
            dialogNoInternet!!.setCancelable(false)
            val inflater = activity.layoutInflater
            val v: View = inflater.inflate(R.layout.dialog_check_internet, null)
            dialogNoInternet!!.setContentView(v)
            val window = dialogNoInternet!!.getWindow()
            window!!.setLayout(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )

            dialogNoInternet!!.show()

        }

        fun dismissDialogNoInternet() {
            // قم بفحص إذا كان الديالوج ليس null قبل استخدامه
            dialogNoInternet?.let {
                if (it.isShowing) {
                    it.dismiss()
                }
            }

        }


        fun showToastSuccess(activity: Activity, message: String) {
            CookieBar.build(activity).setTitle(R.string.title_CookieBar)
                .setTitleColor(R.color.white).setIcon(R.drawable.icon_done)
                .setBackgroundColor(R.color.green)
                //.setIconAnimation(R.animator.spin)
                .setMessage(message)
//                .setAction(R.string.action_text) {
//                    // Action code - Do something
//                }
                .setDuration(3000) // 5 seconds
                .show()

        }

        fun showToastError(activity: Activity, message: String) {
            CookieBar.build(activity).setTitle(R.string.title_CookieBar_error)
                .setTitleColor(R.color.white).setIcon(R.drawable.icon_close)
                .setBackgroundColor(R.color.red)
                //.setIconAnimation(R.animator.spin)
                .setMessage(message)
//                .setAction(R.string.action_text) {
//                    // Action code - Do something
//                }
                .setDuration(3000) // 5 seconds
                .show()

        }

        fun getFilePathName(filename: String, fileextintion: String, FolderTypeName: String, AppFolder: String): File {
            Log.e("FullFileName:", File(
                getDownloadFolderPath(
                    FolderTypeName,
                    AppFolder
                ) + "/" + filename + fileextintion).absolutePath)
            return File(
                getDownloadFolderPath(
                    FolderTypeName,
                    AppFolder
                ) + "/" + filename + fileextintion)
        }



        fun getDownloadFolderPath(FolderTypeName: String, AppFolder: String): String {
            val folder = File(getFilesFolderPath(AppFolder), FolderTypeName)
            if (!folder.exists()) {
                folder.mkdirs()
            }
            Log.e("FolderPath:", folder.absolutePath)
            return folder.absolutePath
        }


        fun getFilesFolderPath(AppFolder: String): String {
            val folder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), AppFolder)
            if (!folder.exists()) {
                folder.mkdirs()
            }
            Log.e("FolderPath:", folder.absolutePath)
            return folder.absolutePath
        }

        fun generateRandomString(length: Int): String {
            val AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_"
            val rnd = Random()
            val sb = StringBuilder(length)
            for (i in 0 until length)
                sb.append(AB[rnd.nextInt(AB.length)])
            return sb.toString()
        }


        fun onPrintLog(o: Any?) {
            if (isALog) {
                Log.e("Response >>>>", Gson().toJson(o))
            }

        }

    }
}
