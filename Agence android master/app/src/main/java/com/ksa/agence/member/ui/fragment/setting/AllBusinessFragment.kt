package com.ksa.agence.member.ui.fragment.setting

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ksa.agence.R
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.databinding.FragmentAllBusinessBinding
import com.ksa.agence.member.adapter.AllWorksAdapter
import com.ksa.agence.member.base.BaseFragment
import com.ksa.agence.member.common.util.Utilities
import com.ksa.agence.member.entity.getCompanyWorksResponse.DataGetCompanyWorksResponse
import com.ksa.agence.member.interfaces.Home
import com.ksa.agence.member.ui.activity.MainActivity
import com.ksa.agence.member.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllBusinessFragment : BaseFragment<FragmentAllBusinessBinding>(), Home {

    override fun getLayoutId(): Int = R.layout.fragment_all_business

    private val viewModel: HomeViewModel by viewModel()
    private lateinit var mainActivity: MainActivity

    lateinit var allWorksAdapter: AllWorksAdapter
    private val listData = ArrayList<DataGetCompanyWorksResponse>()

    private var pos: Int = -1

    /** true only while a delete request sent from this screen is waiting for its result */
    private var deletePending = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()

        allWorksAdapter = AllWorksAdapter(requireActivity(), listData, this)
        mViewDataBinding.rvAllBusiness.adapter = allWorksAdapter
        updateSummary()

        onClick()
        observeResponses()
    }

    private fun onClick() {
        mViewDataBinding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        mViewDataBinding.btnAddBusiness.setOnClickListener {
            val action =
                AllBusinessFragmentDirections.actionAllBusinessFragmentToAddBusinessFragment2()
            findNavController().navigate(action)
        }
    }

    private fun updateSummary() {
        val count = listData.size
        mViewDataBinding.tvWorksCount.text =
            resources.getQuantityString(R.plurals.works_count, count, count)
        mViewDataBinding.layoutEmpty.visibility = if (count == 0) View.VISIBLE else View.GONE
        mViewDataBinding.rvAllBusiness.visibility = if (count == 0) View.GONE else View.VISIBLE
    }

    private fun observeResponses() {
        viewModel.getCompanyWorksResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let {
                        when (it.code) {
                            CODE200 -> {
                                listData.clear()
                                listData.addAll(it.data.orEmpty())
                                allWorksAdapter.notifyDataSetChanged()
                                updateSummary()
                            }

                            CODE422 -> Utilities.showToastError(requireActivity(), it.message!!)

                            else -> Utilities.showToastError(requireActivity(), it.message!!)
                        }
                    }
                }

                is Resource.Error -> {
                    showProgress(false)
                    Log.i("TestVerification", "error")
                }

                is Resource.Loading -> {
                    Log.i("TestVerification", "loading")
                    showProgress(true)
                }
            }
        })

        viewModel.deleteWorksResponse.observe(viewLifecycleOwner, Observer { result ->
            if (!deletePending) return@Observer
            when (result) {
                is Resource.Success -> {
                    deletePending = false
                    showProgress(false)
                    result.data?.let {
                        when (it.code) {
                            CODE200 -> {
                                if (pos in listData.indices) {
                                    listData.removeAt(pos)
                                    allWorksAdapter.notifyItemRemoved(pos)
                                    allWorksAdapter.notifyItemRangeChanged(pos, listData.size - pos)
                                }
                                updateSummary()
                                showDialogSuccess()
                            }

                            CODE422 -> Utilities.showToastError(requireActivity(), it.message!!)

                            else -> Utilities.showToastError(requireActivity(), it.message!!)
                        }
                    }
                }

                is Resource.Error -> {
                    deletePending = false
                    showProgress(false)
                    Log.i("TestVerification", "error")
                }

                is Resource.Loading -> {
                    Log.i("TestVerification", "loading")
                    showProgress(true)
                }
            }
        })
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            viewModel.getCompanyWorks()
        }
    }

    private fun confirmDelete(idWork: Int, position: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.works_delete_title)
            .setMessage(R.string.works_delete_body)
            .setPositiveButton(R.string.works_delete_confirm) { dialog, _ ->
                dialog.dismiss()
                pos = position
                deletePending = true
                viewModel.deleteWorks(idWork)
            }
            .setNegativeButton(R.string.works_cancel) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    fun showDialogSuccess() {
        val dialog = Dialog(requireActivity(), R.style.customDialogTheme)
        dialog.setCancelable(false)
        val v: View = requireActivity().layoutInflater.inflate(R.layout.member_dialog_success_order, null)
        dialog.setContentView(v)

        val ivClose = dialog.findViewById<ImageView>(R.id.imageViewClose)
        val orderTitle = dialog.findViewById<TextView>(R.id.textViewTitel)
        val orderNo = dialog.findViewById<TextView>(R.id.tv_order_number)

        orderTitle.text = getString(R.string.deleted)
        orderNo.visibility = View.GONE

        ivClose.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!::mainActivity.isInitialized) return
        val dest = mainActivity.navController?.currentDestination?.id
        if (dest != R.id.myProfileFragment
            && dest != R.id.allBusinessFragment
            && dest != R.id.addBusinessFragment2
        ) {
            mainActivity.showHomeToolbar()
        }
    }

    override fun clickItemShowService(idService: Int) {}

    override fun clickItemOpportunitiesDetails(idOpportunities: Int) {}

    override fun clickItemUpdateService(idService: Int) {}

    override fun clickItemDeleteService(idWork: Int, position: Int) {
        confirmDelete(idWork, position)
    }
}
