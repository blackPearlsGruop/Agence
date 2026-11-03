package com.ksa.agence.member.ui.fragment.home

import com.ksa.agence.common.sampleText
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.member.adapter.AllCategoriesHomeAdapter
import com.ksa.agence.member.adapter.AllOpportunitiesAdapter
import com.ksa.agence.member.adapter.AllOrdersAdapter
import com.ksa.agence.member.adapter.AllSubscriptionAdapter
import com.ksa.agence.member.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.member.common.util.Utilities
import com.ksa.agence.databinding.MemberFragmentHomeBinding
import com.ksa.agence.member.entity.allOpportunitiesResponse.DataAllOpportunitiesResponse
import com.ksa.agence.member.entity.allOrdersResponse.DataAllOrdersResponse
import com.ksa.agence.member.entity.allSubscriptionResponse.DataAllSubscriptionResponse
import com.ksa.agence.member.entity.categoriesResponse.DataCategoriesResponse
import com.ksa.agence.member.interfaces.Home
import com.ksa.agence.member.interfaces.Order
import com.ksa.agence.member.ui.activity.MainActivity
import com.ksa.agence.member.viewModels.AuthenticationViewModel
import com.ksa.agence.member.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<MemberFragmentHomeBinding>(), Home, Order {

    override fun getLayoutId(): Int = R.layout.member_fragment_home
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

        // New Figma elements (local/mock — no backend for these yet)
        try {
            setupBeneficiariesFallback()
        } catch (e: Exception) {
            Log.e("HomeFragment", "beneficiaries failed", e)
        }
        try {
            setupProjectsFallback()
        } catch (e: Exception) {
            Log.e("HomeFragment", "projects list failed", e)
        }
        try {
            setupOpportunitiesFallback()
        } catch (e: Exception) {
            Log.e("HomeFragment", "opportunities list failed", e)
        }
        try {
            setupComingSoonButtons()
        } catch (e: Exception) {
            Log.e("HomeFragment", "coming soon buttons failed", e)
        }

        onClick()
    }

    private fun setupOpportunitiesFallback() {
        val opportunities = listOf(
            com.ksa.agence.member.adapter.OpportunityHome(
                sampleText("إعداد حملة إعلانات جوجل"), sampleText("صيدلية المدينة"), sampleText("150 ريال"),
                listOf("PPC", "Google Ads"), sampleText("الموعد النهائي: 15 أغسطس 2026")
            ),
            com.ksa.agence.member.adapter.OpportunityHome(
                sampleText("تصميم هوية العلامة التجارية"), sampleText("الفارس للتجزئة"), sampleText("8,500 ريال"),
                listOf("Branding", "Design"), sampleText("الموعد النهائي: 20 أغسطس 2026")
            ),
            com.ksa.agence.member.adapter.OpportunityHome(
                sampleText("استراتيجية سوشيال الربع الثالث"), sampleText("تِك فيجن"), sampleText("6,000 ريال"),
                listOf("Content", "Social Media"), sampleText("الموعد النهائي: 30 أغسطس 2026")
            ),
        )
        val adapter = com.ksa.agence.member.adapter.OpportunityHomeAdapter(opportunities) {
            try {
                val action = HomeFragmentDirections.actionMenuHomeToOpportunityDetailsFragment(1, "Home")
                mViewDataBinding.root.findNavController().navigate(action)
            } catch (e: Exception) {
                Log.e("HomeFragment", "nav opportunity failed", e)
            }
        }
        mViewDataBinding.rvAnOpportunity.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(
                requireContext(),
                androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
                false
            )
        mViewDataBinding.rvAnOpportunity.adapter = adapter
    }

    private fun setupProjectsFallback() {
        val projects = listOf(
            com.ksa.agence.member.adapter.ProjectHome(
                sampleText("هوية العلامة التجارية والتموضع"), sampleText("شركة رياض التقنية"), sampleText("5 أعضاء")
            ),
            com.ksa.agence.member.adapter.ProjectHome(
                sampleText("حملة سوشيال ميديا الربع الثالث"), sampleText("الفارس للتجزئة"), sampleText("3 أعضاء")
            ),
        )
        val adapter = com.ksa.agence.member.adapter.ProjectHomeAdapter(projects) {
            mViewDataBinding.root.findNavController().navigate(R.id.teamProjectFragment)
        }
        mViewDataBinding.rvProjectsHome.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        mViewDataBinding.rvProjectsHome.adapter = adapter
    }

    private fun setupBeneficiariesFallback() {
        val isArabic = com.ksa.agence.member.app.AgenceCompanyApp.pref.getString(com.ksa.agence.common.LANG, "ar") == "ar"

        val beneficiaries = listOf(
            com.ksa.agence.member.adapter.Beneficiary(
                companyAr = sampleText("شركة الرياض التقنية"), companyEn = "Riyad Tech Co.",
                projectAr = sampleText("الهوية البصرية وتحديد الموقع"), projectEn = "Brand Identity & Positioning",
                budget = sampleText("12,000 ر.س"), progress = 45, statusAr = sampleText("نشط"), statusEn = "Active", statusColorGreen = true
            ),
            com.ksa.agence.member.adapter.Beneficiary(
                companyAr = sampleText("الفارس للتجزئة"), companyEn = "Al-Faris Retail",
                projectAr = sampleText("حملة سوشيال ميديا الربع الثالث"), projectEn = "Social Media Campaign Q3",
                budget = sampleText("8,500 ر.س"), progress = 20, statusAr = sampleText("قيد المراجعة"), statusEn = "In Review", statusColorGreen = false
            ),
            com.ksa.agence.member.adapter.Beneficiary(
                companyAr = sampleText("صيدلية المدينة"), companyEn = "Medina Pharmacy",
                projectAr = sampleText("إعداد وإدارة إعلانات جوجل"), projectEn = "Google Ads Setup & Management",
                budget = sampleText("4,200 ر.س"), progress = 70, statusAr = sampleText("نشط"), statusEn = "Active", statusColorGreen = true
            ),
        )

        val adapter = com.ksa.agence.member.adapter.BeneficiariesAdapter(beneficiaries, isArabic) {
            mViewDataBinding.root.findNavController().navigate(R.id.teamProjectFragment)
        }
        mViewDataBinding.rvBeneficiaries.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(
                requireContext(),
                androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
                false
            )
        mViewDataBinding.rvBeneficiaries.adapter = adapter

        mViewDataBinding.tvWalletBalance.text = sampleText("24,500 ريال")
    }

    private fun setupComingSoonButtons() {
        val comingSoon = View.OnClickListener {
            Utilities.showToastError(requireActivity(), getString(R.string.coming_soon))
        }
        mViewDataBinding.btnNotification.setOnClickListener {
            try {
                androidx.navigation.fragment.NavHostFragment.findNavController(this)
                    .navigate(R.id.notificationFragment)
            } catch (e: Exception) {
                Log.e("HomeFragment", "open notifications failed", e)
            }
        }
        mViewDataBinding.btnViewCandidates.setOnClickListener(comingSoon)
        mViewDataBinding.btnWalletTopup.setOnClickListener(comingSoon)
        mViewDataBinding.btnWalletWithdraw.setOnClickListener(comingSoon)
        mViewDataBinding.btnWalletHistory.setOnClickListener(comingSoon)
        mViewDataBinding.layoutSearch.setOnClickListener(comingSoon)
    }


    private fun initResponse() {

        authenticationViewModel.me()
        authenticationViewModel.meResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    listDataAllSubscription.clear()
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                if (it.data!!.availability == 1) {
                                    mViewDataBinding.btnSwitch.isChecked = true
                                    mViewDataBinding.tvReceivingRequests.text = getString(R.string.available)
                                    mViewDataBinding.tvReceivingRequests.setTextColor(resources.getColor(R.color.secondary))
                                } else if (it.data!!.availability == 0) {
                                    mViewDataBinding.btnSwitch.isChecked = false
                                    mViewDataBinding.tvReceivingRequests.text = getString(R.string.unavailable)
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
                    showProgress(false)
                    Log.i("TestVerification", "error")
                }
                is Resource.Loading -> {
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
                            CODE200 -> {
                                listDataAllSubscription.addAll(it.data!!)
                                allSubscriptionAdapter =
                                    AllSubscriptionAdapter(requireActivity(), listDataAllSubscription, this)
                                mViewDataBinding.rvSubscriptions.adapter = allSubscriptionAdapter
                                allSubscriptionAdapter.notifyDataSetChanged()

                                viewModel.getCategory()
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
                    showProgress(false)
                    Log.i("TestVerification", "error")
                }
                is Resource.Loading -> {
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
                            CODE200 -> {
                                listDataAllOpportunities.addAll(it.data!!)
                                if (listDataAllOpportunities.isNotEmpty()) {
                                    allOpportunitiesAdapter =
                                        AllOpportunitiesAdapter(requireActivity(), listDataAllOpportunities, this)
                                    mViewDataBinding.rvAnOpportunity.adapter = allOpportunitiesAdapter
                                    allOpportunitiesAdapter.notifyDataSetChanged()
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
                    showProgress(false)
                    Log.i("TestVerification", "error")
                }
                is Resource.Loading -> {
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
                            CODE200 -> {
                                listCategories.addAll(it.data!!)
                                categoriesAdapter =
                                    AllCategoriesHomeAdapter(requireActivity(), listCategories, this)
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
                    showProgress(false)
                    Log.i("TestVerification", "error")
                }
                is Resource.Loading -> {
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
                    showProgress(false)
                    Log.i("TestVerification", "error")
                }
                is Resource.Loading -> {
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
                    showProgress(false)
                    Log.i("TestVerification", "error")
                }
                is Resource.Loading -> {
                    Log.i("TestVerification", "loading")
                    showProgress(true)
                }
            }
        })
    }


    private fun onClick() {

        mViewDataBinding.btnSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mViewDataBinding.tvReceivingRequests.text = getString(R.string.available)
                mViewDataBinding.tvReceivingRequests.setTextColor(resources.getColor(R.color.secondary))
            } else {
                mViewDataBinding.tvReceivingRequests.text = getString(R.string.unavailable)
                mViewDataBinding.tvReceivingRequests.setTextColor(resources.getColor(R.color.grey_bold))
            }

            viewModel.updateAvailability()
        }
        mViewDataBinding.tvAllSubscriptions.setOnClickListener {
            val action = HomeFragmentDirections.actionMenuHomeToAllPlansFragment()
            mViewDataBinding.root.findNavController().navigate(action)
        }

        mViewDataBinding.tvAllServices.setOnClickListener {
            mViewDataBinding.root.findNavController().navigate(R.id.teamProjectFragment)
        }
        mViewDataBinding.textViewService.setOnClickListener {
            mViewDataBinding.root.findNavController().navigate(R.id.teamProjectFragment)
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
        if (isConnected) {
            initResponse()
        }
    }


    override fun clickItemShowService(idService: Int) {
        val action =
            HomeFragmentDirections.actionMenuHomeToServiceDetailsFragment(idService, "Home", "Service")
        mViewDataBinding.root.findNavController().navigate(action)
    }

    override fun clickItemOpportunitiesDetails(idOpportunities: Int) {
        val action =
            HomeFragmentDirections.actionMenuHomeToOpportunityDetailsFragment(idOpportunities, "Home")
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