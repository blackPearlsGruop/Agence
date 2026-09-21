package com.ksa.agence.ui.fragment.home

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.adapter.ServiceCompanyAdapter
import com.ksa.agence.app.AgenceApp
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE404
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.LANG
import com.ksa.agence.common.Resource
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentShowCompanyBinding
import com.ksa.agence.entity.showCompaniesResponse.ServiceShowCompaniesResponse
import com.ksa.agence.interfaces.Services
import com.ksa.agence.ui.activity.MainActivity
import com.ksa.agence.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowCompanyFragment : BaseFragment<FragmentShowCompanyBinding>(), Services {

    override fun getLayoutId(): Int = R.layout.fragment_show_company
    private var flagPage: String = ""
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var mainActivity: MainActivity

    private var id_Company: Int = 0

    lateinit var serviceCompanyAdapter: ServiceCompanyAdapter
    lateinit var listServiceData: ArrayList<ServiceShowCompaniesResponse>

    private var currentProviderName: String = ""
    private var currentProviderAgency: String = ""
    private var currentProviderPhoto: String = ""
    private var currentProviderRating: Float = 0f
    private var currentAddress: String = ""

    private val isArabic get() = AgenceApp.pref.getString(LANG, "ar") == "ar"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        // Own light header now (matches Home/Account/Orders/Messages/Support),
        // same pattern used across every redesigned screen this session.
        try {
            mainActivity.mViewDataBinding.constraintLayout2.visibility = View.GONE
        } catch (e: Exception) {
            Log.e("ShowCompanyFragment", "constraintLayout2 hide failed", e)
        }

        mViewDataBinding.ivBack.setOnClickListener {
            mViewDataBinding.root.findNavController().popBackStack()
        }

        if (arguments != null) {

            val args: ShowCompanyFragmentArgs = ShowCompanyFragmentArgs.fromBundle(requireArguments())
            id_Company = args.idCompany
            flagPage = args.flage

            // Mock providers from Home/Orders/AI-Matching use negative ids —
            // show the real Figma reference details instead of calling the
            // (down) API.
            if (id_Company < 0) {
                showProgress(false)
                populateMockCompany(id_Company)
            } else {
                viewModel.showCompanies(id_Company)
            }

        }

        onClick()

    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private fun addBadge(iconRes: Int, colorHex: String) {
        val color = Color.parseColor(colorHex)
        val circle = FrameLayout(requireActivity())
        val size = dp(30)
        val params = LinearLayout.LayoutParams(size, size)
        params.marginEnd = dp(6)
        circle.layoutParams = params

        val bg = GradientDrawable()
        bg.shape = GradientDrawable.OVAL
        bg.setColor(Color.argb(18, Color.red(color), Color.green(color), Color.blue(color)))
        bg.setStroke(dp(2), color)
        circle.background = bg

        val icon = ImageView(requireActivity())
        icon.setImageResource(iconRes)
        icon.setColorFilter(color)
        val iconParams = FrameLayout.LayoutParams(dp(14), dp(14))
        iconParams.gravity = Gravity.CENTER
        icon.layoutParams = iconParams
        circle.addView(icon)

        mViewDataBinding.layoutBadges.addView(circle)
    }

    private fun addPastWorkChip(name: String, colorHex: String) {
        val chip = TextView(requireActivity())
        chip.text = name
        chip.setTextColor(Color.parseColor(colorHex))
        chip.textSize = 12f
        chip.typeface = androidx.core.content.res.ResourcesCompat.getFont(requireActivity(), R.font.somar_bold)
        chip.setPadding(dp(14), dp(8), dp(14), dp(8))
        val bg = GradientDrawable()
        bg.cornerRadius = dp(10).toFloat()
        bg.setColor(resources.getColor(R.color.agence_bg))
        chip.background = bg
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.marginEnd = dp(8)
        chip.layoutParams = params
        mViewDataBinding.layoutPastWork.addView(chip)
    }

    private data class MockProvider(
        val name: String, val logo: String, val tag: String, val tagColorRes: Int,
        val specialty: String, val rating: Float, val reviews: Int, val address: String,
        val agency: String, val brief: String, val badges: List<String>,
        val pastWork: List<Pair<String, String>>, val services: List<Triple<String, String, Int>>
    )

    private fun populateMockCompany(id: Int) {
        listServiceData = ArrayList()

        val mock = when (id) {
            -1 -> MockProvider(
                name = if (isArabic) "فهد العتيبي" else "Fahad Al-Otaibi",
                logo = "https://images.unsplash.com/photo-1616147147027-60d49d3582c4?w=300&h=300&fit=crop&auto=format",
                tag = if (isArabic) "الأعلى تقييماً" else "Top Rated", tagColorRes = R.color.agence_orange,
                specialty = if (isArabic) "استراتيجية العلامة التجارية والسوشيال" else "Brand Strategy & Social",
                rating = 4.9f, reviews = 138,
                address = if (isArabic) "الرياض، المملكة العربية السعودية" else "Riyadh, Saudi Arabia",
                agency = if (isArabic) "وكالة الرواد للتسويق" else "Al-Ruwwad Marketing Agency",
                brief = if (isArabic) "المدير الإبداعي السابق في وكالة Leo Burnett الرياض. 12 عاماً في بناء حملات B2B وحملات المستهلكين عبر دول الخليج." else "Former creative director at Leo Burnett Riyadh. 12 years building B2B and consumer campaigns across the GCC.",
                badges = listOf("verified", "certified", "topPerformer"),
                pastWork = listOf(Pair("stc", "#6B3FA0"), Pair("Noon", "#B8960C"), Pair("Tamara", "#070606")),
                services = listOf(
                    Triple(if (isArabic) "تدقيق العلامة التجارية والتموضع" else "Brand Audit & Positioning", if (isArabic) "أسبوعان" else "2 weeks", 5000),
                    Triple(if (isArabic) "استراتيجية محتوى السوشيال" else "Social Content Strategy", if (isArabic) "أسبوع واحد" else "1 week", 4000),
                    Triple(if (isArabic) "الإدارة الإبداعية للحملة" else "Campaign Creative Direction", if (isArabic) "4 أسابيع" else "4 weeks", 4100)
                )
            )
            -2 -> MockProvider(
                name = if (isArabic) "وكالة نجم" else "Najm Agency",
                logo = "https://images.unsplash.com/photo-1595680337986-ce4862b497b9?w=300&h=300&fit=crop&auto=format",
                tag = if (isArabic) "تسليم سريع" else "Fast Delivery", tagColorRes = R.color.agence_blue,
                specialty = if (isArabic) "الإعلانات المدفوعة والنمو" else "Paid Acquisition & Growth",
                rating = 4.7f, reviews = 94,
                address = if (isArabic) "جدة، المملكة العربية السعودية" else "Jeddah, Saudi Arabia",
                agency = if (isArabic) "وكالة نجم" else "Najm Agency",
                brief = if (isArabic) "متخصصون في تسويق الأداء. أداروا أكثر من 30 مليون ريال سعودي من الإنفاق الإعلاني السنوي في السعودية ودول الخليج." else "Performance marketing specialists. Managed over SAR 30M in annual ad spend across Saudi and GCC.",
                badges = listOf("verified", "certified"),
                pastWork = listOf(Pair(if (isArabic) "شي إن السعودية" else "Shein KSA", "#E91E63"), Pair("Extra", "#1565C0")),
                services = listOf(
                    Triple(if (isArabic) "تدقيق إعلانات ميتا" else "Meta Ads Audit", if (isArabic) "في الساعة" else "Per hour", 150),
                    Triple(if (isArabic) "إعداد القمع التسويقي الكامل" else "Full Funnel Setup", if (isArabic) "في الساعة" else "Per hour", 100),
                    Triple(if (isArabic) "اشتراك شهري" else "Monthly Retainer", if (isArabic) "مستمر" else "Ongoing", 3500)
                )
            )
            -3 -> MockProvider(
                name = if (isArabic) "استديو أثر" else "Athar Studio",
                logo = "https://images.unsplash.com/photo-1690166444476-8cc4c0f24032?w=300&h=300&fit=crop&auto=format",
                tag = if (isArabic) "جديد" else "New", tagColorRes = R.color.agence_orange,
                specialty = if (isArabic) "الهوية البصرية والتصميم" else "Visual Identity & Design",
                rating = 5.0f, reviews = 62,
                address = if (isArabic) "الدمام، المملكة العربية السعودية" else "Dammam, Saudi Arabia",
                agency = if (isArabic) "استديو أثر" else "Athar Studio",
                brief = if (isArabic) "استديو تصميم متخصص في الهوية البصرية وأنظمة العلامة التجارية. مبني للتوسع عبر الوسائط المطبوعة والرقمية." else "Design studio for visual identity and brand systems. Built to scale across print and digital.",
                badges = listOf("verified"),
                pastWork = listOf(Pair(if (isArabic) "طيران الرياض" else "Riyadh Air", "#070606"), Pair(if (isArabic) "الدرعية" else "Diriyah", "#E96D07")),
                services = listOf(
                    Triple(if (isArabic) "الشعار ونظام الهوية" else "Logo & Identity System", if (isArabic) "3 أسابيع" else "3 weeks", 4100),
                    Triple(if (isArabic) "وثيقة دليل العلامة التجارية" else "Brand Guidelines Doc", if (isArabic) "أسبوع واحد" else "1 week", 1400)
                )
            )
            else -> MockProvider(
                name = if (isArabic) "مزوّد الخدمة" else "Service Provider",
                logo = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300&h=300&fit=crop&auto=format",
                tag = if (isArabic) "موثّق" else "Verified", tagColorRes = R.color.agence_blue,
                specialty = if (isArabic) "خدمات تسويقية" else "Marketing Services",
                rating = 4.8f, reviews = 40,
                address = if (isArabic) "الرياض، المملكة العربية السعودية" else "Riyadh, Saudi Arabia",
                agency = if (isArabic) "مزوّد الخدمة" else "Service Provider",
                brief = if (isArabic) "مزوّد خدمة موثّق على منصة أجينس." else "A verified service provider on the Agence platform.",
                badges = listOf("verified"),
                pastWork = emptyList(),
                services = listOf(Triple(if (isArabic) "خدمة عامة" else "General Service", if (isArabic) "أسبوعان" else "2 weeks", 2500))
            )
        }

        Utilities.onLoadImageFromUrl(requireContext(), mock.logo, mViewDataBinding.ivLogoCompany)
        mViewDataBinding.tvTag.text = mock.tag
        mViewDataBinding.tvTag.background = GradientDrawable().apply {
            cornerRadius = dp(999).toFloat()
            setColor(resources.getColor(mock.tagColorRes))
        }
        mViewDataBinding.tvNameCompany.text = mock.name
        mViewDataBinding.tvSpecialty.text = mock.specialty
        mViewDataBinding.ratingBar.rating = mock.rating
        mViewDataBinding.tvCountRat.text = "${mock.reviews} ${getString(R.string.reviews_label)}"
        mViewDataBinding.tvAddressCompany.text = mock.address
        mViewDataBinding.tvAgencyName.text = mock.agency
        mViewDataBinding.tvBrief.text = mock.brief

        // Remembered for the "Get Offer" -> Order Preview handoff.
        currentProviderName = mock.name
        currentProviderAgency = mock.agency
        currentProviderPhoto = mock.logo
        currentProviderRating = mock.rating
        currentAddress = mock.address

        mViewDataBinding.layoutBadges.removeAllViews()
        for (badge in mock.badges) {
            when (badge) {
                "verified" -> addBadge(R.drawable.icon_shield_check, "#2505ED")
                "certified" -> addBadge(R.drawable.icon_award, "#E96D07")
                "topPerformer" -> addBadge(R.drawable.icon_trophy, "#C17F2A")
            }
        }

        mViewDataBinding.layoutPastWork.removeAllViews()
        if (mock.pastWork.isEmpty()) {
            mViewDataBinding.layoutPastWorkCard.visibility = View.GONE
        } else {
            for ((name, color) in mock.pastWork) addPastWorkChip(name, color)
        }

        listServiceData.addAll(
            mock.services.mapIndexed { index, (name, duration, price) ->
                ServiceShowCompaniesResponse(
                    description = duration, id = -(100 + index), images = null,
                    price = price, service_duration_in_days = null, title = name
                )
            }
        )
        serviceCompanyAdapter = ServiceCompanyAdapter(requireActivity(), listServiceData, this)
        mViewDataBinding.rvServices.adapter = serviceCompanyAdapter
        serviceCompanyAdapter.notifyDataSetChanged()
    }

    private fun onClick() {
    }


    private fun initResponse() {

        listServiceData = ArrayList()

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
                                mViewDataBinding.tvNameCompany.text=it.data.title
                                mViewDataBinding.ratingBar.rating= it.data.avg_rate!!.toFloat()
                                mViewDataBinding.tvCountRat.text= "${it.data.rate_count} ${getString(R.string.reviews_label)}"
                                mViewDataBinding.tvAddressCompany.text= "" + it.data.address
                                mViewDataBinding.tvAgencyName.text = "" + it.data.title
                                mViewDataBinding.tvBrief.text= "" + it.data.description
                                mViewDataBinding.tvSpecialty.text = it.data.categories?.firstOrNull()?.title ?: ""
                                mViewDataBinding.tvTag.text = ""
                                mViewDataBinding.layoutBadges.removeAllViews()
                                mViewDataBinding.layoutPastWorkCard.visibility = View.GONE

                                listServiceData.clear()
                                listServiceData.addAll(it.data.services ?: emptyList())
                                serviceCompanyAdapter=ServiceCompanyAdapter(requireActivity(),listServiceData,this)
                                mViewDataBinding.rvServices.adapter=serviceCompanyAdapter
                                serviceCompanyAdapter.notifyDataSetChanged()

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
        if (isConnected) {
            initResponse()
        }
    }

    override fun clickItemServices(idService: Int) {
        val service = listServiceData.find { it.id == idService }
        val bundle = android.os.Bundle().apply {
            putString("providerName", currentProviderName)
            putString("providerAgency", currentProviderAgency)
            putString("providerPhoto", currentProviderPhoto)
            putFloat("providerRating", currentProviderRating)
            putString("address", currentAddress)
            putString("serviceName", service?.title ?: "")
            putString("servicePrice", "${service?.price ?: 0} ${getString(R.string.r_s)}")
            putString("serviceDuration", service?.description ?: "")
            putString("orderNumber", "ORD-${(10000..99999).random()}")
        }
        mViewDataBinding.root.findNavController()
            .navigate(R.id.action_showCompanyFragment_to_orderPreviewFragment, bundle)
    }

    override fun clickItemOfferCompany(idOffer: Int) {
        val action=ShowCompanyFragmentDirections.actionShowCompanyFragmentToQuickOrderFragment(idOffer,0,0,"offer")
        mViewDataBinding.root.findNavController().navigate(action)
    }

}
