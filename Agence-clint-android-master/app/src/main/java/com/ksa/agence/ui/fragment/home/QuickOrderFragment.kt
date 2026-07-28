package com.ksa.agence.ui.fragment.home

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.ksa.agence.R
import com.ksa.agence.adapter.DropDownServiceAdapter
import com.ksa.agence.base.BaseBottomDialog
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentQuickOrderBinding
import com.ksa.agence.entity.categoriesResponse.DataCategoriesResponse
import com.ksa.agence.ui.activity.MainActivity
import com.ksa.agence.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class QuickOrderFragment : BaseBottomDialog<FragmentQuickOrderBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_quick_order
    private lateinit var type: String
    private var idCompany: Int?=0
    private var idOffer: Int?=0
    private var idService: Int?=0
    private lateinit var orderTitle: String
    private lateinit var order_duration_in_days: String
    private lateinit var orderDescription: String
    private  var orderType: String="quick"
    private var catigoryId: Int?=0
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var mainActivity: MainActivity


    lateinit var dropDownServiceAdapter: DropDownServiceAdapter
    lateinit var listData: ArrayList<DataCategoriesResponse>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()

        listData = ArrayList()


        if (arguments != null){

            try {

                val args:QuickOrderFragmentArgs=QuickOrderFragmentArgs.fromBundle(requireArguments())
                idOffer=args.idOffer ?: 0
                idService=args.idService ?: 0
                idCompany=args.idCompany ?: 0
                type=args.type

                //// service,offer,quick,private
                if (type=="service")
                {
                    orderType="service"
                }
                else     if (type=="offer")
                {
                    orderType="offer"
                }
                else     if (type=="private")
                {
                    orderType="private"
                }
                else
                {
                    orderType="quick"
                }

            }
            catch (e:Exception){}

        }


        onClick()


    }

    private fun initResponse() {

        listData.add(
            DataCategoriesResponse(
                "", "", 0, 0, getString(R.string.select)
            )
        )

        // resend response
        viewModel.getCategory()
        viewModel.categoriesResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {

                                Log.d("TestVerification", "Data received: ${it.data}")

                                listData.addAll(it.data!!)
                                dropDownServiceAdapter = DropDownServiceAdapter(requireActivity(), listData)
                                mViewDataBinding.spService.adapter = dropDownServiceAdapter
                                dropDownServiceAdapter.notifyDataSetChanged()
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



        viewModel.quickOrderResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {

                                showDialogSuccess(it.data!!.order_number!!)

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


    private fun onClick() {

        mViewDataBinding.spService?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    if (position != 0) {
                        catigoryId = listData.get(position).id
                    } else {

                    }
                }

            }

        mViewDataBinding.btnSendToAll.setOnClickListener {

                orderTitle=mViewDataBinding.tvOrderAddress.text.toString()
                orderDescription=mViewDataBinding.tvOrderDetails.text.toString()
                order_duration_in_days=mViewDataBinding.tvDurationOfCompletion.text.toString()

            if (catigoryId==0){
                Utilities.showToastError(requireActivity(), getString(R.string.select_servic))

            }
            else if (orderTitle.isEmpty())
            {
                mViewDataBinding.tvOrderAddress.error=getString(R.string.this_item_is_required)
            }
            else if (orderDescription.isEmpty())
            {
                mViewDataBinding.tvOrderDetails.error=getString(R.string.this_item_is_required)
            }
            else if (order_duration_in_days.isEmpty())
            {
                mViewDataBinding.tvDurationOfCompletion.error=getString(R.string.this_item_is_required)
            }
            else{
                viewModel.quickOrder(
                    catigoryId!!,
                    if (idCompany != 0) idCompany else null,
                    if (idOffer != 0) idOffer else null,
                    if (idService != 0) idService else null,
                    orderType,orderTitle,orderDescription,order_duration_in_days)
            }

        }

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

    }

    fun showDialogSuccess(orderNumber: String) {
        val  dialog = Dialog(requireActivity(), R.style.customDialogTheme)
        dialog.setCancelable(false)
        val inflater = requireActivity().layoutInflater
        val v: View = inflater.inflate(R.layout.dialog_success_order, null)
        dialog.setContentView(v)

        val ivClose=dialog.findViewById<ImageView>(R.id.imageViewClose)
        val orderNo=dialog.findViewById<TextView>(R.id.tv_order_number)

        orderNo.text=getString(R.string.order_no)+" "+orderNumber

        ivClose.setOnClickListener {
            dialog.dismiss()
            dismiss()
        }

        dialog.show()

    }


}