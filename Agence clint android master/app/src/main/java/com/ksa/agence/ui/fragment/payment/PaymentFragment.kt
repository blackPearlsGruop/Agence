package com.ksa.agence.ui.fragment.payment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import com.ksa.agence.R
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentPaymentBinding
import com.ksa.agence.entity.getSingleOrderResponse.GetSingleOrderResponse
import com.ksa.agence.ui.activity.MainActivity


class PaymentFragment : BaseFragment<FragmentPaymentBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_payment
    private  var url: String=""

    private lateinit var mainActivity: MainActivity

    private var shouldCloseWebView = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()
        mainActivity.mViewDataBinding.tvSearch.visibility=View.GONE
        mainActivity.mViewDataBinding.tvTitleToolBar.text=getString(R.string.paying_off)

        showProgress(true)

        Handler(Looper.getMainLooper()).postDelayed({
            showProgress(false)
        }, 3000L) // تأخير لمدة ثانيتين قبل تنفيذ الأكشن

        mViewDataBinding.webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true // تمكين تخزين DOM
            loadWithOverviewMode = true
            useWideViewPort = true
        }


        if (arguments != null) {
//            val args: PaymentFragmentArgs =
//                PaymentFragmentArgs.fromBundle(requireArguments())
//            url = args.urlPay!!


            url = arguments?.getString("URL")!!
            // الآن يمكنك استخدام myDataObject2


        }

        mViewDataBinding.webView.loadUrl(url!!) // قم بتحميل الرابط المطلوب

        mViewDataBinding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?, request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()
                Log.d("WebView", "Loading URL: $url") // تسجيل الرابط
                //   https://mytime.com.sa/api/v1/clients/payBookingStatus/89?id=d42daadc-fac5-4e80-9cdd-f48465d137fb&status=paid&amount=11500&message=APPROVED
                //             تحقق من URL الخاص بزر "إلغاء عملية الدفع"
//                if (url.contains("https://mytime.com.sa/api/v1/clients/payBookingTabbyStatus/") || url.contains(
//                        "https://mytime.com.sa/api/v1/clients/payBookingStatus/")
//                ) {
//                    shouldCloseWebView = true
//                    closeFragment() // أغلق الـ Fragment فورًا
//                    return true // منع WebView من تحميل هذا الرابط
//                }
                if (url.contains("https://staging.agence.sa/api/v1/payment-return/")) {
                    shouldCloseWebView = true
                    closeFragment() // أغلق الـ Fragment فورًا

                    return true // منع WebView من تحميل هذا الرابط
                }


                return false // السماح لـ WebView بتحميل الروابط الأخرى
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
//                // تحقق من URL المستهدف عند بدء التحميل
//                if (url != null && url.contains("https://mytime.com.sa/api/v1/clients/payBookingStatus/")) {
//                    shouldCloseWebView = true
//                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // تنفيذ الأكشن عند انتهاء تحميل الصفحة إذا لزم الأمر
                if (shouldCloseWebView) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        closeFragment() // إغلاق الـ Fragment أو النشاط
                    }, 3000L) // تأخير لمدة ثانيتين قبل تنفيذ الأكشن
                }
            }
        }


        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    mainActivity.navController!!.popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)


    }

    // تعريف دالة closeFragment لإغلاق الـ Fragment
    fun closeFragment() {
        // استخدام findNavController لإغلاق الـ Fragment
        // findNavController().popBackStack()
//        activity?.onBackPressed()
        mainActivity.navController!!.navigate(R.id.menuHome)
        onDestroy()
        Utilities.showToastSuccess(requireActivity(), "تمت عملية الدفع بنجاح ")

    }

    // تعريف دالة performCustomActionOnClose لتنفيذ الأكشن المخصص

    override fun onDestroy() {
        super.onDestroy()
        mainActivity.showHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.text=getString(R.string.home)
    }



    override fun onNetworkConnectionChanged(isConnected: Boolean) {

    }

}