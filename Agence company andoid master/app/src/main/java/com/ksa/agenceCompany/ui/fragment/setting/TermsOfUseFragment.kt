package com.ksa.agenceCompany.ui.fragment.setting

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.common.CODE200
import com.ksa.agenceCompany.common.CODE422
import com.ksa.agenceCompany.common.Resource
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.databinding.FragmentTermsOfUseBinding
import com.ksa.agenceCompany.ui.activity.MainActivity
import com.ksa.agenceCompany.viewModels.InfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TermsOfUseFragment : BaseFragment<FragmentTermsOfUseBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_terms_of_use
    private val viewModel: InfoViewModel by viewModel()


    private lateinit var mainActivity: MainActivity


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.terms_and_conditions)

    }


    private fun initResponse() {
        // resend response
        viewModel.info(2)
        viewModel.infoResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                              mViewDataBinding.tvDic.text=it.data!!.description
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

    override fun onDestroy() {
        super.onDestroy()
        mainActivity.showHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.setting)

    }


}