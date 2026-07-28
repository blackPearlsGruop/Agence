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
import com.ksa.agence.databinding.FragmentOffersBinding
import com.ksa.agence.entity.allOfferCompanyResponse.AllOfferCompanyResponse
import com.ksa.agence.entity.allOfferCompanyResponse.DataAllOfferCompanyResponse
import com.ksa.agence.interfaces.Offer
import com.ksa.agence.ui.activity.MainActivity
import com.ksa.agence.ui.dialog.BottomSheetPaymentFragment
import com.ksa.agence.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class OffersFragment : BaseFragment<FragmentOffersBinding>(), Offer {

    override fun getLayoutId(): Int = R.layout.fragment_offers
    private lateinit var myDataObject: AllOfferCompanyResponse
    private lateinit var mainActivity: MainActivity


    private var position: Int=0
    private val viewModel: HomeViewModel by viewModel()


    lateinit var allOffersAdapter: AllOffersAdapter
    lateinit var listData: ArrayList<DataAllOfferCompanyResponse>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity


    }


    private fun initResponse() {

        listData= ArrayList()

        // resend response
        viewModel.allOfferCompany()
        viewModel.allOfferCompanyResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    listData.clear()
                    result.data?.let { response ->
                        when (response.code) {
                            // dismiss loading
                            CODE200 -> {
                                response.data?.let { data ->
                                    myDataObject=response
                                    listData.addAll(data)
                                    allOffersAdapter = AllOffersAdapter(requireActivity(), listData, this)
                                    mViewDataBinding.rvOffers.adapter = allOffersAdapter
                                    allOffersAdapter.notifyDataSetChanged()
                                } ?: run {
                                    // Handle the case where data is null
                                    Log.e("OffersFragment", "Response data is null")
                                }
                            }

                            CODE422 -> {
                                Utilities.showToastError(requireActivity(), response.message ?: "Unknown error")
                            }

                            else -> {
                                showProgress(false)
                                Utilities.showToastError(requireActivity(), response.message ?: "Unknown error")
                            }
                        }
                    } ?: run {
                        // Handle the case where result.data is null
                        Log.e("OffersFragment", "Result data is null")
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


 viewModel.acceptOfferResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                Utilities.showToastSuccess(requireActivity(), it.message!!)
                                listData.removeAt(position)
                                allOffersAdapter.notifyDataSetChanged()
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

 viewModel.rejectOfferResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                Utilities.showToastSuccess(requireActivity(), it.message!!)
                                listData.removeAt(position)
                                allOffersAdapter.notifyDataSetChanged()
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
        val action=OffersFragmentDirections.actionMenuOffersToOfferDetailsFragment(idOffer, imageCompany, nameCompany, dicCategory, nameCategory, priceOffer, descriptionOrder, orderNo)
        mViewDataBinding.root.findNavController().navigate(action)
    }

    override fun acceptedOffer(idOffer: Int, pos: Int) {
        position=pos
        //viewModel.acceptOffer(idOffer,"online-payment")

//        val action=OffersFragmentDirections.actionMenuOffersToBottomSheetPaymentFragment()
//        mViewDataBinding.root.findNavController().navigate(action)
        val bundle = Bundle()
        bundle.putSerializable("my_data_key", myDataObject)

        val bottomSheet = BottomSheetPaymentFragment()
        bottomSheet.arguments = bundle
        bottomSheet.show(parentFragmentManager, BottomSheetPaymentFragment.TAG)
    }

    override fun rejectOffer(idOffer: Int, pos: Int) {
        position=pos
        viewModel.rejectOffer(idOffer)

    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity.showHomeToolbar()
            mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.home)

    }
}