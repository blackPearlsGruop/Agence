package com.ksa.agenceCompany.ui.fragment.setting

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.adapter.AllWorksAdapter
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.common.CODE200
import com.ksa.agenceCompany.common.CODE422
import com.ksa.agenceCompany.common.Resource
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.databinding.FragmentAddBusinessBinding
import com.ksa.agenceCompany.databinding.FragmentAllBusinessBinding
import com.ksa.agenceCompany.entity.getCompanyWorksResponse.DataGetCompanyWorksResponse
import com.ksa.agenceCompany.interfaces.Home
import com.ksa.agenceCompany.ui.activity.MainActivity
import com.ksa.agenceCompany.ui.fragment.home.SettingFragmentDirections
import com.ksa.agenceCompany.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllBusinessFragment : BaseFragment<FragmentAllBusinessBinding>(),Home {

    override fun getLayoutId(): Int = R.layout.fragment_all_business
    private var pos: Int=0
    private val viewModel: HomeViewModel by viewModel()


    private lateinit var mainActivity: MainActivity

    lateinit var allWorksAdapter: AllWorksAdapter
    lateinit var listData: ArrayList<DataGetCompanyWorksResponse>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.add_business)

        listData= ArrayList()



        onClick()


    }

    private fun onClick() {

        mViewDataBinding.btnAddBusiness.setOnClickListener {

            val action = AllBusinessFragmentDirections.actionAllBusinessFragmentToAddBusinessFragment2()
            mViewDataBinding.root.findNavController().navigate(action)

        }
    }


    private fun initResponse() {
        // resend response
        viewModel.getCompanyWorks()
        viewModel.getCompanyWorksResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    listData.clear()
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                listData.addAll(it.data!!)
                                allWorksAdapter=AllWorksAdapter(requireActivity(),listData,this)
                                mViewDataBinding.rvAllBusiness.adapter=allWorksAdapter
                                allWorksAdapter.notifyDataSetChanged()

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

        viewModel.deleteWorksResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                showDialogSuccess()
                                listData.removeAt(pos)
                                allWorksAdapter.notifyDataSetChanged()

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


    fun showDialogSuccess() {
        val dialog = Dialog(requireActivity(), R.style.customDialogTheme)
        dialog.setCancelable(false)
        val inflater = requireActivity().layoutInflater
        val v: View = inflater.inflate(R.layout.dialog_success_order, null)
        dialog.setContentView(v)

        val ivClose = dialog.findViewById<ImageView>(R.id.imageViewClose)
        val orderTitle = dialog.findViewById<TextView>(R.id.textViewTitel)
        val orderNo = dialog.findViewById<TextView>(R.id.tv_order_number)

        orderTitle.text = getString(R.string.deleted)
        orderNo.visibility = View.GONE

        ivClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }


    override fun onDestroy() {
        super.onDestroy()
        mainActivity.showHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.setting)

    }

    override fun clickItemShowService(idService: Int) {
        TODO("Not yet implemented")
    }

    override fun clickItemOpportunitiesDetails(idOpportunities: Int) {
        TODO("Not yet implemented")
    }

    override fun clickItemUpdateService(idService: Int) {
        TODO("Not yet implemented")
    }

    override fun clickItemDeleteService(idWork: Int, position: Int) {
        pos=position
        viewModel.deleteWorks(idWork)
    }


}