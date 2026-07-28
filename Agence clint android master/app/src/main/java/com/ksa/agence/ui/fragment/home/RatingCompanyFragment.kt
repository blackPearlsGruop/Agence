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
    private var id_order: Int=0
    private var id_company: Int=0
    private val viewModel: HomeViewModel by viewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null){

            val args:RatingCompanyFragmentArgs=RatingCompanyFragmentArgs.fromBundle(requireArguments())
            id_company=args.idCompany
            id_order=args.idOrder
        }


        if (AgenceApp.pref.authToken !=null)
        {
            Utilities.onLoadImageFromUrl(
                requireActivity(),
                AgenceApp.pref.loadUserData(requireActivity(), USER_DATA)!!.data!!.user!!.profile_image,
                mViewDataBinding.ivMage
            )

        }

        onClick()

    }

    private fun onClick() {

        mViewDataBinding.btnRating.setOnClickListener {

            val rating=mViewDataBinding.ratingBar.rating
            val review=mViewDataBinding.tvComment.text.toString()
            viewModel.ratingCompany(id_order,id_company,rating,review)

        }
    }

    private fun initResponse() {

        // resend response
        viewModel.ratingCompanyResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                Utilities.showToastSuccess(requireActivity(), it.message!!)
                                val action=RatingCompanyFragmentDirections.actionRatingCompanyFragmentToShowOrderFragment(id_order)
                                mViewDataBinding.root.findNavController().navigate(action)
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

}