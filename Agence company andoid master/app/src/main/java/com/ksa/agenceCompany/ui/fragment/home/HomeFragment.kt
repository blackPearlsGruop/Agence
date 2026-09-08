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

        // New Figma elements (local/mock — no backend for these yet)
        try {
            setupBeneficiariesFallback()
        } catch (e: Exception) {
            Utilities.showToastError(requireActivity(), "مستفيدين: ${e.message}")
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
            Utilities.showToastError(requireActivity(), "أزرار: ${e.message}")
            Log.e("HomeFragment", "coming soon buttons failed", e)
        }

        onClick()
    }

    private fun setupOpportunitiesFallback() {
        val opportunities = listOf(
            com.ksa.agenceCompany.adapter.OpportunityHome(
                "إعداد حملة إعلانات جوجل", "صيدلية المدينة", "ريال 150/hr",
                listOf("PPC", "Google Ads"), "الموعد النهائي: 15 أغسطس 2026"
            ),
            com.ksa.agenceCompany.adapter.OpportunityHome(
                "تصميم هوية العلامة التجارية", "الفارس للتجزئة", "8,500 ريال",
                listOf("Branding", "Design"), "الموعد النهائي: 20 أغسطس 2026"
            ),
            com.ksa.agenceCompany.adapter.OpportunityHome(
                "استراتيجية سوشيال الربع الثالث", "تِك فيجن", "6,000 ريال",
                listOf("Content", "Social Media"), "الموعد النهائي: 30 أغسطس 2026"
            ),
        )
        val adapter = com.ksa.agenceCompany.adapter.OpportunityHomeAdapter(opportunities) {
            Toast.makeText(requireContext(), getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }
        mViewDataBinding.rvAnOpportunity.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        mViewDataBinding.rvAnOpportunity.adapter = adapter
    }

    private fun setupProjectsFallback() {
        val projects = listOf(
            com.ksa.agenceCompany.adapter.ProjectHome(
                "هوية العلامة التجارية والتموضع", "شركة رياض التقنية", "5 أعضاء"
            ),
            com.ksa.agenceCompany.adapter.ProjectHome(
                "حملة سوشيال ميديا الربع الثالث", "الفارس للتجزئة", "3 أعضاء"
            ),
        )
        val adapter = com.ksa.agenceCompany.adapter.ProjectHomeAdapter(projects) {
            mViewDataBinding.root.findNavController().navigate(R.id.teamProjectFragment)
        }
        mViewDataBinding.rvProjectsHome.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        mViewDataBinding.rvProjectsHome.adapter = adapter
    }

    private fun setupBeneficiariesFallback() {
        val isArabic = com.ksa.agenceCompany.AgenceCompanyApp.pref.getString(com.ksa.agenceCompany.common.LANG, "ar") == "ar"

        val beneficiaries = listOf(
            com.ksa.agenceCompany.adapter.Beneficiary(
                companyAr = "شركة الرياض التقنية", companyEn = "Riyad Tech Co.",
                projectAr = "الهوية البصرية وتحديد الموقع", projectEn = "Brand Identity & Positioning",
                budget = "12,000 ر.س", progress = 45, statusAr = "نشط", statusEn = "Active", statusColorGreen = true
            ),
            com.ksa.agenceCompany.adapter.Beneficiary(
                companyAr = "الفارس للتجزئة", companyEn = "Al-Faris Retail",
                projectAr = "حملة سوشيال ميديا الربع الثالث", projectEn = "Social Media Campaign Q3",
                budget = "8,500 ر.س", progress = 20, statusAr = "قيد المراجعة", statusEn = "In Review", statusColorGreen = false
            ),
            com.ksa.agenceCompany.adapter.Beneficiary(
                companyAr = "صيدلية المدينة", companyEn = "Medina Pharmacy",
                projectAr = "إعداد وإدارة إعلانات جوجل", projectEn = "Google Ads Setup & Management",
                budget = "4,200 ر.س", progress = 70, statusAr = "نشط", statusEn = "Active", statusColorGreen = true
            ),
        )

        val adapter = com.ksa.agenceCompany.adapter.BeneficiariesAdapter(beneficiaries, isArabic) {
            mViewDataBinding.root.findNavController().navigate(R.id.teamProjectFragment)
        }
        mViewDataBinding.rvBeneficiaries.adapter = adapter

        mViewDataBinding.tvWalletBalance.text = "24,500 ريال"
    }

    private fun setupComingSoonButtons() {
        val comingSoon = View.OnClickListener {
            Utilities.showToastError(requireActivity(), getString(R.string.coming_soon))
        }
        mViewDataBinding.btnNotification.setOnClickListener(comingSoon)
        mViewDataBinding.btnViewCandidates.setOnClickListener(comingSoon)
        mViewDataBinding.btnWalletTopup.setOnClickListener(comingSoon)
        mViewDataBinding.btnWalletWithdraw.setOnClickListener(comingSoon)
        mViewDataBinding.btnWalletHistory.setOnClickListener(comingSoon)
        mViewDataBinding.layoutSearch.setOnClickListener(comingSoon)
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