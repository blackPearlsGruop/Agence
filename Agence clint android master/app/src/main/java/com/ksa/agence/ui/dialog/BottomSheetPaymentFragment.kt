package com.ksa.agence.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.base.BaseBottomDialog
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentBottomSheetPaymentBinding
import com.ksa.agence.entity.allOfferCompanyResponse.AllOfferCompanyResponse
import com.ksa.agence.entity.getSingleOrderResponse.GetSingleOrderResponse
import com.ksa.agence.ui.activity.MainActivity
import com.ksa.agence.ui.fragment.home.HomeFragmentDirections
import com.ksa.agence.ui.fragment.payment.PaymentFragment
import com.ksa.agence.viewModels.HomeViewModel
import com.ksa.nafhaseha.common.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class BottomSheetPaymentFragment : BaseBottomDialog<FragmentBottomSheetPaymentBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_bottom_sheet_payment

    private var urlPay: String? = ""
    private var idItem: Int? = 0
    private lateinit var mainActivity: MainActivity
    private val viewModel: HomeViewModel by viewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity



        isCancelable = false


        try {

        }catch (e:Exception){}




        val myDataObject = arguments?.getSerializable("my_data_key") as? AllOfferCompanyResponse

        if (myDataObject != null && myDataObject.data != null && myDataObject.data.isNotEmpty()) {
            val firstData = myDataObject.data[0]

            idItem = firstData.id
            val companyTitle = firstData.company?.title ?: ""
            val categoryTitle = firstData.order?.category?.title ?: ""
            val price = firstData.price ?: 0
            val taxPercentage = firstData.order?.tax_percentage ?: 0

            mViewDataBinding.tvCompanyName.text = companyTitle
            mViewDataBinding.tvAmountBeforeTax.text = "$price ${getString(R.string.r_s)}"
            mViewDataBinding.tvTax.text = "% $taxPercentage"


            if (categoryTitle !=null)
            {
                mViewDataBinding.tvTheService.text = categoryTitle

            }

            val taxAmount = price * (taxPercentage / 100.0)
            val totalAmount = price + taxAmount

            mViewDataBinding.tvTotal.text = "$totalAmount ${getString(R.string.r_s)}"
        } else {
            val myDataObject2 =
                arguments?.getSerializable("my_data_key2") as? GetSingleOrderResponse
            // الآن يمكنك استخدام myDataObject2
            idItem = myDataObject2!!.data!!.id
            mViewDataBinding.tvCompanyName.text =
                myDataObject2!!.data!!.offers!!.get(0).company!!.title
            mViewDataBinding.tvAmountBeforeTax.text =
                "" + myDataObject2!!.data!!.offers!!.get(0).price + " " + getString(R.string.r_s)
            mViewDataBinding.tvTax.text = " % " + myDataObject2!!.data!!.tax_percentage

            if (myDataObject2!!.data!!.category !=null)
            {
                mViewDataBinding.tvTheService.text = myDataObject2!!.data!!.category!!.title

            }


            val price = myDataObject2!!.data!!.offers!!.get(0).price!!
            val taxAmount = price * (myDataObject2!!.data!!.tax_percentage!! / 100.0)
            val totalAmount = price + taxAmount

            mViewDataBinding.tvTotal.text = "" + totalAmount + " " + getString(R.string.r_s)


        }
        onClick()


    }


    private fun initResponse() {
        // resend response


        viewModel.makePaymentResponse.observe(requireActivity(), Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                // dismiss()
                                urlPay = it.data!!.url!!
//                                 openLink(requireActivity(),urlPay!!)


//                                val action =
//                                    BottomSheetPaymentFragmentDirections.actionBottomSheetPaymentFragmentToPaymentFragment(
//                                        urlPay!!
//                                    )
//                                mViewDataBinding.root.findNavController().navigate(action)

                                val bundle = Bundle()
                                bundle.putString("URL", urlPay!!)
                                  mainActivity.navController!!.navigate(R.id.paymentFragment,bundle)

                                dismiss()
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


    private fun onClick() {

        mViewDataBinding.imageClose.setOnClickListener {
            dismiss()
        }

        mViewDataBinding.btnToPush.setOnClickListener {
            viewModel.makePayment(idItem!!, "online-payment")
        }
    }

    companion object {
        val TAG: String? = ""
    }

}