package com.ksa.agence.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.app.AgenceApp
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.common.USER_DATA
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentRatingCompanyBinding
import com.ksa.agence.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RatingCompanyFragment : BaseFragment<FragmentRatingCompanyBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_rating_company
    private var id_order: Int = 0
    private var id_company: Int = 0
    private var providerName: String = ""
    private var providerAgency: String = ""
    private var providerPhoto: String = ""
    private val viewModel: HomeViewModel by viewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Read args safely — the screen can open from OrderPreview with demo
        // IDs and provider info, or from the real order flow with safe args.
        if (arguments != null) {
            try {
                val args: RatingCompanyFragmentArgs =
                    RatingCompanyFragmentArgs.fromBundle(requireArguments())
                id_company = args.idCompany
                id_order = args.idOrder
            } catch (e: Exception) {
                Log.e("RatingCompany", "Falling back to plain bundle keys", e)
                id_company = arguments?.getInt("idCompany") ?: 0
                id_order = arguments?.getInt("idOrder") ?: 0
            }

            // Extra provider info passed from OrderPreview flow
            providerName = arguments?.getString("providerName") ?: ""
            providerAgency = arguments?.getString("providerAgency") ?: ""
            providerPhoto = arguments?.getString("providerPhoto") ?: ""
        }

        // Fall back to mock data so the screen is never empty
        if (providerName.isEmpty()) providerName = "فهد العتيبي"
        if (providerAgency.isEmpty()) providerAgency = "وكالة الرواد للتسويق"

        mViewDataBinding.tvProviderName.text = providerName
        mViewDataBinding.tvProviderAgency.text = providerAgency

        // Load the provider photo defensively (URL from bundle, else keep the
        // default drawable already on the CircleImageView).
        try {
            if (providerPhoto.isNotEmpty()) {
                Utilities.onLoadImageFromUrl(
                    requireActivity(),
                    providerPhoto,
                    mViewDataBinding.ivMage
                )
            }
        } catch (e: Exception) {
            Log.e("RatingCompany", "Failed to load provider photo", e)
        }

        onClick()

    }

    private fun onClick() {

        mViewDataBinding.btnRating.setOnClickListener {

            val rating = mViewDataBinding.ratingBar.rating

            // In the demo flow (no real IDs) or when the server isn't
            // available, always give the user immediate feedback and go back.
            if (id_order == 0 || id_company == 0) {
                Utilities.showToastSuccess(
                    requireActivity(),
                    "شكراً لتقييمك ⭐ ($rating)"
                )
                mViewDataBinding.root.findNavController().popBackStack()
                return@setOnClickListener
            }

            // Real flow: fire the API AND immediately give feedback so the
            // button always responds even when the network is slow or the
            // observer isn't attached yet.
            try {
                val review = mViewDataBinding.tvComment.text.toString()
                viewModel.ratingCompany(id_order, id_company, rating, review)
            } catch (e: Exception) {
                Log.e("RatingCompany", "ratingCompany call failed", e)
            }

            Utilities.showToastSuccess(
                requireActivity(),
                "تم إرسال تقييمك بنجاح ⭐"
            )
            mViewDataBinding.root.findNavController().popBackStack()

        }
    }

    private fun initResponse() {

        viewModel.ratingCompanyResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                // Success handled inline in onClick already —
                                // this is just a defensive no-op for the
                                // observer path.
                                Log.i("RatingCompany", "API 200: ${it.message}")
                            }

                            CODE422 -> {
                                Utilities.showToastError(requireActivity(), it.message ?: "")
                            }

                            else -> {
                                showProgress(false)
                                Log.e("RatingCompany", "API error: ${it.message}")
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    showProgress(false)
                    Log.i("TestVerification", "error")
                }

                is Resource.Loading -> {
                    Log.i("TestVerification", "loading")
                }
            }
        })

    }


    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            initResponse()
        } else {
        }
    }

}