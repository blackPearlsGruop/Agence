package com.ksa.agenceCompany.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.adapter.MultiSelectCategoriesAdapter
import com.ksa.agenceCompany.base.BaseBottomDialog
import com.ksa.agenceCompany.common.CODE200
import com.ksa.agenceCompany.common.CODE422
import com.ksa.agenceCompany.common.Resource
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.databinding.FragmentBottomSheetCategoriesBinding
import com.ksa.agenceCompany.entity.categoriesResponse.DataCategoriesResponse
import com.ksa.agenceCompany.ui.activity.AuthActivity
import com.ksa.agenceCompany.ui.activity.MainActivity
import com.ksa.agenceCompany.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class BottomSheetCategoriesFragment : BaseBottomDialog<FragmentBottomSheetCategoriesBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_bottom_sheet_categories
    private lateinit var resultIDS: List<DataCategoriesResponse>
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var mainActivity: AuthActivity


    lateinit var multiSelectCategoriesAdapter: MultiSelectCategoriesAdapter
    lateinit var listData: ArrayList<DataCategoriesResponse>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as AuthActivity


        listData = ArrayList()
        initResponse()
        onClick()
    }

    private fun initResponse() {
        homeViewModel.getCategory()
        homeViewModel.categoriesResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                Log.d("TestVerification", "Data received: ${it.data}")
                                listData.clear() // Clear the existing data
                                listData.addAll(it.data!!)
                                multiSelectCategoriesAdapter = MultiSelectCategoriesAdapter(requireActivity(), listData)
                                mViewDataBinding.rvCategories.adapter = multiSelectCategoriesAdapter
                                multiSelectCategoriesAdapter.notifyDataSetChanged()
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
                    showProgress(false)
                    Log.i("TestVerification", "Error: ${result.message}")
                }
                is Resource.Loading -> {
                    Log.i("TestVerification", "Loading...")
                    showProgress(true)
                }
            }
        })
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            initResponse()
        } else {
            // Optional: Handle offline case
        }
    }

    private fun onClick() {
        mViewDataBinding.btnSave.setOnClickListener {

            mainActivity.navController!!.previousBackStackEntry?.savedStateHandle?.set("key", resultIDS)
//            navController.popBackStack()
            mainActivity.navController!!.popBackStack()
        }

        }


}
