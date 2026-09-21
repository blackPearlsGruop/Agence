package com.ksa.agence.ui.fragment.home

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentPaidAdViewBinding

class PaidAdViewFragment : BaseFragment<FragmentPaidAdViewBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_paid_ad_view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            (requireActivity() as com.ksa.agence.ui.activity.MainActivity)
                .mViewDataBinding.constraintLayout2.visibility = View.GONE
        } catch (e: Exception) {
        }

        // Mock sponsor data matching the Figma reference exactly (this
        // banner has no real backend content yet).
        Utilities.onLoadImageFromUrl(
            requireActivity(),
            "https://images.unsplash.com/photo-1595680337986-ce4862b497b9?w=200&h=200&fit=crop&auto=format",
            mViewDataBinding.ivAdPhoto
        )
        mViewDataBinding.tvAdName.text = getString(R.string.najm_agency_name)
        mViewDataBinding.tvAdTagline.text = getString(R.string.najm_agency_tagline)
        mViewDataBinding.tvAdDescription.text = getString(R.string.najm_agency_description)
        mViewDataBinding.tvAdStat1Value.text = "★ 4.7"
        mViewDataBinding.tvAdStat2Value.text = "×4.2"
        mViewDataBinding.tvAdStat3Value.text = "+30M ${getString(R.string.r_s)}"

        mViewDataBinding.ivBack.setOnClickListener {
            mViewDataBinding.root.findNavController().popBackStack()
        }

        mViewDataBinding.btnViewFullProfile.setOnClickListener {
            val bundle = android.os.Bundle().apply {
                putInt("idCompany", -2) // Najm Agency mock id, same as the Home/Orders mocks
                putString("flage", "PaidAd")
            }
            mViewDataBinding.root.findNavController()
                .navigate(R.id.action_paidAdViewFragment_to_showCompanyFragment, bundle)
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }
}
