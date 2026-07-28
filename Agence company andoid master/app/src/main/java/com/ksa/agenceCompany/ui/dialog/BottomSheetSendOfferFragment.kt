package com.ksa.agenceCompany.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.adapter.MultiSelectCategoriesAdapter
import com.ksa.agenceCompany.base.BaseBottomDialog
import com.ksa.agenceCompany.common.CODE200
import com.ksa.agenceCompany.common.CODE422
import com.ksa.agenceCompany.common.Resource
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.databinding.FragmentBottomSheetSendOfferBinding
import com.ksa.agenceCompany.entity.categoriesResponse.DataCategoriesResponse
import com.ksa.agenceCompany.ui.activity.MainActivity
import com.ksa.agenceCompany.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class BottomSheetSendOfferFragment : BaseBottomDialog<FragmentBottomSheetSendOfferBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_bottom_sheet_send_offer
    private  var dialogSuccess: Dialog?=null
    private var idOrder: Int = 0
    private lateinit var resultIDS: List<DataCategoriesResponse>
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var mainActivity: MainActivity
    private var isDialogShowing = false


    lateinit var multiSelectCategoriesAdapter: MultiSelectCategoriesAdapter
    lateinit var listData: ArrayList<DataCategoriesResponse>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity


        listData = ArrayList()
        initResponse()
        onClick()

        if (arguments != null) {

            val args: BottomSheetSendOfferFragmentArgs =
                BottomSheetSendOfferFragmentArgs.fromBundle(requireArguments())
            idOrder = args.idOrder

            onClick()

        }

    }

    private fun initResponse() {

        homeViewModel.sendOfferResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                dialogSuccess("")

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
                    Log.i("TestVerification", "Error: ${result.message}")
                }

                is Resource.Loading -> {
                    Log.i("TestVerification", "Loading...")
                    showProgress(true)
                }
            }
        })
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            initResponse()
        } else {
            // Optional: Handle offline case
        }
    }

    private fun onClick() {
        mViewDataBinding.btnSend.setOnClickListener {
            var price = mViewDataBinding.tvPrice.text.toString()
            if (price.isEmpty()) {
                mViewDataBinding.tvPrice.error = getString(R.string.price)
            } else {
                homeViewModel.sendOffer(idOrder, price.toInt())

            }

        }

    }


    fun dialogSuccess(message:String) {
        if (isDialogShowing) return

        isDialogShowing = true
        val dialogSuccess = Dialog(requireActivity(), R.style.customDialogTheme)
        dialogSuccess.setCancelable(false)
        val inflater = requireActivity().layoutInflater
        val v: View = inflater.inflate(R.layout.dialog_success_order, null)
        dialogSuccess.setContentView(v)

        val ivClose = dialogSuccess.findViewById<ImageView>(R.id.imageViewClose)
        val textViewTitel = dialogSuccess.findViewById<TextView>(R.id.textViewTitel)
        val orderNo = dialogSuccess.findViewById<TextView>(R.id.tv_order_number)

        textViewTitel.text = getString(R.string.sent_successfully)
       // orderNo.text = getString(R.string.order_no) +" "+message
        orderNo.visibility=View.GONE

        ivClose.setOnClickListener {
            mainActivity.navController!!.popBackStack()
            dialogSuccess.dismiss()
            isDialogShowing = false
        }
        dialogSuccess.show()
    }


//    fun dialogSuccess() {
//        val dialogSuccess = Dialog(requireActivity(), R.style.customDialogTheme)
//        dialogSuccess?.setCancelable(false)
//        val inflater = requireActivity().layoutInflater
//        val v: View = inflater.inflate(R.layout.dialog_success_order, null)
//        dialogSuccess!!.setContentView(v)
//
//        val ivClose = dialogSuccess!!.findViewById<ImageView>(R.id.imageViewClose)
//        val textViewTitel = dialogSuccess!!.findViewById<TextView>(R.id.textViewTitel)
//        val orderNo = dialogSuccess!!.findViewById<TextView>(R.id.tv_order_number)
//
//        textViewTitel.text = getString(R.string.it_was_sent_to_management)
//        orderNo.text = getString(R.string.we_will_contact_you_as_soon_as_possible)
//
//        ivClose.setOnClickListener {
//            mainActivity.navController!!.popBackStack()
//            dialogSuccess!!.dismiss()
////            dismiss()
//        }
//        dialogSuccess!!.show()
//
//    }


}
