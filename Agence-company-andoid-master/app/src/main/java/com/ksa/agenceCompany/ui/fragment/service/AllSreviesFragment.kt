package com.ksa.agenceCompany.ui.fragment.service

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.adapter.AllCategoriesAdapter
import com.ksa.agenceCompany.adapter.AllOffersAdapter
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.common.CODE200
import com.ksa.agenceCompany.common.CODE422
import com.ksa.agenceCompany.common.Resource
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.databinding.FragmentAllServiesBinding
import com.ksa.agenceCompany.entity.categoriesResponse.DataCategoriesResponse
import com.ksa.agenceCompany.interfaces.Home
import com.ksa.agenceCompany.ui.activity.MainActivity
import com.ksa.agenceCompany.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllSreviesFragment : BaseFragment<FragmentAllServiesBinding>(), Home {

    override fun getLayoutId(): Int = R.layout.fragment_all_servies

    private var pos: Int = 0
    private var type: String = "Service"
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var mainActivity: MainActivity


    lateinit var allOffersAdapter: AllOffersAdapter
    lateinit var allCategoriesAdapter: AllCategoriesAdapter
    lateinit var listData: ArrayList<DataCategoriesResponse>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.services)


        listData = ArrayList()

        onClick()

    }

    private fun onClick() {

        mViewDataBinding.btnService.setOnClickListener {
            mViewDataBinding.btnService.setBackgroundResource(R.drawable.shape_bottom)
            mViewDataBinding.tvService.setTextColor(resources.getColor(R.color.white))

            mViewDataBinding.btnOffer.setBackgroundDrawable(null)
            mViewDataBinding.tvOffer.setTextColor(resources.getColor(R.color.primary))

            viewModel.getCategory()
            type = "Service"


        }

        mViewDataBinding.btnOffer.setOnClickListener {
            mViewDataBinding.btnOffer.setBackgroundResource(R.drawable.shape_bottom)
            mViewDataBinding.tvOffer.setTextColor(resources.getColor(R.color.white))

            mViewDataBinding.btnService.setBackgroundDrawable(null)
            mViewDataBinding.tvService.setTextColor(resources.getColor(R.color.primary))
            viewModel.getOffers()
            type = "Offer"


        }

    }


    private fun initResponse() {

        // resend response
        viewModel.getCategory()
        viewModel.categoriesResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    listData.clear()
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {

                                listData.addAll(it.data!!)
                                allCategoriesAdapter =
                                    AllCategoriesAdapter(requireActivity(), listData, this)
                                mViewDataBinding.rvAllCategory.adapter = allCategoriesAdapter
                                allCategoriesAdapter.notifyDataSetChanged()

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

        viewModel.offersResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    listData.clear()
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {

                                listData.addAll(it.data!!)
                                allOffersAdapter =
                                    AllOffersAdapter(requireActivity(), listData, this)
                                mViewDataBinding.rvAllCategory.adapter = allOffersAdapter
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


        // resend response
        viewModel.deleteServiceResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {

                                showDialogSuccess()
                                listData.removeAt(pos)
                                allCategoriesAdapter.notifyDataSetChanged()
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

        // resend response
        viewModel.deleteOfferResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {

                                showDialogSuccess()
                                listData.removeAt(pos)
                                allCategoriesAdapter.notifyDataSetChanged()
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

    override fun clickItemShowService(idService: Int) {
        val action =
            AllSreviesFragmentDirections.actionAllSreviesFragmentToServiceDetailsFragment(
                idService, "AllService", type
            )
        mViewDataBinding.root.findNavController().navigate(action)
    }

    override fun clickItemOpportunitiesDetails(idOpportunities: Int) {

    }

    override fun clickItemUpdateService(idService: Int) {
        val action =
            AllSreviesFragmentDirections.actionAllSreviesFragmentToUpdateAServiceOrOfferFragment(
                idService, type
            )
        mViewDataBinding.root.findNavController().navigate(action)
    }

    override fun clickItemDeleteService(idService: Int, position: Int) {

        pos = position
        if (type == "Service") {
            viewModel.deleteService(idService)

        } else {
            viewModel.deleteOffer(idService)

        }

    }


    fun showDialogSuccess() {
        val dialog = Dialog(requireActivity(), R.style.customDialogTheme)
        dialog.setCancelable(false)
        val inflater = requireActivity().layoutInflater
        val v: View = inflater.inflate(R.layout.dialog_success_order, null)
        dialog.setContentView(v)

        val ivClose = dialog.findViewById<ImageView>(R.id.imageViewClose)
        val orderTitle = dialog.findViewById<TextView>(R.id.textViewTitel)
        val orderNo = dialog.findViewById<TextView>(R.id.tv_order_number)

        orderTitle.text = getString(R.string.deleted)
        orderNo.visibility = View.GONE

        ivClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }


    override fun onDestroy() {
        super.onDestroy()
        mainActivity.showHomeToolbar()
    }

}