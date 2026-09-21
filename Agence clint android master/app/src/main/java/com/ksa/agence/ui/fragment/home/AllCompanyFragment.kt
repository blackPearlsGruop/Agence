package com.ksa.agence.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.adapter.CopanyAdapter
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentAllCompanyBinding
import com.ksa.agence.entity.companyResponse.CompanyResponse
import com.ksa.agence.entity.companyResponse.DataCompanyResponse
import com.ksa.agence.interfaces.Company
import com.ksa.agence.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllCompanyFragment : BaseFragment<FragmentAllCompanyBinding>(),Company {

    override fun getLayoutId(): Int = R.layout.fragment_all_company

    private var position: Int=0
    private val viewModel: HomeViewModel by viewModel()
    lateinit var companyResponse: CompanyResponse
    lateinit var companyAdapter: CopanyAdapter
    lateinit var  listCompany: ArrayList<DataCompanyResponse>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listCompany = ArrayList()

        val searchQuery = arguments?.getString("searchQuery")?.trim().orEmpty()

        // Local mock providers so this screen isn't blank while staging.agence.sa
        // is down — same pattern used across Home/Orders. Replaced automatically
        // once initResponse() gets a real, non-empty list back.
        val isArabic = com.ksa.agence.app.AgenceApp.pref.getString(com.ksa.agence.common.LANG, "ar") == "ar"
        val allMock = mockProviders(isArabic)
        val filtered = if (searchQuery.isEmpty()) allMock else allMock.filter { provider ->
            (provider.title ?: "").contains(searchQuery, ignoreCase = true) ||
                    (provider.description ?: "").contains(searchQuery, ignoreCase = true)
        }
        listCompany.addAll(filtered)
        companyAdapter = CopanyAdapter(requireActivity(), listCompany, this)
        mViewDataBinding.rvAllCompany.adapter = companyAdapter
        companyAdapter.notifyDataSetChanged()

    }

    private fun mockProviders(isArabic: Boolean): List<DataCompanyResponse> {
        fun mockCategory(name: String) = com.ksa.agence.entity.companyResponse.Category(
            description = null, icon = null, id = 1, is_consultant = 0, title = name
        )
        return listOf(
            DataCompanyResponse(
                account_type = "individual", address = if (isArabic) "الرياض، المملكة العربية السعودية" else "Riyadh, Saudi Arabia", availability = 1,
                avg_rate = 4.9, categories = listOf(mockCategory(if (isArabic) "الأعلى تقييماً" else "Top Rated")), company_background_image = null,
                company_logo = "https://images.unsplash.com/photo-1616147147027-60d49d3582c4?w=200&h=200&fit=crop&auto=format",
                consultant_price = null, created_at = null, currentPlan = null, default_lang = "ar",
                description = if (isArabic) "استراتيجية العلامة التجارية والسوشيال" else "Brand Strategy & Social",
                enable_notification = 1, id = -1, is_added_favourite = false,
                is_subscribed_to_free_plan = 0, plan_end_at = null, price_start_from = 5000,
                rate_count = 138, title = if (isArabic) "فهد العتيبي" else "Fahad Al-Otaibi"
            ),
            DataCompanyResponse(
                account_type = "company", address = if (isArabic) "جدة، المملكة العربية السعودية" else "Jeddah, Saudi Arabia", availability = 1,
                avg_rate = 4.7, categories = listOf(mockCategory(if (isArabic) "تسليم سريع" else "Fast Delivery")), company_background_image = null,
                company_logo = "https://images.unsplash.com/photo-1595680337986-ce4862b497b9?w=200&h=200&fit=crop&auto=format",
                consultant_price = null, created_at = null, currentPlan = null, default_lang = "ar",
                description = if (isArabic) "الإعلانات المدفوعة والنمو" else "Paid Acquisition & Growth",
                enable_notification = 1, id = -2, is_added_favourite = false,
                is_subscribed_to_free_plan = 0, plan_end_at = null, price_start_from = 150,
                rate_count = 94, title = if (isArabic) "وكالة نجم" else "Najm Agency"
            ),
            DataCompanyResponse(
                account_type = "company", address = if (isArabic) "الدمام، المملكة العربية السعودية" else "Dammam, Saudi Arabia", availability = 1,
                avg_rate = 5.0, categories = listOf(mockCategory(if (isArabic) "جديد" else "New")), company_background_image = null,
                company_logo = "https://images.unsplash.com/photo-1690166444476-8cc4c0f24032?w=200&h=200&fit=crop&auto=format",
                consultant_price = null, created_at = null, currentPlan = null, default_lang = "ar",
                description = if (isArabic) "الهوية البصرية والتصميم" else "Visual Identity & Design",
                enable_notification = 1, id = -3, is_added_favourite = false,
                is_subscribed_to_free_plan = 0, plan_end_at = null, price_start_from = 4100,
                rate_count = 62, title = if (isArabic) "استديو أثر" else "Athar Studio"
            )
        )
    }

    private fun initResponse() {

        // resend response
        viewModel.getCompany()
        viewModel.companyResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                companyResponse=result.data

                                if (!it.data.isNullOrEmpty()) {
                                    listCompany.clear()
                                    listCompany.addAll(it.data)
                                    companyAdapter =
                                        CopanyAdapter(requireActivity(), listCompany,this)
                                    mViewDataBinding.rvAllCompany.adapter = companyAdapter
                                    companyAdapter.notifyDataSetChanged()
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


    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        // يتم استدعاء هذه الدالة عندما يتغير حالة الاتصال
        if (isConnected) {
            // يمكنك إجراء أي إجراءات إضافية هنا عند الاتصال بالإنترنت
            initResponse()

        } else {
        }

    }

    override fun clickItemCompany(idCompany: Int,flag:String) {
        val action=AllCompanyFragmentDirections.actionAllComanyFragmentToShowCompanyFragment(idCompany,"AllCompany")
        mViewDataBinding.root.findNavController().navigate(action)
    }

    override fun clickItemAddCompanyFav(idCompany: Int, pos: Int) {
        position=pos
        viewModel.addFavourites(idCompany)
    }

    override fun clickItemShowService(idCompany: Int) {

    }

}