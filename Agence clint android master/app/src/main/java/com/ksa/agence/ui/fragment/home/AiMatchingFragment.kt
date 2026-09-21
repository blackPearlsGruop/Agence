package com.ksa.agence.ui.fragment.home

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.adapter.MatchedProviderAdapter
import com.ksa.agence.app.AgenceApp
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.LANG
import com.ksa.agence.common.util.Utilities.Companion.onLoadImageFromUrl
import com.ksa.agence.databinding.FragmentAiMatchingBinding
import com.ksa.agence.entity.MatchedProvider

// Single screen, 3 internal steps (Requirements → Results → Confirm) — this
// merges what used to be two separate ideas in the reference design (an
// inline "AI Matching" flow AND a standalone "Auction" top-10→top-3 screen
// that had no way to reach it from anywhere). Only the inline flow is ever
// actually used, so we build that one, matching it exactly.
class AiMatchingFragment : BaseFragment<FragmentAiMatchingBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_ai_matching

    private var currentStep = 1
    private var selectedBudgetIndex = 1 // middle option pre-selected, matches reference
    private var selectedServiceIndex = 0
    private var selectedProvider: MatchedProvider? = null
    private val isArabic get() = AgenceApp.pref.getString(LANG, "ar") == "ar"

    // service type options: (English, Arabic) — matches the Figma reference list exactly
    private val serviceTypes = listOf(
        "Brand Strategy & Social" to "استراتيجية العلامة والسوشيال",
        "Digital Marketing" to "التسويق الرقمي",
        "Paid Ads (Google / Meta)" to "الإعلانات المدفوعة (جوجل / ميتا)",
        "Visual Identity & Design" to "الهوية البصرية والتصميم",
        "Content Creation" to "إنشاء المحتوى",
        "SEO & Growth" to "تحسين محركات البحث والنمو",
        "Video Production" to "إنتاج الفيديو",
        "Influencer Marketing" to "التسويق عبر المؤثرين"
    )

    // budget options: (English, Arabic)
    private val budgets = listOf(
        "< ﷼5,000" to "< ﷼5,000",
        "﷼5,000–15,000" to "﷼5,000–15,000",
        "> ﷼15,000" to "> ﷼15,000"
    )

    // Local mock candidate pool — same values as the reference design, sorted
    // by score descending. Swap for a real matching-API call once it exists;
    // same fallback pattern used across the rest of Home.
    private val matchedProviders = listOf(
        MatchedProvider(1, "Fahad Al-Otaibi", "﷼ 5,000", 4.9f, "2 wks", 96, "https://images.unsplash.com/photo-1616147147027-60d49d3582c4?w=80&h=80&fit=crop&auto=format"),
        MatchedProvider(2, "Najm Agency", "﷼ 4,200", 4.7f, "10 days", 91, "https://images.unsplash.com/photo-1595680337986-ce4862b497b9?w=80&h=80&fit=crop&auto=format"),
        MatchedProvider(3, "Athar Studio", "﷼ 4,100", 5.0f, "3 wks", 88, "https://images.unsplash.com/photo-1690166444476-8cc4c0f24032?w=80&h=80&fit=crop&auto=format"),
        MatchedProvider(4, "Reem Media", "﷼ 3,800", 4.5f, "2 wks", 84, "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=80&h=80&fit=crop&auto=format"),
        MatchedProvider(5, "Sky Brand Co.", "﷼ 5,500", 4.6f, "3 wks", 81, "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=80&h=80&fit=crop&auto=format"),
        MatchedProvider(6, "Pulse Ads", "﷼ 4,700", 4.4f, "12 days", 78, "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=80&h=80&fit=crop&auto=format"),
        MatchedProvider(7, "Craft Lab", "﷼ 3,500", 4.3f, "2 wks", 74, "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=80&h=80&fit=crop&auto=format"),
        MatchedProvider(8, "Bold Mark", "﷼ 6,000", 4.8f, "4 wks", 70, "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=80&h=80&fit=crop&auto=format"),
        MatchedProvider(9, "Reach Digital", "﷼ 4,000", 4.2f, "2 wks", 66, "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=80&h=80&fit=crop&auto=format"),
        MatchedProvider(10, "Nova Creative", "﷼ 3,200", 4.1f, "10 days", 62, "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=80&h=80&fit=crop&auto=format")
    )

    override fun onResume() {
        super.onResume()
        try {
            (requireActivity() as com.ksa.agence.ui.activity.MainActivity)
                .mViewDataBinding.constraintLayout2.visibility = View.GONE
        } catch (e: Exception) {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // This screen has its own header (back + title). Hide the old shared
        // toolbar explicitly instead of assuming it's already hidden — it can
        // come back visible depending on which screen was open before this one.
        try {
            (requireActivity() as com.ksa.agence.ui.activity.MainActivity)
                .mViewDataBinding.constraintLayout2.visibility = View.GONE
        } catch (e: Exception) {
        }

        updateServiceTypeText()
        updateBudgetSelectionUi()
        goToStep(1)

        mViewDataBinding.ivBack.setOnClickListener {
            if (currentStep > 1) goToStep(currentStep - 1)
            else mViewDataBinding.root.findNavController().popBackStack()
        }

        mViewDataBinding.btnServiceType.setOnClickListener { showServiceTypeMenu(it) }

        mViewDataBinding.budgetOption1.setOnClickListener { selectBudget(0) }
        mViewDataBinding.budgetOption2.setOnClickListener { selectBudget(1) }
        mViewDataBinding.budgetOption3.setOnClickListener { selectBudget(2) }

        mViewDataBinding.btnFindMatch.setOnClickListener { goToStep(2) }

        val adapter = MatchedProviderAdapter(requireActivity(), matchedProviders) { provider ->
            selectedProvider = provider
            bindConfirmCard(provider)
            goToStep(3)
        }
        mViewDataBinding.rvMatchedProviders.adapter = adapter

        mViewDataBinding.tvBackToResults.setOnClickListener { goToStep(2) }

        mViewDataBinding.btnStartProject.setOnClickListener {
            val provider = selectedProvider ?: return@setOnClickListener
            // Mock candidates use local ids 1..10 — map to a real company id once
            // the matching API exists. For now route to provider #1 like the
            // rest of the app's mock-first screens.
            val idCompany = if (provider.id <= 3) provider.id else 1
            val action = AiMatchingFragmentDirections.actionAiMatchingFragmentToShowCompanyFragment(idCompany, "AiMatching")
            mViewDataBinding.root.findNavController().navigate(action)
        }
    }

    private fun showServiceTypeMenu(anchor: View) {
        val popup = PopupMenu(requireActivity(), anchor)
        serviceTypes.forEachIndexed { index, (en, ar) ->
            popup.menu.add(0, index, index, if (isArabic) ar else en)
        }
        popup.setOnMenuItemClickListener { item ->
            selectedServiceIndex = item.itemId
            updateServiceTypeText()
            true
        }
        popup.show()
    }

    private fun updateServiceTypeText() {
        val (en, ar) = serviceTypes[selectedServiceIndex]
        mViewDataBinding.tvServiceType.text = if (isArabic) ar else en
    }

    private fun selectBudget(index: Int) {
        selectedBudgetIndex = index
        updateBudgetSelectionUi()
    }

    private fun updateBudgetSelectionUi() {
        val options = listOf(
            Triple(mViewDataBinding.budgetOption1, mViewDataBinding.tvBudget1, mViewDataBinding.dotBudget1),
            Triple(mViewDataBinding.budgetOption2, mViewDataBinding.tvBudget2, mViewDataBinding.dotBudget2),
            Triple(mViewDataBinding.budgetOption3, mViewDataBinding.tvBudget3, mViewDataBinding.dotBudget3)
        )
        options.forEachIndexed { index, (container, label, dot) ->
            val (en, ar) = budgets[index]
            label.text = if (isArabic) ar else en
            if (index == selectedBudgetIndex) {
                container.setBackgroundResource(R.drawable.bg_budget_option_selected)
                label.setTextColor(requireContext().getColor(R.color.agence_blue))
                dot.visibility = View.VISIBLE
            } else {
                container.setBackgroundResource(R.drawable.bg_budget_option_default)
                label.setTextColor(requireContext().getColor(R.color.agence_black))
                dot.visibility = View.INVISIBLE
            }
        }
    }

    private fun bindConfirmCard(provider: MatchedProvider) {
        onLoadImageFromUrl(requireActivity(), provider.photoUrl, mViewDataBinding.ivConfirmPhoto)
        mViewDataBinding.tvConfirmName.text = provider.name
        mViewDataBinding.ratingBarConfirm.rating = provider.rating
        val deliverySuffix = getString(R.string.delivery_suffix)
        mViewDataBinding.tvConfirmDeliveryInline.text = "${provider.delivery} $deliverySuffix"
        mViewDataBinding.tvConfirmPrice.text = provider.price
        mViewDataBinding.tvConfirmDelivery.text = provider.delivery
        mViewDataBinding.tvConfirmScore.text = "${provider.score}%"
        mViewDataBinding.tvBestMatchBadge.visibility = if (provider.score > 89) View.VISIBLE else View.GONE
    }

    private fun goToStep(step: Int) {
        currentStep = step

        mViewDataBinding.layoutStep1.visibility = if (step == 1) View.VISIBLE else View.GONE
        mViewDataBinding.layoutStep2.visibility = if (step == 2) View.VISIBLE else View.GONE
        mViewDataBinding.layoutStep3.visibility = if (step == 3) View.VISIBLE else View.GONE

        updateStepDot(mViewDataBinding.tvStep1Dot, mViewDataBinding.tvStep1Label, 1, step)
        updateStepDot(mViewDataBinding.tvStep2Dot, mViewDataBinding.tvStep2Label, 2, step)
        updateStepDot(mViewDataBinding.tvStep3Dot, mViewDataBinding.tvStep3Label, 3, step)

        mViewDataBinding.line12.setBackgroundColor(
            requireContext().getColor(if (step > 1) R.color.agence_blue else R.color.agence_muted_bg)
        )
        mViewDataBinding.line23.setBackgroundColor(
            requireContext().getColor(if (step > 2) R.color.agence_blue else R.color.agence_muted_bg)
        )
    }

    private fun updateStepDot(dot: android.widget.TextView, label: android.widget.TextView, dotStep: Int, currentStep: Int) {
        when {
            currentStep > dotStep -> {
                dot.setBackgroundResource(R.drawable.bg_step_dot_active)
                dot.text = "✓"
                dot.setTextColor(requireContext().getColor(R.color.white))
                label.setTextColor(requireContext().getColor(R.color.agence_blue))
            }
            currentStep == dotStep -> {
                dot.setBackgroundResource(R.drawable.bg_step_dot_active)
                dot.text = dotStep.toString()
                dot.setTextColor(requireContext().getColor(R.color.white))
                label.setTextColor(requireContext().getColor(R.color.agence_blue))
            }
            else -> {
                dot.setBackgroundResource(R.drawable.bg_step_dot_inactive)
                dot.text = dotStep.toString()
                dot.setTextColor(requireContext().getColor(R.color.agence_muted))
                label.setTextColor(requireContext().getColor(R.color.agence_muted))
            }
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }
}
