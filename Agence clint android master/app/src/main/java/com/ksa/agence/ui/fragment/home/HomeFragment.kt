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
import com.ksa.agence.adapter.PromoBanner
import com.ksa.agence.adapter.PromoBannerAdapter
import com.ksa.agence.adapter.SliderAdapter
import com.ksa.agence.app.AgenceApp
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.LANG
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

    private lateinit var promoBanners: List<PromoBanner>

    lateinit var companyResponse: CompanyResponse

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        requireActivity().showNotificationPermission()

        mainActivity = requireActivity() as MainActivity
        // Belt-and-suspenders: hide the old shared blue toolbar here too (same
        // fix as Messages), in case this screen is reached without going through
        // MainActivity's bottom nav click handler (e.g. state restoration).
        try {
            mainActivity.mViewDataBinding.constraintLayout2.visibility = View.GONE
        } catch (e: Exception) {
            Log.e("HomeFragment", "constraintLayout2 hide failed", e)
        }
        listCategories = ArrayList()
        listCompany = ArrayList()
        imageList = ArrayList()

        // Show local promo banners right away — no server needed. If the real
        // banner API succeeds later, its own observer swaps in real data.
        try {
            setupPromoBannerFallback()
        } catch (e: Exception) {
            Utilities.showToastError(requireActivity(), "بانر: ${e.message}")
        }

        // Same idea for the providers/companies list: show realistic sample
        // providers right away so the section isn't empty while the server is
        // down. If the real API succeeds later, its own observer replaces this.
        try {
            setupMockCompaniesFallback()
        } catch (e: Exception) {
            Log.e("HomeFragment", "mock companies failed", e)
        }

        // Same idea for the category chips row.
        try {
            setupMockCategoriesFallback()
        } catch (e: Exception) {
            Log.e("HomeFragment", "mock categories failed", e)
        }

        // Featured Members grid (local data, PRO badges + upgrade CTA)
        try {
            setupFeaturedMembersFallback()
        } catch (e: Exception) {
            Utilities.showToastError(requireActivity(), "أعضاء: ${e.message}")
            Log.e("HomeFragment", "featured members failed", e)
        }

        onClick()



    }

    private fun setupPromoBannerFallback() {
        val isArabic = AgenceApp.pref.getString(LANG, "ar") == "ar"

        promoBanners = listOf(
            PromoBanner(
                titleAr = "وسّع شبكة عملائك",
                titleEn = "Grow your client network",
                subtitleAr = "انضم كمزوّد خدمة وابدأ تستقبل طلبات اليوم",
                subtitleEn = "Join as a service provider and start receiving requests today",
                backgroundRes = R.drawable.bg_promo_banner_1
            ),
            PromoBanner(
                titleAr = "خصم 20% على أول طلب",
                titleEn = "20% off your first order",
                subtitleAr = "لفترة محدودة على جميع الخدمات",
                subtitleEn = "Limited time on all services",
                backgroundRes = R.drawable.bg_promo_banner_2,
                isAd = true
            )
        )

        mViewDataBinding.sliderViewPager2.adapter = PromoBannerAdapter(promoBanners, isArabic) { banner ->
            if (banner.isAd) {
                mViewDataBinding.root.findNavController().navigate(R.id.action_menuHome_to_paidAdViewFragment)
            }
        }
        mViewDataBinding.constraintLayout5.visibility = View.VISIBLE
        setupBannerDots(promoBanners.size)

        mViewDataBinding.sliderViewPager2.registerOnPageChangeCallback(object :
            androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateActiveDot(position)
            }
        })
    }

    private fun setupBannerDots(count: Int) {
        val dotsLayout = mViewDataBinding.layoutBannerDots
        dotsLayout.removeAllViews()
        val density = resources.displayMetrics.density
        val size = (6 * density).toInt()
        val margin = (4 * density).toInt()
        for (i in 0 until count) {
            val dot = View(requireActivity())
            val params = android.widget.LinearLayout.LayoutParams(size, size)
            params.marginEnd = margin
            dot.layoutParams = params
            dot.setBackgroundResource(
                if (i == 0) R.drawable.bg_promo_dot_active else R.drawable.bg_promo_dot_inactive
            )
            dotsLayout.addView(dot)
        }
    }

    private fun updateActiveDot(activePosition: Int) {
        val dotsLayout = mViewDataBinding.layoutBannerDots
        for (i in 0 until dotsLayout.childCount) {
            dotsLayout.getChildAt(i).setBackgroundResource(
                if (i == activePosition) R.drawable.bg_promo_dot_active else R.drawable.bg_promo_dot_inactive
            )
        }
    }

    private fun setupMockCompaniesFallback() {
        val isArabic = AgenceApp.pref.getString(LANG, "ar") == "ar"
        val mockCategory = { name: String ->
            com.ksa.agence.entity.companyResponse.Category(
                description = null, icon = null, id = 1, is_consultant = 0, title = name
            )
        }

        val mockProviders = listOf(
            DataCompanyResponse(
                account_type = "individual", address = if (isArabic) "الرياض، المملكة العربية السعودية" else "Riyadh, Saudi Arabia", availability = 1,
                avg_rate = 4.9, categories = listOf(mockCategory(if (isArabic) "الأعلى تقييماً" else "Top Rated")),
                company_background_image = null,
                company_logo = "https://images.unsplash.com/photo-1616147147027-60d49d3582c4?w=200&h=200&fit=crop&auto=format",
                consultant_price = null,
                created_at = null, currentPlan = null, default_lang = "ar",
                description = if (isArabic) "استراتيجية العلامة التجارية والسوشيال" else "Brand Strategy & Social",
                enable_notification = 1, id = -1, is_added_favourite = false,
                is_subscribed_to_free_plan = 0, plan_end_at = null, price_start_from = 5000,
                rate_count = 138, title = if (isArabic) "فهد العتيبي" else "Fahad Al-Otaibi"
            ),
            DataCompanyResponse(
                account_type = "company", address = if (isArabic) "جدة، المملكة العربية السعودية" else "Jeddah, Saudi Arabia", availability = 1,
                avg_rate = 4.7, categories = listOf(mockCategory(if (isArabic) "تسليم سريع" else "Fast Delivery")),
                company_background_image = null,
                company_logo = "https://images.unsplash.com/photo-1595680337986-ce4862b497b9?w=200&h=200&fit=crop&auto=format",
                consultant_price = null,
                created_at = null, currentPlan = null, default_lang = "ar",
                description = if (isArabic) "الإعلانات المدفوعة والنمو" else "Paid Acquisition & Growth",
                enable_notification = 1, id = -2, is_added_favourite = false,
                is_subscribed_to_free_plan = 0, plan_end_at = null, price_start_from = 150,
                rate_count = 94, title = if (isArabic) "وكالة نجم" else "Najm Agency"
            ),
            DataCompanyResponse(
                account_type = "company", address = if (isArabic) "الدمام، المملكة العربية السعودية" else "Dammam, Saudi Arabia", availability = 1,
                avg_rate = 5.0, categories = listOf(mockCategory(if (isArabic) "جديد" else "New")),
                company_background_image = null,
                company_logo = "https://images.unsplash.com/photo-1690166444476-8cc4c0f24032?w=200&h=200&fit=crop&auto=format",
                consultant_price = null,
                created_at = null, currentPlan = null, default_lang = "ar",
                description = if (isArabic) "الهوية البصرية والتصميم" else "Visual Identity & Design",
                enable_notification = 1, id = -3, is_added_favourite = false,
                is_subscribed_to_free_plan = 0, plan_end_at = null, price_start_from = 4100,
                rate_count = 62, title = if (isArabic) "استديو أثر" else "Athar Studio"
            ),
        )

        listCompany.clear()
        listCompany.addAll(mockProviders)
        companyAdapter = CopanyAdapter(requireActivity(), listCompany, this)
        mViewDataBinding.rvCompany.adapter = companyAdapter
        companyAdapter.notifyDataSetChanged()
    }

    private fun setupMockCategoriesFallback() {
        val isArabic = AgenceApp.pref.getString(LANG, "ar") == "ar"

        val mockCategories = listOf(
            com.ksa.agence.entity.categoriesResponse.DataCategoriesResponse(
                description = null, icon = null, id = -1, is_consultant = 0,
                title = if (isArabic) "الكل" else "All", isSelected = true
            ),
            com.ksa.agence.entity.categoriesResponse.DataCategoriesResponse(
                description = null, icon = null, id = -2, is_consultant = 0,
                title = if (isArabic) "سوشيال ميديا" else "Social Media"
            ),
            com.ksa.agence.entity.categoriesResponse.DataCategoriesResponse(
                description = null, icon = null, id = -3, is_consultant = 0,
                title = if (isArabic) "تصميم" else "Design"
            ),
            com.ksa.agence.entity.categoriesResponse.DataCategoriesResponse(
                description = null, icon = null, id = -4, is_consultant = 0,
                title = if (isArabic) "إعلانات مدفوعة" else "Paid Ads"
            ),
            com.ksa.agence.entity.categoriesResponse.DataCategoriesResponse(
                description = null, icon = null, id = -5, is_consultant = 0,
                title = if (isArabic) "استشارات" else "Consulting"
            ),
        )

        listCategories.clear()
        listCategories.addAll(mockCategories)
        categoriesAdapter = CategoriesAdapter(requireActivity(), listCategories, this)
        mViewDataBinding.rvCategory.adapter = categoriesAdapter
        categoriesAdapter.notifyDataSetChanged()
    }

    private fun setupFeaturedMembersFallback() {
        val isArabic = AgenceApp.pref.getString(LANG, "ar") == "ar"

        val members = listOf(
            com.ksa.agence.adapter.FeaturedMember(
                photoUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&h=200&fit=crop&auto=format",
                nameAr = "James Whitfield", nameEn = "James Whitfield",
                specialtyAr = "استراتيجية العلامة التجارية", specialtyEn = "Brand Strategy",
                locationAr = "لندن، المملكة المتحدة", locationEn = "London, UK",
                rating = 4.9f, reviews = 128, matchPct = 97,
                bioAr = "استراتيجي علامات تجارية أول بخبرة تزيد على 10 سنوات في بناء هويات احترافية للشركات العالمية. متخصص في تحديد موقع السوق ورواية القصص البصرية.",
                bioEn = "Senior brand strategist with 10+ years helping global businesses build iconic identities. Specialises in market positioning and visual storytelling.",
                servicesAr = listOf("تدقيق العلامة التجارية", "تصميم الهوية", "تحديد موقع السوق"),
                servicesEn = listOf("Brand Audit", "Identity Design", "Market Positioning")
            ),
            com.ksa.agence.adapter.FeaturedMember(
                photoUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=200&h=200&fit=crop&auto=format",
                nameAr = "Sophie Hartley", nameEn = "Sophie Hartley",
                specialtyAr = "سوشيال ميديا والمحتوى", specialtyEn = "Social Media & Content",
                locationAr = "مانشستر، المملكة المتحدة", locationEn = "Manchester, UK",
                rating = 4.8f, reviews = 94, matchPct = 94,
                bioAr = "استراتيجية محتوى إبداعية متخصصة في منصات التواصل الاجتماعي متعددة اللغات. أدارت قنوات تجاوزت 2 مليون متابع لعلامات FMCG والأسلوب الحياتي.",
                bioEn = "Creative content strategist focused on multilingual social media. Built and managed channels exceeding 2M followers for lifestyle and FMCG brands.",
                servicesAr = listOf("تخطيط المحتوى", "إعلانات سوشيال", "إدارة المجتمع"),
                servicesEn = listOf("Content Calendar", "Social Ads", "Community Management")
            ),
            com.ksa.agence.adapter.FeaturedMember(
                photoUrl = "https://images.unsplash.com/photo-1560250097-0b93528c311a?w=200&h=200&fit=crop&auto=format",
                nameAr = "Oliver Pemberton", nameEn = "Oliver Pemberton",
                specialtyAr = "الإعلانات المدفوعة", specialtyEn = "Paid Advertising",
                locationAr = "برمنغهام، المملكة المتحدة", locationEn = "Birmingham, UK",
                rating = 4.7f, reviews = 77, matchPct = 91,
                bioAr = "خبير تسويق أداء يدير حملات Google وMeta وTikTok وSnapchat. متوسط عائد الإنفاق الإعلاني للعملاء 4.2×.",
                bioEn = "Performance marketing expert running Google, Meta, TikTok and Snapchat campaigns. Average client ROAS of 4.2×.",
                servicesAr = listOf("إعلانات جوجل", "إعلانات ميتا", "حملات تيك توك"),
                servicesEn = listOf("Google Ads", "Meta Ads", "TikTok Campaigns")
            ),
            com.ksa.agence.adapter.FeaturedMember(
                photoUrl = "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=200&h=200&fit=crop&auto=format",
                nameAr = "Charlotte Moore", nameEn = "Charlotte Moore",
                specialtyAr = "تصميم تجربة المستخدم", specialtyEn = "UI/UX & Digital Design",
                locationAr = "إدنبرة، المملكة المتحدة", locationEn = "Edinburgh, UK",
                rating = 4.9f, reviews = 112, matchPct = 96,
                bioAr = "مصممة رقمية حائزة على جوائز متخصصة في الواجهات ثنائية اللغة. نفّذت منتجات لعملاء التقنية المالية والرعاية الصحية والتجارة الإلكترونية في MENA وأوروبا.",
                bioEn = "Award-winning digital designer with a focus on bilingual interfaces. Delivered products for fintech, healthcare and e-commerce clients across MENA and Europe.",
                servicesAr = listOf("تصميم التطبيق", "هوية بصرية", "أنظمة التصميم"),
                servicesEn = listOf("App Design", "Brand Identity", "Design Systems")
            ),
            com.ksa.agence.adapter.FeaturedMember(
                photoUrl = "https://images.unsplash.com/photo-1741241290790-0b69e17888da?w=200&h=200&fit=crop&auto=format",
                nameAr = "Ethan Clarke", nameEn = "Ethan Clarke",
                specialtyAr = "تحسين محركات البحث والنمو", specialtyEn = "SEO & Growth",
                locationAr = "بريستول، المملكة المتحدة", locationEn = "Bristol, UK",
                rating = 4.7f, reviews = 63, matchPct = 89,
                bioAr = "متخصص SEO وخبير نمو يتخصص في البحث متعدد اللغات. ساعد أكثر من 30 علامة تجارية على مضاعفة حركتها العضوية ثلاث مرات خلال 6 أشهر.",
                bioEn = "SEO specialist and growth hacker focusing on multilingual search. Helped 30+ brands triple their organic traffic within 6 months.",
                servicesAr = listOf("تدقيق SEO", "محتوى SEO", "إعداد التحليلات"),
                servicesEn = listOf("SEO Audit", "Content SEO", "Analytics Setup")
            ),
            com.ksa.agence.adapter.FeaturedMember(
                photoUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=200&h=200&fit=crop&auto=format",
                nameAr = "Isabelle Grant", nameEn = "Isabelle Grant",
                specialtyAr = "التسويق عبر المؤثرين", specialtyEn = "Influencer Marketing",
                locationAr = "ليدز، المملكة المتحدة", locationEn = "Leeds, UK",
                rating = 4.8f, reviews = 88, matchPct = 93,
                bioAr = "مديرة علاقات مؤثرين بشبكة تضم أكثر من 500 مبدع موثّق. تتخصص في حملات أصيلة لعلامات الجمال وأسلوب الحياة والمطاعم.",
                bioEn = "Influencer relations manager with a network of 500+ verified creators. Specialises in authentic campaigns for beauty, lifestyle and F&B brands.",
                servicesAr = listOf("اختيار المؤثرين", "إدارة الحملة", "تقارير الأداء"),
                servicesEn = listOf("Influencer Casting", "Campaign Management", "Performance Reports")
            ),
            com.ksa.agence.adapter.FeaturedMember(
                photoUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200&h=200&fit=crop&auto=format",
                nameAr = "William Foster", nameEn = "William Foster",
                specialtyAr = "إنتاج الفيديو", specialtyEn = "Video Production",
                locationAr = "غلاسكو، المملكة المتحدة", locationEn = "Glasgow, UK",
                rating = 4.6f, reviews = 51, matchPct = 87,
                bioAr = "مصوّر ومخرج متخصص في الأفلام التجارية للعلامات التجارية والفعاليات المؤسسية والحملات الرقمية.",
                bioEn = "Videographer and director specialising in commercial brand films for corporate events and digital campaigns.",
                servicesAr = listOf("أفلام تجارية", "تغطية فعاليات", "فيديوهات سوشيال"),
                servicesEn = listOf("Brand Films", "Event Coverage", "Social Videos")
            ),
            com.ksa.agence.adapter.FeaturedMember(
                photoUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200&h=200&fit=crop&auto=format",
                nameAr = "Amelia Thornton", nameEn = "Amelia Thornton",
                specialtyAr = "العلاقات العامة", specialtyEn = "Public Relations",
                locationAr = "لندن، المملكة المتحدة", locationEn = "London, UK",
                rating = 4.7f, reviews = 72, matchPct = 90,
                bioAr = "مستشارة علاقات عامة أولى بعلاقات وثيقة مع الإعلام الدولي والشرق الأوسطي. خبيرة في التواصل في الأزمات والبيانات الصحفية وتحديد موقع التنفيذيين.",
                bioEn = "Senior PR consultant with deep ties to international and Middle East media. Expert in crisis communications, press releases and executive positioning.",
                servicesAr = listOf("علاقات إعلامية", "إدارة الأزمات", "بيانات صحفية"),
                servicesEn = listOf("Media Relations", "Crisis Comms", "Press Releases")
            ),
            com.ksa.agence.adapter.FeaturedMember(
                photoUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=200&h=200&fit=crop&auto=format",
                nameAr = "George Caldwell", nameEn = "George Caldwell",
                specialtyAr = "الاستشارات التسويقية", specialtyEn = "Marketing Consulting",
                locationAr = "أوكسفورد، المملكة المتحدة", locationEn = "Oxford, UK",
                rating = 4.8f, reviews = 99, matchPct = 95,
                bioAr = "مستشار تسويق بمستوى CMO للشركات الدولية الراغبة في التوسع في أسواق MENA. يقدّم خطط طرح للسوق مدتها 90 يومًا مستندةً إلى أبحاث المستهلك المحلي.",
                bioEn = "CMO-level marketing consultant for international companies expanding into MENA markets. Delivers 90-day go-to-market plans grounded in local consumer research.",
                servicesAr = listOf("استراتيجية الطرح للسوق", "أبحاث المستهلك", "تخطيط الحملات"),
                servicesEn = listOf("GTM Strategy", "Consumer Research", "Campaign Planning")
            ),
        )

        val featuredAdapter = com.ksa.agence.adapter.FeaturedMembersAdapter(requireActivity(), members, isArabic) { member ->
            val bundle = android.os.Bundle().apply {
                putString("name", if (isArabic) member.nameAr else member.nameEn)
                putString("specialty", if (isArabic) member.specialtyAr else member.specialtyEn)
                putString("location", if (isArabic) member.locationAr else member.locationEn)
                putString("bio", if (isArabic) member.bioAr else member.bioEn)
                putString("photo", member.photoUrl)
                putFloat("rating", member.rating)
                putInt("reviews", member.reviews)
                putInt("matchPct", member.matchPct)
                putStringArrayList("services", ArrayList(if (isArabic) member.servicesAr else member.servicesEn))
            }
            mViewDataBinding.root.findNavController()
                .navigate(R.id.action_menuHome_to_featuredMemberProfileFragment, bundle)
        }
        mViewDataBinding.rvFeaturedMembers.layoutManager =
            androidx.recyclerview.widget.GridLayoutManager(requireActivity(), 3)
        mViewDataBinding.rvFeaturedMembers.adapter = featuredAdapter

        val upgradeClick = View.OnClickListener {
            com.ksa.agence.ui.dialog.UpgradeFeaturedDialog().show(childFragmentManager, "upgrade_featured")
        }
        mViewDataBinding.btnUpgrade.setOnClickListener(upgradeClick)
        mViewDataBinding.layoutUpgradeTeaser.setOnClickListener(upgradeClick)
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
                                createSlideShow()

                                if (imageList.size==0){
                                    // Real banners came back empty — keep the local promo
                                    // fallback that's already showing instead of hiding it.
                                    setupPromoBannerFallback()
                                }
                                else
                                {
                                    mViewDataBinding.constraintLayout5.visibility=View.VISIBLE
                                    setupBannerDots(imageList.size)
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

        mViewDataBinding.btnAiMatching.setOnClickListener {
            val action = HomeFragmentDirections.actionMenuHomeToAiMatchingFragment()
            mViewDataBinding.root.findNavController().navigate(action)
        }

        val runSearch = {
            val query = mViewDataBinding.etSearch.text.toString().trim()
            val bundle = android.os.Bundle().apply { putString("searchQuery", query) }
            mViewDataBinding.root.findNavController().navigate(R.id.allComanyFragment, bundle)
        }
        mViewDataBinding.etSearch.setOnEditorActionListener { _, _, _ -> runSearch(); true }
        mViewDataBinding.ivSearchIcon.setOnClickListener { runSearch() }

        mViewDataBinding.btnFilter.setOnClickListener {
            mViewDataBinding.root.findNavController().navigate(R.id.allComanyFragment)
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