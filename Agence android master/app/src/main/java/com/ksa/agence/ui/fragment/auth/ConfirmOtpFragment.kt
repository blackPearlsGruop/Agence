package com.ksa.agence.ui.fragment.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.ksa.agence.R
import com.ksa.agence.app.AgenceApp.Companion.pref
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.common.USER_DATA
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentConfirmOtpBinding
import com.ksa.agence.receiver.SmsBroadcastReceiver
import com.ksa.agence.ui.activity.MainActivity
import com.ksa.agence.viewModels.AuthenticationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.regex.Pattern

class ConfirmOtpFragment : BaseFragment<FragmentConfirmOtpBinding>() {

    private lateinit var countryCode: String
    private lateinit var numberPhone: String

    override fun getLayoutId(): Int = R.layout.fragment_confirm_otp
    private val viewModel: AuthenticationViewModel by viewModel()



    var countDownTimer: CountDownTimer? = null

    private var smsBroadcastReceive: SmsBroadcastReceiver? = null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            val args: ConfirmOtpFragmentArgs = ConfirmOtpFragmentArgs.fromBundle(requireArguments())
            numberPhone=args.phone
            countryCode=args.countryCode

            mViewDataBinding.tvMobile.text="+966"+numberPhone

        }


        // Init Sms Retriever >>>>
        initSmsListener()
        countDownTimer()

        onClick()
        initResponse()
    }


    private fun initResponse() {
        // resend response
        viewModel.activeCodeResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                pref.authToken = it.data!!.access_token!!
                                pref.saveUserData(requireActivity(), USER_DATA, it)

                                Utilities.showToastSuccess(requireActivity(), it.message!!)
                                openActivityAndFinish(MainActivity::class.java)

                            }
                            CODE422 -> {
                                Utilities.showToastError(requireActivity(), it.message!!)
                            }
                            else -> {
                                showProgress(false)
                                Utilities.showToastError(requireActivity(), it.message!!)

                            }
                        }
                    }
                }
                is Resource.Error -> {
                    // dismiss loading
                    showProgress(false)
                    Log.i("TestVerification", "error")

                }
                is Resource.Loading -> {
                    // show loading
                    Log.i("TestVerification", "loading")
                    showProgress(true)

                }
            }
        })

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                countDownTimer()

                            }
                            CODE422 -> {
                                Utilities.showToastError(requireActivity(), it.message!!)
                            }
                            else -> {
                                showProgress(false)
                                Utilities.showToastError(requireActivity(), it.message!!)

                            }
                        }
                    }
                }
                is Resource.Error -> {
                    // dismiss loading
                    showProgress(false)
                    Log.i("TestVerification", "error")

                }
                is Resource.Loading -> {
                    // show loading
                    Log.i("TestVerification", "loading")
                    showProgress(true)

                }
            }
        })
    }




    fun onClick() {

        mViewDataBinding.ivBack.setOnClickListener {

            mViewDataBinding.root.findNavController().popBackStack()
        }
        mViewDataBinding.tvReTransmitter.setOnClickListener {

            viewModel.userLogin("+966"+numberPhone)
        }

        mViewDataBinding.btnConfirm.setOnClickListener {

            val otp = mViewDataBinding.otpView.otp.toString().trim()

            if (otp.isEmpty()) {
                Utilities.showToastError(
                    requireActivity(),
                    getString(R.string.please_enter_the_code_sent_to_the_mobile_number)
                )
            } else {
                numberPhone="+966"+numberPhone
                viewModel.activeCode(numberPhone, otp)
            }
        }
    }


    fun countDownTimer() {
        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val sec = millisUntilFinished / 1000
                mViewDataBinding.counter.text = "$sec"

//                mViewDataBinding.sendAgain.setTextColor(
//                    activity?.resources!!.getColor(
//                        R.color.text,
//                        null
//                    )
//                )
                mViewDataBinding.tvReTransmitter.isClickable = false
                mViewDataBinding.tvReTransmitter.isEnabled = false
                mViewDataBinding.tvReTransmitter.alpha = 0.5F

                //here you can have your logic to set text to edittext
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onFinish() {
                try {
                    mViewDataBinding.tvReTransmitter.setTextColor(
                        activity?.resources!!.getColor(
                            R.color.secondary, null
                        )
                    )

                    mViewDataBinding.tvReTransmitter.isClickable = true
                    mViewDataBinding.tvReTransmitter.isEnabled = true
                    mViewDataBinding.tvReTransmitter.alpha = 1F

                } catch (e: Exception) {
                }


            }
        }.start()
    }


    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        // يتم استدعاء هذه الدالة عندما يتغير حالة الاتصال
        if (isConnected) {
            // يمكنك إجراء أي إجراءات إضافية هنا عند الاتصال بالإنترنت

        } else {
        }

    }

    private fun registerBroadcastReceiver() {
        smsBroadcastReceive = SmsBroadcastReceiver()
        smsBroadcastReceive!!.smsBroadcastReceiveListener = object : SmsBroadcastReceiver.
        SmsBroadcastReceiveListener {
            override fun onSuccess(intent: Intent?) {
                // startActivityForResult(requireActivity().intent,REQ_USER_CONSENT)
                smsActivityResultLauncher.launch(intent)
            }

            override fun onFailure() {
            }

        }
        initBroadCast()
    }


    private fun initBroadCast() {
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().registerReceiver(
                smsBroadcastReceive, intentFilter,
                Context.RECEIVER_EXPORTED
            )
        } else {
            requireActivity().registerReceiver(smsBroadcastReceive, intentFilter)
        }

    }

    private fun initSmsListener() {
        val client = SmsRetriever.getClient(requireActivity())
        client.startSmsUserConsent(null)

    }


    var smsActivityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    )
    { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            //That gives all message to us.
            // We need to get the code from inside with regex

            val message = data!!.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
            getOtpFromMessage(message!!)
            Log.d("smsAhmed", message.toString())

        }
    }


    private fun getOtpFromMessage(message: String?) {
        // This will match any 6 digit number in the message
        val pattern = Pattern.compile("(|^)\\d{4}")
        val matcher = pattern.matcher(message)
        if (matcher.find()) {
            Log.d("smsAhmed", matcher.group(0))
            mViewDataBinding.otpView.otp = matcher.group(0)
        }
    }

}