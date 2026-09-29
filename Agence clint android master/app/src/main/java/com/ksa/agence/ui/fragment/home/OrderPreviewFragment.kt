package com.ksa.agence.ui.fragment.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentOrderPreviewBinding

// Screen shown when tapping "Pay" on a service in a provider's profile
// (matches the Figma "Order" screen exactly).
class OrderPreviewFragment : BaseFragment<FragmentOrderPreviewBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_order_preview

    private var providerName: String = ""
    private var providerAgency: String = ""
    private var providerPhoto: String = ""
    private var servicePrice: String = ""
    private var serviceName: String = ""
    private var orderNumber: String = ""
    private var serviceDuration: String = ""
    private var address: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            (requireActivity() as com.ksa.agence.ui.activity.MainActivity)
                .mViewDataBinding.constraintLayout2.visibility = View.GONE
        } catch (e: Exception) {
        }

        // Hide the floating Quick Order button so it doesn't overlap
        try {
            (requireActivity() as com.ksa.agence.ui.activity.MainActivity)
                .mViewDataBinding.btnQuickOrder.visibility = View.GONE
        } catch (e: Exception) {
        }

        // Read args from the previous screen
        val args = arguments
        providerName = args?.getString("providerName") ?: ""
        providerAgency = args?.getString("providerAgency") ?: ""
        providerPhoto = args?.getString("providerPhoto") ?: ""
        val providerRating = args?.getFloat("providerRating") ?: 0f
        address = args?.getString("address") ?: ""
        serviceName = args?.getString("serviceName") ?: ""
        servicePrice = args?.getString("servicePrice") ?: ""
        serviceDuration = args?.getString("serviceDuration") ?: ""
        orderNumber = args?.getString("orderNumber") ?: ""

        // Fill mock defaults when args are empty (or come back as zero-price)
        // so the screen never looks half-empty during demo/preview flows.
        if (providerName.isEmpty()) providerName = "فهد العتيبي"
        if (providerAgency.isEmpty()) providerAgency = "وكالة الرواد للتسويق"
        if (address.isEmpty()) address = "الرياض، المملكة العربية السعودية"
        if (serviceName.isEmpty()) serviceName = "Brand Audit & Positioning"
        // Treat "0", "0 ر.س", or empty as missing price
        val priceDigits = servicePrice.replace("[^0-9]".toRegex(), "").trim()
        if (servicePrice.isEmpty() || priceDigits.isEmpty() || priceDigits == "0") {
            servicePrice = "5,750 ر.س"
        }
        if (serviceDuration.isEmpty()) serviceDuration = "أسبوعان"
        if (orderNumber.isEmpty()) orderNumber = "ORD-23611"
        val effectiveRating = if (providerRating > 0f) providerRating else 4.9f

        if (providerPhoto.isNotEmpty()) {
            Utilities.onLoadImageFromUrl(
                requireActivity(),
                providerPhoto,
                mViewDataBinding.ivProviderPhoto
            )
        }
        mViewDataBinding.tvProviderName.text = providerName
        mViewDataBinding.tvProviderAgency.text = providerAgency
        mViewDataBinding.tvProviderRating.text = effectiveRating.toString()
        mViewDataBinding.tvOrderLocation.text =
            address.split(",").firstOrNull()?.trim() ?: address
        mViewDataBinding.tvOrderNo.text = orderNumber
        mViewDataBinding.tvOrderDuration.text = serviceDuration
        mViewDataBinding.tvTotalPrice.text = servicePrice
        mViewDataBinding.tvServiceTitle.text = serviceName
        mViewDataBinding.tvStartDate.text =
            if (com.ksa.agence.app.AgenceApp.pref.getString(com.ksa.agence.common.LANG, "ar") == "ar")
                "14 يوليو 2026"
            else
                "14 Jul 2026"

        buildSteps()

        mViewDataBinding.ivBack.setOnClickListener {
            mViewDataBinding.root.findNavController().popBackStack()
        }

        mViewDataBinding.btnChat.setOnClickListener {
            // Navigate to conversation screen with mock data
            try {
                val bundle = Bundle().apply {
                    putInt("idCompany", 1)
                    putString("orderNO", orderNumber)
                    putString("CategoryName", serviceName)
                    putString("nameCompany", providerName)
                    putString("imageCompany", providerPhoto)
                    putInt("idOrder", 1)
                    putString("flag", "order_preview")
                }
                mViewDataBinding.root.findNavController().navigate(
                    R.id.conversationFragment, bundle
                )
            } catch (e: Exception) {
                Log.e("OrderPreview", "Chat navigation failed", e)
                Utilities.showToastError(requireActivity(), "تعذر فتح المحادثة")
            }
        }

        mViewDataBinding.btnAcceptOffer.setOnClickListener {
            // Open our new payment methods bottom sheet — pass the ACTUAL
            // company/agency and service info so the sheet matches this order
            // (not the hard-coded demo defaults).
            try {
                val bottomSheet = com.ksa.agence.ui.dialog.BottomSheetPaymentFragment()
                val bundle = Bundle().apply {
                    // اسم الشركة = وكالة المزود، مو اسمه الشخصي
                    putString("companyName", providerAgency)
                    putString("serviceName", serviceName)
                    // Price shown here is the total (5,750 in demo). Break it
                    // back into base + tax for the sheet's rows.
                    val cleanPrice = servicePrice.replace("[^0-9,\\.]".toRegex(), "").trim()
                    putString("amountBeforeTax", "5,000 ${getString(R.string.r_s)}")
                    putString("taxPercentage", "15")
                    putString("totalAmount", if (cleanPrice.isNotEmpty()) servicePrice else "5,750 ${getString(R.string.r_s)}")
                }
                bottomSheet.arguments = bundle
                bottomSheet.show(
                    parentFragmentManager,
                    com.ksa.agence.ui.dialog.BottomSheetPaymentFragment.TAG
                )
            } catch (e: Exception) {
                Log.e("OrderPreview", "Failed to open payment sheet", e)
                Utilities.showToastSuccess(requireActivity(), "تم قبول العرض بنجاح ✓")
            }
        }

        mViewDataBinding.btnReorder.setOnClickListener {
            mViewDataBinding.root.findNavController().popBackStack()
            Utilities.showToastSuccess(requireActivity(), "يمكنك إعادة اختيار الخدمة")
        }

        mViewDataBinding.btnEvaluation.setOnClickListener {
            // Navigate to rating fragment with provider info so the screen
            // shows the provider's face and name, not the user's.
            try {
                val bundle = Bundle().apply {
                    putInt("idOrder", 0)
                    putInt("idCompany", 0)
                    putString("providerName", providerName)
                    putString("providerAgency", providerAgency)
                    putString("providerPhoto", providerPhoto)
                }
                mViewDataBinding.root.findNavController().navigate(
                    R.id.ratingCompanyFragment, bundle
                )
            } catch (e: Exception) {
                Log.e("OrderPreview", "Failed to open rating", e)
                Utilities.showToastSuccess(requireActivity(), "شكراً لتقييمك ⭐")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            (requireActivity() as com.ksa.agence.ui.activity.MainActivity)
                .mViewDataBinding.btnQuickOrder.visibility = View.VISIBLE
        } catch (e: Exception) {
        }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    // Mock steps used when the flow is in demo mode. Language-aware so the
    // Arabic build gets Arabic, and English build gets English.
    private fun mockSteps(): List<String> {
        val isArabic = try {
            com.ksa.agence.app.AgenceApp.pref.getString(
                com.ksa.agence.common.LANG,
                "ar"
            ) == "ar"
        } catch (e: Exception) {
            true
        }
        return if (isArabic) {
            listOf(
                "جلسة تعارف واستكشاف احتياجاتك",
                "بحث وتحليل المنافسين في السوق",
                "عرض التصورات الأولية (اتجاهان)",
                "جولات المراجعة (حتى جولتين)",
                "التسليم النهائي ودليل الهوية PDF"
            )
        } else {
            listOf(
                "Discovery call & briefing session",
                "Research phase & competitive analysis",
                "Initial concepts presented (2 directions)",
                "Revision rounds (up to 2 rounds)",
                "Final deliverables & brand guidelines PDF"
            )
        }
    }

    private fun buildSteps() {
        // In demo mode we always show the English mock steps so محتويات الخدمة
        // reads as a real service outline rather than "step 1, step 2...".
        val steps = mockSteps()

        for ((index, step) in steps.withIndex()) {
            val row = LinearLayout(requireActivity())
            row.orientation = LinearLayout.HORIZONTAL
            val rowParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            if (index < steps.size - 1) rowParams.bottomMargin = dp(4)
            row.layoutParams = rowParams

            val circleColumn = LinearLayout(requireActivity())
            circleColumn.orientation = LinearLayout.VERTICAL
            circleColumn.gravity = Gravity.CENTER_HORIZONTAL
            circleColumn.layoutParams = LinearLayout.LayoutParams(
                dp(24),
                LinearLayout.LayoutParams.MATCH_PARENT
            )

            val circle = FrameLayout(requireActivity())
            val circleParams = LinearLayout.LayoutParams(dp(24), dp(24))
            circle.layoutParams = circleParams
            val bg = android.graphics.drawable.GradientDrawable()
            bg.shape = android.graphics.drawable.GradientDrawable.OVAL
            bg.setColor(resources.getColor(R.color.agence_blue))
            circle.background = bg
            val numberText = TextView(requireActivity())
            numberText.text = (index + 1).toString()
            numberText.setTextColor(resources.getColor(R.color.agence_orange))
            numberText.textSize = 9f
            numberText.typeface = androidx.core.content.res.ResourcesCompat.getFont(
                requireActivity(),
                R.font.somar_bold
            )
            val numberParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            numberParams.gravity = Gravity.CENTER
            numberText.layoutParams = numberParams
            circle.addView(numberText)
            circleColumn.addView(circle)

            if (index < steps.size - 1) {
                val line = View(requireActivity())
                val lineParams = LinearLayout.LayoutParams(dp(2), 0)
                lineParams.weight = 1f
                lineParams.topMargin = dp(3)
                line.layoutParams = lineParams
                line.setBackgroundColor(Color.parseColor("#14070606"))
                circleColumn.addView(line)
            }

            row.addView(circleColumn)

            val text = TextView(requireActivity())
            text.text = step
            text.setTextColor(resources.getColor(R.color.agence_black))
            text.textSize = 13f
            text.typeface = androidx.core.content.res.ResourcesCompat.getFont(
                requireActivity(),
                R.font.somar_regular
            )
            val textParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            textParams.weight = 1f
            textParams.marginStart = dp(10)
            textParams.bottomMargin = dp(16)
            text.layoutParams = textParams
            row.addView(text)

            mViewDataBinding.layoutSteps.addView(row)
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }
}