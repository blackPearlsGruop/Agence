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
            // 1) Try loading from AllOfferCompanyResponse (from ShowOrder / accept offer flow)
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

                if (categoryTitle != null) {
                    mViewDataBinding.tvTheService.text = categoryTitle
                }

                val taxAmount = price * (taxPercentage / 100.0)
                val totalAmount = price + taxAmount
                mViewDataBinding.tvTotal.text = "$totalAmount ${getString(R.string.r_s)}"
            } else {
                // 2) Try loading from GetSingleOrderResponse (from real ShowOrder flow)
                val myDataObject2 = arguments?.getSerializable("my_data_key2") as? GetSingleOrderResponse

                if (myDataObject2 != null && myDataObject2.data != null) {
                    idItem = myDataObject2.data!!.id
                    mViewDataBinding.tvCompanyName.text =
                        myDataObject2.data!!.offers?.getOrNull(0)?.company?.title ?: ""
                    mViewDataBinding.tvAmountBeforeTax.text =
                        "" + (myDataObject2.data!!.offers?.getOrNull(0)?.price ?: 0) + " " + getString(R.string.r_s)
                    mViewDataBinding.tvTax.text = " % " + (myDataObject2.data!!.tax_percentage ?: 0)

                    if (myDataObject2.data!!.category != null) {
                        mViewDataBinding.tvTheService.text = myDataObject2.data!!.category!!.title
                    }

                    val price = myDataObject2.data!!.offers?.getOrNull(0)?.price ?: 0
                    val taxAmount = price * ((myDataObject2.data!!.tax_percentage ?: 0) / 100.0)
                    val totalAmount = price + taxAmount
                    mViewDataBinding.tvTotal.text = "" + totalAmount + " " + getString(R.string.r_s)
                } else {
                    // 3) Fall back to plain string keys passed from OrderPreview
                    //    (companyName, serviceName, amountBeforeTax, taxPercentage, totalAmount)
                    val companyName = arguments?.getString("companyName") ?: ""
                    val serviceName = arguments?.getString("serviceName") ?: ""
                    val amountBeforeTax = arguments?.getString("amountBeforeTax") ?: ""
                    val taxPercentage = arguments?.getString("taxPercentage") ?: ""
                    val totalAmount = arguments?.getString("totalAmount") ?: ""

                    populateMockData(
                        companyName = companyName,
                        serviceName = serviceName,
                        amountBeforeTax = amountBeforeTax,
                        taxPercentage = taxPercentage,
                        totalAmount = totalAmount
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("BottomSheetPayment", "Failed to load data, using mock", e)
            populateMockData()
        }

        onClick()
    }

    /**
     * Populate the sheet from either the string-key bundle passed by the
     * OrderPreview flow, or with pure demo defaults when nothing was passed.
     * The parameters override the demo defaults one by one — so if only the
     * company name was passed, the price fields stay on demo values.
     */
    private fun populateMockData(
        companyName: String = "",
        serviceName: String = "",
        amountBeforeTax: String = "",
        taxPercentage: String = "",
        totalAmount: String = ""
    ) {
        mViewDataBinding.tvCompanyName.text =
            if (companyName.isNotEmpty()) companyName else "وكالة نجم"
        mViewDataBinding.tvTheService.text =
            if (serviceName.isNotEmpty()) serviceName else "Brand Audit & Positioning"
        mViewDataBinding.tvAmountBeforeTax.text =
            if (amountBeforeTax.isNotEmpty()) amountBeforeTax else "5,000 ${getString(R.string.r_s)}"
        mViewDataBinding.tvTax.text =
            if (taxPercentage.isNotEmpty()) "% $taxPercentage" else "% 15"
        mViewDataBinding.tvTotal.text =
            if (totalAmount.isNotEmpty()) totalAmount else "5,750 ${getString(R.string.r_s)}"
    }


    private fun initResponse() {
        viewModel.makePaymentResponse.observe(requireActivity(), Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                urlPay = it.data!!.url!!
                                val bundle = Bundle()
                                bundle.putString("URL", urlPay!!)
                                mainActivity.navController!!.navigate(R.id.paymentFragment, bundle)
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
                    showProgress(false)
                    Log.i("TestVerification", "error")
                }
                is Resource.Loading -> {
                    Log.i("TestVerification", "loading")
                    showProgress(true)
                }
            }
        })
    }


    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            initResponse()
        } else {
        }
    }


    private fun onClick() {

        mViewDataBinding.imageClose.setOnClickListener {
            dismiss()
        }

        mViewDataBinding.btnToPush.setOnClickListener {
            if (idItem != null && idItem != 0) {
                viewModel.makePayment(idItem!!, "online-payment")
            } else {
                Utilities.showToastSuccess(requireActivity(), "تم إرسال الدفعة بنجاح ✓")
                dismiss()
            }
        }
    }

    companion object {
        val TAG: String? = ""
    }

}