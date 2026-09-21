package com.ksa.agence.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.adapter.AllCategoriesAdapter
import com.ksa.agence.adapter.CopanyAdapter
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentAllServiesBinding
import com.ksa.agence.databinding.FragmentChatBinding
import com.ksa.agence.databinding.FragmentChoosePageBinding
import com.ksa.agence.databinding.FragmentHomeBinding
import com.ksa.agence.databinding.FragmentOffersBinding
import com.ksa.agence.databinding.FragmentShowServiesBinding
import com.ksa.agence.entity.categoriesResponse.DataCategoriesResponse
import com.ksa.agence.entity.companyResponse.DataCompanyResponse
import com.ksa.agence.interfaces.Company
import com.ksa.agence.ui.activity.MainActivity
import com.ksa.agence.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowSreviesFragment : BaseFragment<FragmentShowServiesBinding>(), Company {

    override fun getLayoutId(): Int = R.layout.fragment_show_servies
    private lateinit var flagPage: String
    private lateinit var mainActivity: MainActivity


    private var idServise: Int=0
    private var position: Int=0
    private val viewModel: HomeViewModel by viewModel()

    lateinit var companyAdapter: CopanyAdapter
    lateinit var  listCompany: ArrayList<DataCompanyResponse>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.services)

        listCompany=ArrayList()

        if (arguments != null){

            val args:ShowSreviesFragmentArgs=ShowSreviesFragmentArgs.fromBundle(requireArguments())
            idServise=args.idServise
            flagPage=args.flage

            // Mock category chips on Home use negative ids — show a realistic
            // local provider list instead of calling the (down) API.
            if (idServise < 0) {
                showProgress(false)
                populateMockServiceResults(idServise)
            } else {
                viewModel.categoriesById(idServise)
            }

        }

    }

    private fun populateMockServiceResults(categoryId: Int) {
        val isArabic = com.ksa.agence.app.AgenceApp.pref.getString(com.ksa.agence.common.LANG, "ar") == "ar"

        val categoryTitle = when (categoryId) {
            -1 -> if (isArabic) "الكل" else "All"
            -2 -> if (isArabic) "سوشيال ميديا" else "Social Media"
            -3 -> if (isArabic) "تصميم" else "Design"
            -4 -> if (isArabic) "إعلانات مدفوعة" else "Paid Ads"
            -5 -> if (isArabic) "استشارات" else "Consulting"
            else -> if (isArabic) "الخدمات" else "Services"
        }
        mViewDataBinding.tvTitleService.text = categoryTitle

        val mockCategory = { name: String ->
            com.ksa.agence.entity.companyResponse.Category(
                description = null, icon = null, id = 1, is_consultant = 0, title = name
            )
        }

        val mockProviders = listOf(
            DataCompanyResponse(
                account_type = "individual", address = if (isArabic) "الرياض، المملكة العربية السعودية" else "Riyadh, Saudi Arabia", availability = 1,
                avg_rate = 4.9, categories = listOf(mockCategory(categoryTitle)),
                company_background_image = null,
                company_logo = "https://images.unsplash.com/photo-1616147147027-60d49d3582c4?w=200&h=200&fit=crop&auto=format",
                consultant_price = null, created_at = null, currentPlan = null, default_lang = "ar",
                description = if (isArabic) "استراتيجية العلامة التجارية والسوشيال" else "Brand Strategy & Social",
                enable_notification = 1, id = -1, is_added_favourite = false,
                is_subscribed_to_free_plan = 0, plan_end_at = null, price_start_from = 5000,
                rate_count = 138, title = if (isArabic) "فهد العتيبي" else "Fahad Al-Otaibi"
            ),
            DataCompanyResponse(
                account_type = "company", address = if (isArabic) "جدة، المملكة العربية السعودية" else "Jeddah, Saudi Arabia", availability = 1,
                avg_rate = 4.7, categories = listOf(mockCategory(categoryTitle)),
                company_background_image = null,
                company_logo = "https://images.unsplash.com/photo-1595680337986-ce4862b497b9?w=200&h=200&fit=crop&auto=format",
                consultant_price = null, created_at = null, currentPlan = null, default_lang = "ar",
                description = if (isArabic) "الإعلانات المدفوعة والنمو" else "Paid Acquisition & Growth",
                enable_notification = 1, id = -2, is_added_favourite = false,
                is_subscribed_to_free_plan = 0, plan_end_at = null, price_start_from = 150,
                rate_count = 94, title = if (isArabic) "وكالة نجم" else "Najm Agency"
            ),
            DataCompanyResponse(
                account_type = "company", address = if (isArabic) "الدمام، المملكة العربية السعودية" else "Dammam, Saudi Arabia", availability = 1,
                avg_rate = 5.0, categories = listOf(mockCategory(categoryTitle)),
                company_background_image = null,
                company_logo = "https://images.unsplash.com/photo-1690166444476-8cc4c0f24032?w=200&h=200&fit=crop&auto=format",
                consultant_price = null, created_at = null, currentPlan = null, default_lang = "ar",
                description = if (isArabic) "الهوية البصرية والتصميم" else "Visual Identity & Design",
                enable_notification = 1, id = -3, is_added_favourite = false,
                is_subscribed_to_free_plan = 0, plan_end_at = null, price_start_from = 4100,
                rate_count = 62, title = if (isArabic) "استديو أثر" else "Athar Studio"
            )
        )

        listCompany.clear()
        listCompany.addAll(mockProviders)
        companyAdapter = CopanyAdapter(requireActivity(), listCompany, this)
        mViewDataBinding.rvAllCompany.adapter = companyAdapter
        companyAdapter.notifyDataSetChanged()
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

                                mViewDataBinding.tvTitleService.text=it.data!!.title
                                listCompany.addAll(it.data!!.companies!!)
                                companyAdapter =
                                    CopanyAdapter(requireActivity(), listCompany,this)
                                mViewDataBinding.rvAllCompany.adapter = companyAdapter
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
        val action=ShowSreviesFragmentDirections.actionShowSreviesFragmentToShowCompanyFragment(idCompany,"showService")
        mViewDataBinding.root.findNavController().navigate(action)
    }

    override fun clickItemAddCompanyFav(idCompany: Int, pos: Int) {
        position=pos
        viewModel.addFavourites(idCompany)
    }

    override fun clickItemShowService(idCompany: Int) {

    }


    override fun onDestroy() {
        super.onDestroy()
        mainActivity.showHomeToolbar()

        if (flagPage=="Home")
        {
            mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.home)

        }
        else
        {
            mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.services)

        }

    }

}