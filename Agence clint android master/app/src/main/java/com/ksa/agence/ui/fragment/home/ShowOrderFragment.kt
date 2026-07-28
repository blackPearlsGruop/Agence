package com.ksa.agence.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.adapter.AllOffersAdapter
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentShowOrderBinding
import com.ksa.agence.entity.allOfferCompanyResponse.DataAllOfferCompanyResponse
import com.ksa.agence.entity.getSingleOrderResponse.GetSingleOrderResponse
import com.ksa.agence.interfaces.Offer
import com.ksa.agence.ui.activity.MainActivity
import com.ksa.agence.ui.dialog.BottomSheetPaymentFragment
import com.ksa.agence.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowOrderFragment : BaseFragment<FragmentShowOrderBinding>(), Offer {

    override fun getLayoutId(): Int = R.layout.fragment_show_order
    private var id_company: Int? = 0
    private var id_order: Int = 0
    private var position: Int = 0
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var mainActivity: MainActivity


    lateinit var allOffersAdapter: AllOffersAdapter
    lateinit var listData: ArrayList<DataAllOfferCompanyResponse>
    private lateinit var myDataObject: GetSingleOrderResponse


    var idCompany: Int = 0
    var nameCompany: String = ""
    var imageCompany: String = ""
    var imageUser: String = ""
    var categoryName: String = "No Category"
    var orderNumber: String = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.order_details)



        if (arguments != null) {

            val args: ShowOrderFragmentArgs = ShowOrderFragmentArgs.fromBundle(requireArguments())
            id_order = args.idOrder

            onClick()

        }


    }


    private fun initResponse() {

        listData = ArrayList()

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
                                myDataObject = result.data

                                val data = it.data
                                if (data != null) {
                                    id_order = data.id!!
                                    id_company = data.company?.id

                                    val company = data.company
                                    val category = data.category

                                    if (company != null) {
                                        mViewDataBinding.constraintDataCompany.visibility =
                                            View.VISIBLE

                                        company?.company_logo?.let {
                                            Utilities.onLoadImageFromUrl(
                                                requireContext(),
                                                it,
                                                mViewDataBinding.ivLogoCompany)

                                            mViewDataBinding.tvNameCompany.text =
                                                data.company!!.title ?: ""
                                            mViewDataBinding.tvAddressCompany.text =
                                                data.company!!.address ?: ""



                                            idCompany = company!!.id!!
                                            nameCompany = company!!.title!!
                                            imageUser = data.user!!.profile_image!!
                                            imageCompany = company.company_logo!!
                                            orderNumber = data.order_number!!
                                            if (category !=null)
                                            {
                                                categoryName = category!!.title!!

                                            }


                                        }
                                    }



                                    if (data.price != null) {
                                        mViewDataBinding.constraintLayoutPrice.visibility =
                                            View.VISIBLE
                                    }

                                    mViewDataBinding.tvNoOrder.text =
                                        getString(R.string.order_no) + " " + (data.order_number)
                                    mViewDataBinding.tvDate.text = data.created_at
                                    mViewDataBinding.tvTime.text =
                                        getString(R.string.duration_of_completion) + " : " + data.order_duration_in_days + " : " + getString(
                                            R.string.day
                                        )
                                    mViewDataBinding.tvPriceService.text =
                                        "${data.price} ${getString(R.string.r_s)}"
                                    mViewDataBinding.tvNameCategory.text = category?.title ?: ""
                                    mViewDataBinding.tvDic.text = data.description ?: ""

                                    mViewDataBinding.tvAmountBeforeTax.text =
                                        "${data.price ?: ""} ${getString(R.string.r_s)}"
                                    mViewDataBinding.tvTax.text = "${data.tax_percentage ?: ""}"
                                    mViewDataBinding.tvTotal.text =
                                        "${data.price ?: ""} ${getString(R.string.r_s)}"


                                    if (data.order_status == "pending") {
                                        mViewDataBinding.btnCompanyChat.visibility = View.GONE
                                        mViewDataBinding.btnRating.visibility = View.GONE
                                        mViewDataBinding.btnReOrder.visibility = View.GONE

                                    } else if (data.order_status == "in-progress") {
                                        mViewDataBinding.btnCompanyChat.visibility = View.VISIBLE
                                        mViewDataBinding.btnRating.visibility = View.GONE
                                        mViewDataBinding.btnReOrder.visibility = View.GONE

                                    } else if (data.order_status == "completed") {
                                        mViewDataBinding.btnCompanyChat.visibility = View.GONE
                                        mViewDataBinding.btnReOrder.visibility = View.VISIBLE

                                        if (data.accepted_offer != null) {
                                            mViewDataBinding.btnRating.visibility = View.GONE
                                        } else {
                                            mViewDataBinding.btnRating.visibility = View.VISIBLE
                                        }

                                    } else if (data.order_status == "canceled") {
                                        mViewDataBinding.btnCompanyChat.visibility = View.GONE
                                        mViewDataBinding.btnRating.visibility = View.GONE
                                        mViewDataBinding.btnReOrder.visibility = View.VISIBLE
                                    } else {
                                        // No default action needed
                                    }


                                } else {
                                    Log.e("ShowOrderFragment", "Data is null")
                                }

                                listData.addAll(data!!.offers!!)
                                allOffersAdapter =
                                    AllOffersAdapter(requireActivity(), listData, this)
                                mViewDataBinding.rvOffers.adapter = allOffersAdapter
                                allOffersAdapter.notifyDataSetChanged()

                                if (listData.size != 0) {
                                    mViewDataBinding.textViewOffers.visibility = View.VISIBLE
                                    mViewDataBinding.rvOffers.visibility = View.VISIBLE
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

    }


    private fun onClick() {

        mViewDataBinding.btnRating.setOnClickListener {
            val action = ShowOrderFragmentDirections.actionShowOrderFragmentToRatingCompanyFragment(
                id_order, id_company!!
            )
            mViewDataBinding.root.findNavController().navigate(action)
        }


        mViewDataBinding.btnCompanyChat.setOnClickListener {

            val action = ShowOrderFragmentDirections.actionShowOrderFragmentToConversationFragment(
                idCompany,
                orderNumber,
                categoryName!!,
                nameCompany,
                imageCompany,
                id_order,
                "SHOW_ORDER"
            )
            mViewDataBinding.root.findNavController().navigate(action)


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

    override fun clickItemOffer(
        idOffer: Int,
        imageCompany: String,
        nameCompany: String,
        dicCategory: String,
        nameCategory: String,
        priceOffer: String,
        descriptionOrder: String,
        orderNo: String
    ) {
        val action = ShowOrderFragmentDirections.actionShowOrderFragmentToOfferDetailsFragment(
            idOffer,
            imageCompany,
            nameCompany,
            dicCategory,
            nameCategory,
            priceOffer,
            descriptionOrder,
            orderNo
        )
        mViewDataBinding.root.findNavController().navigate(action)
    }

    override fun acceptedOffer(idOffer: Int, pos: Int) {
        val bundle = Bundle()
        bundle.putSerializable("my_data_key2", myDataObject)

        val bottomSheet = BottomSheetPaymentFragment()
        bottomSheet.arguments = bundle
        bottomSheet.show(parentFragmentManager, BottomSheetPaymentFragment.TAG)
    }

    override fun rejectOffer(idOffer: Int, pos: Int) {
        position = pos
        viewModel.rejectOffer(idOffer)

    }


}