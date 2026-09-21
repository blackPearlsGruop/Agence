package com.ksa.agence.ui.fragment.home

import android.graphics.Color
import android.os.Bundle
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

// Screen shown when tapping "Get Offer" on a service in a provider's profile
// (matches the Figma "Order" screen exactly). Payment/escrow/dispute is the
// next screen in this flow and is being built separately — Accept Offer is
// wired to a short placeholder for now.
class OrderPreviewFragment : BaseFragment<FragmentOrderPreviewBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_order_preview

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            (requireActivity() as com.ksa.agence.ui.activity.MainActivity)
                .mViewDataBinding.constraintLayout2.visibility = View.GONE
        } catch (e: Exception) {
        }

        val args = arguments
        val providerName = args?.getString("providerName") ?: ""
        val providerAgency = args?.getString("providerAgency") ?: ""
        val providerPhoto = args?.getString("providerPhoto") ?: ""
        val providerRating = args?.getFloat("providerRating") ?: 0f
        val address = args?.getString("address") ?: ""
        val serviceName = args?.getString("serviceName") ?: ""
        val servicePrice = args?.getString("servicePrice") ?: ""
        val serviceDuration = args?.getString("serviceDuration") ?: ""
        val orderNumber = args?.getString("orderNumber") ?: ""

        Utilities.onLoadImageFromUrl(requireActivity(), providerPhoto, mViewDataBinding.ivProviderPhoto)
        mViewDataBinding.tvProviderName.text = providerName
        mViewDataBinding.tvProviderAgency.text = providerAgency
        mViewDataBinding.tvProviderRating.text = providerRating.toString()
        mViewDataBinding.tvOrderLocation.text = address.split(",").firstOrNull()?.trim() ?: address
        mViewDataBinding.tvOrderNo.text = orderNumber
        mViewDataBinding.tvOrderDuration.text = serviceDuration
        mViewDataBinding.tvTotalPrice.text = servicePrice
        mViewDataBinding.tvServiceTitle.text = serviceName
        mViewDataBinding.tvStartDate.text = if (com.ksa.agence.app.AgenceApp.pref.getString(com.ksa.agence.common.LANG, "ar") == "ar") "14 يوليو 2026" else "14 Jul 2026"

        buildSteps()

        mViewDataBinding.ivBack.setOnClickListener {
            mViewDataBinding.root.findNavController().popBackStack()
        }

        mViewDataBinding.btnChat.setOnClickListener {
            mViewDataBinding.root.findNavController().popBackStack()
        }

        mViewDataBinding.btnAcceptOffer.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }

        mViewDataBinding.btnReorder.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }
        mViewDataBinding.btnEvaluation.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private fun buildSteps() {
        val steps = listOf(
            getString(R.string.step_1), getString(R.string.step_2), getString(R.string.step_3),
            getString(R.string.step_4), getString(R.string.step_5)
        )

        for ((index, step) in steps.withIndex()) {
            val row = LinearLayout(requireActivity())
            row.orientation = LinearLayout.HORIZONTAL
            val rowParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
            if (index < steps.size - 1) rowParams.bottomMargin = dp(4)
            row.layoutParams = rowParams

            val circleColumn = LinearLayout(requireActivity())
            circleColumn.orientation = LinearLayout.VERTICAL
            circleColumn.gravity = Gravity.CENTER_HORIZONTAL
            circleColumn.layoutParams = LinearLayout.LayoutParams(dp(24), LinearLayout.LayoutParams.MATCH_PARENT)

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
            numberText.typeface = androidx.core.content.res.ResourcesCompat.getFont(requireActivity(), R.font.somar_bold)
            val numberParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
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
            text.typeface = androidx.core.content.res.ResourcesCompat.getFont(requireActivity(), R.font.somar_regular)
            val textParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
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
