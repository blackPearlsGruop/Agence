package com.ksa.agence.ui.fragment.setting

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.adapter.CopanyAdapter
import com.ksa.agence.adapter.FavouritesAdapter
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentAllCompanyBinding
import com.ksa.agence.databinding.FragmentFavouritesBinding
import com.ksa.agence.entity.companyResponse.DataCompanyResponse
import com.ksa.agence.entity.getAllFavouritesResponse.DataGetAllFavouritesResponse
import com.ksa.agence.interfaces.Company
import com.ksa.agence.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : BaseFragment<FragmentFavouritesBinding>(),Company {

    override fun getLayoutId(): Int = R.layout.fragment_favourites

    private var position: Int=0
    private val viewModel: HomeViewModel by viewModel()

    lateinit var favouritesAdapter: FavouritesAdapter
    lateinit var  listFavourites: ArrayList<DataGetAllFavouritesResponse>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listFavourites= ArrayList()



    }

    private fun initResponse() {

        // resend response
        viewModel.getAllFavourites()
        viewModel.getAllFavouritesResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {

                                listFavourites.addAll(it.data!!)
                                favouritesAdapter =
                                    FavouritesAdapter(requireActivity(), listFavourites,this)
                                mViewDataBinding.rvAllCompany.adapter = favouritesAdapter
                                favouritesAdapter.notifyDataSetChanged()

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

        viewModel.addFavouritesResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                listFavourites.removeAt(position)
                                favouritesAdapter.notifyDataSetChanged()

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

    override fun clickItemCompany(idCompany: Int,flag:String) {
        val action=FavouritesFragmentDirections.actionFavouritesFragmentToShowCompanyFragment(idCompany,"Favourites")
        mViewDataBinding.root.findNavController().navigate(action)

    }

    override fun clickItemAddCompanyFav(idCompany: Int, pos: Int) {
        position=pos
        viewModel.addFavourites(idCompany)
    }

    override fun clickItemShowService(idCompany: Int) {

    }

}