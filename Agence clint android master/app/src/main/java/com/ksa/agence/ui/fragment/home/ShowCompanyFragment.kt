package com.ksa.agence.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.adapter.OffersCompanyAdapter
import com.ksa.agence.adapter.ServiceCompanyAdapter
import com.ksa.agence.adapter.OurBusinessCompanyAdapter
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE404
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentShowCompanyBinding
import com.ksa.agence.entity.showCompaniesResponse.OfferShowCompaniesResponse
import com.ksa.agence.entity.showCompaniesResponse.ServiceShowCompaniesResponse
import com.ksa.agence.entity.showCompaniesResponse.WorkShowCompaniesResponse
import com.ksa.agence.interfaces.Services
import com.ksa.agence.ui.activity.MainActivity
import com.ksa.agence.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowCompanyFragment : BaseFragment<FragmentShowCompanyBinding>(), Services {

    override fun getLayoutId(): Int = R.layout.fragment_show_company
    private var flagPage: String=""
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var mainActivity: MainActivity



    private var id_Company: Int=0

    lateinit var ourBusinessCompanyAdapter: OurBusinessCompanyAdapter
    lateinit var listWorkData: ArrayList<WorkShowCompaniesResponse>

    lateinit var serviceCompanyAdapter: ServiceCompanyAdapter
    lateinit var listServiceData: ArrayList<ServiceShowCompaniesResponse>


    lateinit var offersCompanyAdapter: OffersCompanyAdapter
    lateinit var listOffersData: ArrayList<OfferShowCompaniesResponse>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.company_details)



        if (arguments != null){

            val args:ShowCompanyFragmentArgs=ShowCompanyFragmentArgs.fromBundle(requireArguments())
            id_Company=args.idCompany
            flagPage=args.flage
            viewModel.showCompanies(id_Company)

        }


        onClick()


    }

    private fun onClick() {
        mViewDataBinding.btnSendASpecialRequest.setOnClickListener {
            val action=ShowCompanyFragmentDirections.actionShowCompanyFragmentToQuickOrderFragment(0,0,id_Company,"private")
            mViewDataBinding.root.findNavController().navigate(action)
        }
    }


    private fun initResponse() {

        listWorkData=ArrayList()
        listServiceData=ArrayList()
        listOffersData=ArrayList()

        // resend response
        viewModel.showCompaniesResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {

                                Utilities.onLoadImageFromUrl(
                                    requireContext(),
                                    it.data!!.company_logo,
                                    mViewDataBinding.ivLogoCompany)
                                Utilities.onLoadImageFromUrl(
                                    requireContext(),
                                    it.data!!.company_background_image,
                                    mViewDataBinding.ivCover)
                                mViewDataBinding.tvNameCompany.text=it.data.title
                                mViewDataBinding.ratingBar.rating= it.data.avg_rate!!.toFloat()
                                mViewDataBinding.tvCountRat.text= ""+it.data.rate_count
                                mViewDataBinding.tvAddressCompany.text= ""+it.data.address
                                mViewDataBinding.tvBrief.text= ""+it.data.description

                                listWorkData.addAll(it.data.works!!)
                                ourBusinessCompanyAdapter=OurBusinessCompanyAdapter(requireActivity(),listWorkData)
                                mViewDataBinding.sliderOurBusiness.adapter=ourBusinessCompanyAdapter
                                ourBusinessCompanyAdapter.notifyDataSetChanged()
                                mViewDataBinding.dotsIndicator.attachTo(mViewDataBinding.sliderOurBusiness)

                                listServiceData.addAll(it.data.services!!)
                                serviceCompanyAdapter=ServiceCompanyAdapter(requireActivity(),listServiceData,this)
                                mViewDataBinding.rvServices.adapter=serviceCompanyAdapter
                                serviceCompanyAdapter.notifyDataSetChanged()


                                listOffersData.addAll(it.data.offers!!)
                                offersCompanyAdapter=OffersCompanyAdapter(requireActivity(),listOffersData,this)
                                mViewDataBinding.rvOffers.adapter=offersCompanyAdapter
                                offersCompanyAdapter.notifyDataSetChanged()

                            }

                            CODE422 -> {
                                Utilities.showToastError(requireActivity(), it.message!!)
                            }

                            CODE404 -> {
                                Utilities.showToastError(requireActivity(), it.message!!)
                                showProgress(false)

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

        if (flagPage=="Home")
        {
            mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.home)

        }
        else if (flagPage=="Favourites"){
            mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.favorite)

        }
        else if (flagPage=="showService"){
            mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.services)

        }
        else if (flagPage=="FIlir"){
            mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.search)
            mainActivity.hideHomeToolbar()

        }
        else{
            mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.companies)

        }
    }

    override fun clickItemServices(idService: Int) {
        val action=ShowCompanyFragmentDirections.actionShowCompanyFragmentToQuickOrderFragment(idService,0,0,"service")
        mViewDataBinding.root.findNavController().navigate(action)

    }

    override fun clickItemOfferCompany(idOffer: Int) {

        val action=ShowCompanyFragmentDirections.actionShowCompanyFragmentToQuickOrderFragment(idOffer,0,0,"offer")
        mViewDataBinding.root.findNavController().navigate(action)

    }


}