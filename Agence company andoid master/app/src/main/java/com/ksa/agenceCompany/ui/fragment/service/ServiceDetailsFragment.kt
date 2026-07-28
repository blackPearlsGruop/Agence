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
import com.ksa.agenceCompany.adapter.SliderImageServiceAdapter
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.common.CODE200
import com.ksa.agenceCompany.common.CODE422
import com.ksa.agenceCompany.common.Resource
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.databinding.FragmentAllServiesBinding
import com.ksa.agenceCompany.databinding.FragmentServiceDetailsBinding
import com.ksa.agenceCompany.entity.categoriesResponse.DataCategoriesResponse
import com.ksa.agenceCompany.interfaces.Home
import com.ksa.agenceCompany.ui.activity.MainActivity
import com.ksa.agenceCompany.ui.fragment.home.OfferDetailsFragmentArgs
import com.ksa.agenceCompany.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ServiceDetailsFragment : BaseFragment<FragmentServiceDetailsBinding>(), Home {

    override fun getLayoutId(): Int = R.layout.fragment_service_details

    private lateinit var type: String
    private lateinit var flag: String
    private var idService: Int=0
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var mainActivity: MainActivity


    lateinit var sliderImageServiceAdapter: SliderImageServiceAdapter
    lateinit var listData: ArrayList<String>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()


        listData = ArrayList()

        if (arguments != null){

            val args: ServiceDetailsFragmentArgs = ServiceDetailsFragmentArgs.fromBundle(requireArguments())
            idService=args.idService
            flag=args.flag
            type=args.type

            if (type=="Service"){
                viewModel.categoriesById(idService)
                mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.service_details)
            }
            else{
                viewModel.offerById(idService)
                mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.offer_details)

            }

        }

        onClick()


    }

    private fun onClick() {

        mViewDataBinding.btnDelete.setOnClickListener {
            if (type=="Service"){
                viewModel.deleteService(idService)

            }
            else{
                viewModel.deleteOffer(idService)

            }
        }

        mViewDataBinding.btnUpdate.setOnClickListener {

            if (type=="Service"){
                val action = ServiceDetailsFragmentDirections.actionServiceDetailsFragmentToUpdateAServiceOrOfferFragment(idService,"Service")
                mViewDataBinding.root.findNavController().navigate(action)
            }
            else{
                val action = ServiceDetailsFragmentDirections.actionServiceDetailsFragmentToUpdateAServiceOrOfferFragment(idService,"Offer")
                mViewDataBinding.root.findNavController().navigate(action)
            }

        }
    }


    private fun initResponse() {

        // resend response
        viewModel.categoriesByIdResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {


                                mViewDataBinding.tvNameService.text=it.data!!.title
                                mViewDataBinding.tvDic.text=it.data!!.description
                                mViewDataBinding.tvAddress.text=it.data!!.company!!.address
                                mViewDataBinding.tvDate.text=getString(R.string.duration_of_completion)+" : "+it.data!!.service_duration_in_days!!
                                mViewDataBinding.tvPrice.text=""+it.data!!.price+" "+getString(R.string.r_s)

                                listData.addAll(it.data!!.images!!)
                                sliderImageServiceAdapter =
                                    SliderImageServiceAdapter(requireActivity(), listData)
                                mViewDataBinding.sliderViewPager2.adapter = sliderImageServiceAdapter
                                sliderImageServiceAdapter.notifyDataSetChanged()
                                mViewDataBinding.dotsIndicator.attachTo(mViewDataBinding.sliderViewPager2)

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


        viewModel.offerByIdResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {


                                mViewDataBinding.tvNameService.text=it.data!!.title
                                mViewDataBinding.tvDic.text=it.data!!.description
                                mViewDataBinding.tvAddress.text=it.data!!.company!!.address
                                mViewDataBinding.tvDate.text=getString(R.string.duration_of_completion)+" : "+it.data!!.offer_duration_in_days!!
                                mViewDataBinding.tvPrice.text=""+it.data!!.price+" "+getString(R.string.r_s)

                                listData.addAll(it.data!!.images!!)
                                sliderImageServiceAdapter =
                                    SliderImageServiceAdapter(requireActivity(), listData)
                                mViewDataBinding.sliderViewPager2.adapter = sliderImageServiceAdapter
                                sliderImageServiceAdapter.notifyDataSetChanged()
                                mViewDataBinding.dotsIndicator.attachTo(mViewDataBinding.sliderViewPager2)

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

    }

    override fun clickItemOpportunitiesDetails(idOpportunities: Int) {

    }

    override fun clickItemUpdateService(idService: Int) {

    }

    override fun clickItemDeleteService(idService: Int, position: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity.showHomeToolbar()
        if (flag=="Home")
        {
            mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.home)

        }
        else{
            mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.services)

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
            mainActivity.navController!!.popBackStack()
            mainActivity.showHomeToolbar()
        }

        dialog.show()

    }

}