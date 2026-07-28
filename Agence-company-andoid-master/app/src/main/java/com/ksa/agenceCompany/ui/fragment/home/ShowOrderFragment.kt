package com.ksa.agenceCompany.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.common.CODE200
import com.ksa.agenceCompany.common.CODE422
import com.ksa.agenceCompany.common.Resource
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.databinding.FragmentShowOrderBinding
import com.ksa.agenceCompany.entity.getSingleOrderResponse.GetSingleOrderResponse
import com.ksa.agenceCompany.interfaces.Order
import com.ksa.agenceCompany.ui.activity.MainActivity
import com.ksa.agenceCompany.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowOrderFragment : BaseFragment<FragmentShowOrderBinding>() , Order {

    override fun getLayoutId(): Int = R.layout.fragment_show_order
    private var id_company: Int?=0
    private var id_order: Int=0
    private var position: Int=0
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var mainActivity: MainActivity


    private lateinit var myDataObject: GetSingleOrderResponse



    var idUser: Int =0
    var nameUser: String =""
    var imageUser: String = ""
    var categoryName: String = ""
    var orderNumber: String = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.order_details)



        if (arguments != null){

            val args:ShowOrderFragmentArgs=ShowOrderFragmentArgs.fromBundle(requireArguments())
            id_order=args.idOrder

            onClick()

        }



    }




    private fun initResponse() {

        // resend response
        viewModel.getSingleOrder(id_order)
        viewModel.getSingleOrderResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                myDataObject=result.data

                                val data = it.data
                                if (data != null) {
                                    id_order = data.id!!
                                    id_company = data.company?.id

                                    val user = data.user
                                    val category = data.category

                                    if (user !=null)
                                    {
                                        mViewDataBinding.constraintDataCompany.visibility=View.VISIBLE

                                        user?.profile_image?.let {
                                            Utilities.onLoadImageFromUrl(requireContext(), it, mViewDataBinding.ivLogoUser)
                                            mViewDataBinding.tvNameUser.text = data.user!!.name ?: ""
                                          //  mViewDataBinding.tvAddress.text = data.company!!.address ?: ""

                                            idUser = user!!.id!!
                                            nameUser = user!!.name!!
                                            imageUser = user.profile_image!!
                                            orderNumber = data.order_number!!

                                            if (category !=null)
                                            {
                                                categoryName = category!!.title!!

                                            }


                                        }
                                    }


                                    mViewDataBinding.tvNoOrder.text = getString(R.string.order_no) + " " + (data.order_number )
                                    mViewDataBinding.tvTime.text = getString(R.string.duration_of_completion) + " : " + data.order_duration_in_days + " : " + getString(R.string.day)
                                    mViewDataBinding.tvNameCategory.text = category?.title ?: ""
                                    mViewDataBinding.tvDic.text = data.description ?: ""

                                    if ( data.created_at !=null)
                                    {
                                        mViewDataBinding.tvDate.text = data.created_at

                                    }

                                    if (data.order_status =="pending") {
                                        mViewDataBinding.btnCustomerChat.visibility = View.GONE
                                        mViewDataBinding.btnSendOffer.visibility = View.VISIBLE
                                        mViewDataBinding.btnReject.visibility = View.VISIBLE

                                    }

                                  else  if ( data.order_status == "in-progress") {
                                        mViewDataBinding.btnCustomerChat.visibility = View.VISIBLE
                                        mViewDataBinding.btnSendOffer.visibility = View.GONE
                                        mViewDataBinding.btnReject.visibility = View.GONE

                                    }
                                    else if (data.order_status == "completed") {
                                        mViewDataBinding.btnCustomerChat.visibility = View.GONE
                                        mViewDataBinding.btnSendOffer.visibility = View.GONE
                                        mViewDataBinding.btnReject.visibility = View.GONE
                                    }

                                    else if (data.order_status == "canceled") {
                                        mViewDataBinding.btnCustomerChat.visibility = View.GONE
                                        mViewDataBinding.btnSendOffer.visibility = View.GONE
                                        mViewDataBinding.btnReject.visibility = View.GONE
                                    }
                                    else {
                                        // No default action needed
                                    }



                                } else {
                                    Log.e("ShowOrderFragment", "Data is null")
                                }

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

        viewModel.rejectOrderResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                Utilities.showToastSuccess(requireActivity(), it.message!!)
                                viewModel.getSingleOrder(id_order)

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

        mViewDataBinding.btnCustomerChat.setOnClickListener {

            val action=ShowOrderFragmentDirections.actionShowOrderFragmentToConversationFragment(idUser,orderNumber,categoryName,nameUser,imageUser,id_order,"SHOW_ORDER")
            mViewDataBinding.root.findNavController().navigate(action)


        }

        mViewDataBinding.btnSendOffer.setOnClickListener {
            val action=ShowOrderFragmentDirections.actionShowOrderFragmentToBottomSheetSendOfferFragment(id_order)
            mViewDataBinding.root.findNavController().navigate(action)

        }

        mViewDataBinding.btnReject.setOnClickListener {
            viewModel.rejectOrder(id_order)

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
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.orders)

    }


    override fun clickItemOrder(idOrder: Int) {

    }

    override fun clickItemSendOffer(idOrder: Int) {

    }

    override fun clickItemRejectOrder(idOrder: Int) {

    }

    override fun onResume() {
        super.onResume()
        viewModel.getSingleOrder(id_order)
    }


}