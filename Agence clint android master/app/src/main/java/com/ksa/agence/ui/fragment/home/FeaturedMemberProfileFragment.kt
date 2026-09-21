package com.ksa.agence.ui.fragment.home

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentFeaturedMemberProfileBinding

class FeaturedMemberProfileFragment : BaseFragment<FragmentFeaturedMemberProfileBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_featured_member_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        val name = args?.getString("name") ?: ""
        val specialty = args?.getString("specialty") ?: ""
        val location = args?.getString("location") ?: ""
        val bio = args?.getString("bio") ?: ""
        val photo = args?.getString("photo") ?: ""
        val rating = args?.getFloat("rating") ?: 0f
        val reviews = args?.getInt("reviews") ?: 0
        val matchPct = args?.getInt("matchPct") ?: 0
        val services = args?.getStringArrayList("services") ?: arrayListOf()

        Utilities.onLoadImageFromUrl(requireActivity(), photo, mViewDataBinding.ivHeroPhoto)
        mViewDataBinding.tvMemberName.text = name
        mViewDataBinding.tvMemberSpecialty.text = specialty
        mViewDataBinding.tvMemberLocation.text = location
        mViewDataBinding.tvMemberBio.text = bio
        mViewDataBinding.tvStatRating.text = "$rating ★"
        mViewDataBinding.tvStatReviews.text = "$reviews"
        mViewDataBinding.tvStatMatch.text = "$matchPct%"

        mViewDataBinding.layoutMemberServices.removeAllViews()
        for (service in services) {
            val row = layoutInflater.inflate(
                R.layout.item_member_service_check, mViewDataBinding.layoutMemberServices, false
            )
            row.findViewById<android.widget.TextView>(R.id.tv_service_name).text = service
            mViewDataBinding.layoutMemberServices.addView(row)
        }

        mViewDataBinding.ivBack.setOnClickListener {
            mViewDataBinding.root.findNavController().popBackStack()
        }

        mViewDataBinding.btnGetInTouch.setOnClickListener {
            mViewDataBinding.root.findNavController().popBackStack()
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }
}
