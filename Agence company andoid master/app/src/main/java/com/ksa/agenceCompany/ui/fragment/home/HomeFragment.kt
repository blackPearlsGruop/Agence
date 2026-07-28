package com.ksa.agenceCompany.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.adapter.AllCategoriesHomeAdapter
import com.ksa.agenceCompany.adapter.AllOpportunitiesAdapter
import com.ksa.agenceCompany.adapter.AllOrdersAdapter
import com.ksa.agenceCompany.adapter.AllSubscriptionAdapter
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.common.CODE200
import com.ksa.agenceCompany.common.CODE422
import com.ksa.agenceCompany.common.Resource
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.databinding.FragmentHomeBinding
import com.ksa.agenceCompany.entity.allOpportunitiesResponse.DataAllOpportunitiesResponse
import com.ksa.agenceCompany.entity.allOrdersResponse.DataAllOrdersResponse
import com.ksa.agenceCompany.entity.allSubscriptionResponse.DataAllSubscriptionResponse
import com.ksa.agenceCompany.entity.categoriesResponse.DataCategoriesResponse
import com.ksa.agenceCompany.interfaces.Home
import com.ksa.agenceCompany.interfaces.Order
import com.ksa.agenceCompany.ui.activity.MainActivity
import com.ksa.agenceCompany.viewModels.AuthenticationViewModel
import com.ksa.agenceCompany.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(), Home, Order {

    override fun getLayoutId(): Int = R.layout.fragment_home
    private lateinit var mainActivity: MainActivity


    private var position: Int = 0
    private val viewModel: HomeViewModel by viewModel()
    private val authenticationViewModel: AuthenticationViewModel by viewModel()


    lateinit var categoriesAdapter: AllCategoriesHomeAdapter
    lateinit var listCategories: ArrayList<DataCategoriesResponse>

    lateinit var allSubscriptionAdapter: AllSubscriptionAdapter
    lateinit var listDataAllSubscription: ArrayList<DataAllSubscriptionResponse>


    lateinit var allOrdersAdapter: AllOrdersAdapter
    lateinit var listDataOrder: ArrayList<DataAllOrdersResponse>


    lateinit var allOpportunitiesAdapter: AllOpportunitiesAdapter
    lateinit var listDataAllOpportunities: ArrayList<DataAllOpportunitiesResponse>



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        listCategories = ArrayList()
        listDataAllSubscription = ArrayList()
        listDataOrder = ArrayList()
        listDataAllOpportunities = ArrayList()

        onClick()
    }


    private fun initResponse() {

        // resend response
        authenticationViewModel.me()
        authenticationViewModel.meResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    listDataAllSubscription.clear()
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                if (it.data!!.availability==1){
                                    mViewDataBinding.btnSwitch.isChecked=true
                                    mViewDataBinding.tvReceivingRequests.text=getString(R.string.available)
                                    mViewDataBinding.tvReceivingRequests.setTextColor(resources.getColor(R.color.secondary))
                                }
                                else if (it.data!!.availability==0){
                                    mViewDataBinding.btnSwitch.isChecked=false
                                    mViewDataBinding.tvReceivingRequests.text=getString(R.string.unavailable)
                                    mViewDataBinding.tvReceivingRequests.setTextColor(resources.getColor(R.color.grey_bold))

                                }

                                viewModel.getAllSubscription()

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



        viewModel.subscriptionResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    listDataAllSubscription.clear()
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                listDataAllSubscription.addAll(it.data!!)
                                allSubscriptionAdapter =
                                    AllSubscriptionAdapter(
                                        requireActivity(),
                                        listDataAllSubscription,
                                        this
                                    )
                                mViewDataBinding.rvSubscriptions.adapter = allSubscriptionAdapter
                                allSubscriptionAdapter.notifyDataSetChanged()

                                viewModel.getCategory()
                                val statusArray = listOf("pending")
                                viewModel.allOrders("pending")
                                viewModel.allOpportunities()

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


        viewModel.allOpportunitiesResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    listDataAllOpportunities.clear()
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                listDataAllOpportunities.addAll(it.data!!)
                                allOpportunitiesAdapter =
                                    AllOpportunitiesAdapter(
                                        requireActivity(),
                                        listDataAllOpportunities,
                                        this
                                    )
                                mViewDataBinding.rvAnOpportunity.adapter = allOpportunitiesAdapter
                                allOpportunitiesAdapter.notifyDataSetChanged()

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
                                    AllCategoriesHomeAdapter(
                                        requireActivity(),
                                        listCategories,
                                        this
                                    )
                                mViewDataBinding.rvServices.adapter = categoriesAdapter
                                categoriesAdapter.notifyDataSetChanged()


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

        viewModel.allOrdersResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    listDataOrder.clear()
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading

                            CODE200 -> {
                                listDataOrder.addAll(it.data!!)
                                allOrdersAdapter =
                                    AllOrdersAdapter(requireActivity(), listDataOrder, this)
                                mViewDataBinding.rvOrders.adapter = allOrdersAdapter
                                allOrdersAdapter.notifyDataSetChanged()

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


        viewModel.updateAvailabilityResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
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

        mViewDataBinding.btnSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Switch تم تشغيله
                mViewDataBinding.tvReceivingRequests.text=getString(R.string.available)
                mViewDataBinding.tvReceivingRequests.setTextColor(resources.getColor(R.color.secondary))

            } else {
                // Switch تم إيقافه
                mViewDataBinding.tvReceivingRequests.text=getString(R.string.unavailable)
                mViewDataBinding.tvReceivingRequests.setTextColor(resources.getColor(R.color.grey_bold))


            }

            viewModel.updateAvailability()

        }
        mViewDataBinding.tvAllSubscriptions.setOnClickListener {

            val action = HomeFragmentDirections.actionMenuHomeToAllPlansFragment()
            mViewDataBinding.root.findNavController().navigate(action)


        }

        mViewDataBinding.tvAllServices.setOnClickListener {

            val action = HomeFragmentDirections.actionMenuHomeToAllSreviesFragment()
            mViewDataBinding.root.findNavController().navigate(action)

        }

        mViewDataBinding.tvAllAnOpportunity.setOnClickListener {

            val action = HomeFragmentDirections.actionMenuHomeToAllOpportunityFragment()
            mViewDataBinding.root.findNavController().navigate(action)

        }

        mViewDataBinding.tvAllOrders.setOnClickListener {
            mainActivity.navController!!.navigate(R.id.menuOrders)
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


    override fun clickItemShowService(idService: Int) {

        val action =
            HomeFragmentDirections.actionMenuHomeToServiceDetailsFragment(
                idService,"Home","Service")
        mViewDataBinding.root.findNavController().navigate(action)
    }

    override fun clickItemOpportunitiesDetails(idOpportunities: Int) {
        val action =
            HomeFragmentDirections.actionMenuHomeToOpportunityDetailsFragment(
                idOpportunities,
                "Home"
            )
        mViewDataBinding.root.findNavController().navigate(action)
    }

    override fun clickItemUpdateService(idService: Int) {

    }

    override fun clickItemDeleteService(idService: Int, position: Int) {
    }


    override fun clickItemOrder(idOrder: Int) {
        val action = HomeFragmentDirections.actionMenuHomeToShowOrderFragment(idOrder)
        mViewDataBinding.root.findNavController().navigate(action)
    }

    override fun clickItemSendOffer(idOrder: Int) {

    }

    override fun clickItemRejectOrder(idOrder: Int) {
    }


}