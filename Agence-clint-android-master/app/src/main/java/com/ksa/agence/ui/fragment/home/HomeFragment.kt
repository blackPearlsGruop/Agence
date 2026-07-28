package com.ksa.agence.ui.fragment.home

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.adapter.CopanyAdapter
import com.ksa.agence.adapter.CategoriesAdapter
import com.ksa.agence.adapter.SliderAdapter
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Notifications
import com.ksa.agence.common.Notifications.showNotificationPermission
import com.ksa.agence.common.Resource
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentHomeBinding
import com.ksa.agence.entity.bannerResponse.DataBannerResponse
import com.ksa.agence.entity.categoriesResponse.CategoriesResponse
import com.ksa.agence.entity.categoriesResponse.DataCategoriesResponse
import com.ksa.agence.entity.companyResponse.CompanyResponse
import com.ksa.agence.entity.companyResponse.DataCompanyResponse
import com.ksa.agence.interfaces.Company
import com.ksa.agence.ui.activity.MainActivity
import com.ksa.agence.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Timer
import java.util.TimerTask

class HomeFragment : BaseFragment<FragmentHomeBinding>(), Company {

    override fun getLayoutId(): Int = R.layout.fragment_home
    private lateinit var mainActivity: MainActivity


    private var position: Int=0
    private val viewModel: HomeViewModel by viewModel()


    lateinit var categoriesAdapter: CategoriesAdapter
    lateinit var listCategories: ArrayList<DataCategoriesResponse>

    lateinit var companyAdapter: CopanyAdapter
    lateinit var  listCompany: ArrayList<DataCompanyResponse>


    private lateinit var imageList: ArrayList<DataBannerResponse>
    private lateinit var sliderAdapter: SliderAdapter
    private var current_position: Int=1
    private lateinit var timer: Timer
    private lateinit var handler: Handler

    lateinit var companyResponse: CompanyResponse

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().showNotificationPermission()

        mainActivity = requireActivity() as MainActivity
        listCategories = ArrayList()
        listCompany = ArrayList()
        imageList = ArrayList()

        onClick()



    }




    private fun initResponse() {

        // resend response
        viewModel.getCategory()
        viewModel.categoriesResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    listCategories.clear()
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                listCategories.addAll(it.data!!)
                                categoriesAdapter =
                                    CategoriesAdapter(requireActivity(), listCategories,this)
                                mViewDataBinding.rvCategory.adapter = categoriesAdapter
                                categoriesAdapter.notifyDataSetChanged()

                                viewModel.getCompany()
                                viewModel.getBanner()


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

        viewModel.companyResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    listCompany.clear()
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading

                            CODE200 -> {
                                companyResponse=result.data

                                listCompany.addAll(it.data!!)
                                companyAdapter =
                                    CopanyAdapter(requireActivity(), listCompany,this)
                                mViewDataBinding.rvCompany.adapter = companyAdapter
                                companyAdapter.notifyDataSetChanged()

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


        viewModel.bannerResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    imageList.clear()
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {

                                imageList.addAll(it.data!!)
                                sliderAdapter =
                                    SliderAdapter(requireActivity(), imageList)

                                mViewDataBinding.sliderViewPager2.adapter = sliderAdapter
//                                mViewDataBinding.dotsIndicator.attachTo(mViewDataBinding.sliderViewPager2)
                                createSlideShow()

                                if (imageList.size==0){
                                    mViewDataBinding.constraintLayout5.visibility=View.GONE
                                }
                                else
                                {
                                    mViewDataBinding.constraintLayout5.visibility=View.VISIBLE
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
                    showProgress(false)

                }
            }
        })

        viewModel.addFavouritesResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                Utilities.showToastSuccess(requireActivity(), it.message!!)
                                if(companyResponse.data!![position].is_added_favourite!!) {
                                    companyResponse.data!![position].is_added_favourite=false
                                }else{
                                    companyResponse.data!![position].is_added_favourite=true

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


    private fun createSlideShow() {
      //  mViewDataBinding.dotsIndicator.setViewPager2(mViewDataBinding.sliderViewPager)
        val handler = Handler()
        val runnable = Runnable {
            if (current_position == imageList.size)
                current_position = 0
            mViewDataBinding.sliderViewPager2.setCurrentItem(current_position++, true)
        }
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(runnable)
            }
        }, 300, 5000)
    }



    private fun onClick() {

        mViewDataBinding.tvAllCompanies.setOnClickListener {

            val action=HomeFragmentDirections.actionMenuHomeToAllComanyFragment()
            mViewDataBinding.root.findNavController().navigate(action)

        }

        mViewDataBinding.tvAllServices.setOnClickListener {

            val action=HomeFragmentDirections.actionMenuHomeToAllSreviesFragment()
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

    override fun clickItemCompany(idCompany: Int,flag:String) {
        val action=HomeFragmentDirections.actionMenuHomeToShowCompanyFragment(idCompany,"Home")
        mViewDataBinding.root.findNavController().navigate(action)
    }

    override fun clickItemAddCompanyFav(idCompany: Int, pos: Int) {
        position=pos
        viewModel.addFavourites(idCompany)
    }

    override fun clickItemShowService(idService: Int) {
        val action=HomeFragmentDirections.actionMenuHomeToShowSreviesFragment(idService,"Home")
        mViewDataBinding.root.findNavController().navigate(action)    }


}