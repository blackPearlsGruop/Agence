package com.ksa.agence.member.ui.fragment.setting

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.ksa.agence.R
import com.ksa.agence.member.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.member.common.util.Utilities
import com.ksa.agence.databinding.FragmentMoneyBinding
import com.ksa.agence.databinding.FragmentTermsOfUseBinding
import com.ksa.agence.member.ui.activity.AuthActivity
import com.ksa.agence.member.ui.activity.MainActivity
import com.ksa.agence.member.viewModels.AuthenticationViewModel
import com.ksa.agence.member.viewModels.InfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoneyFragment : BaseFragment<FragmentMoneyBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_money
    private val viewModel: AuthenticationViewModel by viewModel()


    private lateinit var mainActivity: MainActivity


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.terms_and_conditions)


        mViewDataBinding.btnSave.setOnClickListener {

            val price=mViewDataBinding.tvPrice.text.toString()
            if (price.isEmpty())
            {
                mViewDataBinding.tvPrice.error=getString(R.string.member_this_item_is_required)
            }
            else{
                viewModel.updateConsultationPrice(price.toInt())

            }

        }
    }


    private fun initResponse() {
        // resend response
        viewModel.updateConsultationPriceResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                showDialogSuccess()
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



    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        // يتم استدعاء هذه الدالة عندما يتغير حالة الاتصال
        if (isConnected) {
            // يمكنك إجراء أي إجراءات إضافية هنا عند الاتصال بالإنترنت
            initResponse()

        } else {
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity.showHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.member_setting)

    }



    fun showDialogSuccess() {
        val dialog = Dialog(requireActivity(), R.style.customDialogTheme)
        dialog.setCancelable(false)
        val inflater = requireActivity().layoutInflater
        val v: View = inflater.inflate(R.layout.member_dialog_success_order, null)
        dialog.setContentView(v)

        val ivClose = dialog.findViewById<ImageView>(R.id.imageViewClose)
        val orderTitle = dialog.findViewById<TextView>(R.id.textViewTitel)
        val orderNo = dialog.findViewById<TextView>(R.id.tv_order_number)

        orderTitle.text = getString(R.string.saved_successfully)
        orderNo.visibility = View.GONE

        ivClose.setOnClickListener {
            dialog.dismiss()
            mainActivity.navController!!.popBackStack()
        }

        dialog.show()

    }


}