package com.ksa.agence.member.ui.fragment.home

import com.ksa.agence.common.sampleText
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ksa.agence.R
import com.ksa.agence.member.adapter.OpportunityHome
import com.ksa.agence.member.adapter.OpportunityHomeAdapter
import com.ksa.agence.member.base.BaseFragment
import com.ksa.agence.databinding.FragmentAllOpportunityBinding

class AllOpportunityFragment : BaseFragment<FragmentAllOpportunityBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_all_opportunity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOpportunities()

        mViewDataBinding.ivBack.setOnClickListener {
            mViewDataBinding.root.findNavController().popBackStack()
        }
    }

    private fun setupOpportunities() {
        val opportunities = listOf(
            OpportunityHome(
                sampleText("إعداد حملة إعلانات جوجل"), sampleText("صيدلية المدينة"), sampleText("ريال 150/hr"),
                listOf("PPC", "Google Ads"), sampleText("الموعد النهائي: 15 أغسطس 2026")
            ),
            OpportunityHome(
                sampleText("تصميم هوية العلامة التجارية"), sampleText("الفارس للتجزئة"), sampleText("8,500 ريال"),
                listOf("Branding", "Design"), sampleText("الموعد النهائي: 20 أغسطس 2026")
            ),
            OpportunityHome(
                sampleText("استراتيجية سوشيال الربع الثالث"), sampleText("تِك فيجن"), sampleText("6,000 ريال"),
                listOf("Content", "Social Media"), sampleText("الموعد النهائي: 30 أغسطس 2026")
            ),
            OpportunityHome(
                sampleText("إدارة متجر إلكتروني"), sampleText("نون ستور"), sampleText("4,500 ريال"),
                listOf("E-commerce", "Management"), sampleText("الموعد النهائي: 5 سبتمبر 2026")
            ),
            OpportunityHome(
                sampleText("كتابة محتوى تسويقي"), sampleText("مؤسسة الإبداع"), sampleText("ريال 90/hr"),
                listOf("Copywriting", "Content"), sampleText("الموعد النهائي: 12 سبتمبر 2026")
            ),
        )

        val adapter = OpportunityHomeAdapter(opportunities) {
            try {
                val action = AllOpportunityFragmentDirections
                    .actionAllOpportunityFragmentToOpportunityDetailsFragment(1, "ALL")
                mViewDataBinding.root.findNavController().navigate(action)
            } catch (e: Exception) {
                Log.e("AllOpportunity", "nav failed", e)
            }
        }

        mViewDataBinding.rvAnOpportunity.layoutManager = LinearLayoutManager(requireContext())
        mViewDataBinding.rvAnOpportunity.adapter = adapter
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }
}