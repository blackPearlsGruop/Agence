package com.ksa.agence.ui.fragment.home

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import com.ksa.agence.R
import com.ksa.agence.base.BaseBottomDialog
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.LANG
import com.ksa.agence.common.Resource
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentQuickOrderBinding
import com.ksa.agence.entity.categoriesResponse.DataCategoriesResponse
import com.ksa.agence.ui.activity.MainActivity
import com.ksa.agence.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class QuickOrderFragment : BaseBottomDialog<FragmentQuickOrderBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_quick_order
    private lateinit var type: String
    private var idCompany: Int? = 0
    private var idOffer: Int? = 0
    private var idService: Int? = 0
    private lateinit var orderTitle: String
    private lateinit var order_duration_in_days: String
    private lateinit var orderDescription: String
    private var orderType: String = "quick"
    private var catigoryId: Int? = 0
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var mainActivity: MainActivity

    lateinit var listData: ArrayList<DataCategoriesResponse>

    private var selectedFileUri: android.net.Uri? = null
    private val isArabic get() = com.ksa.agence.app.AgenceApp.pref.getString(LANG, "ar") == "ar"

    private val pickFileLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedFileUri = uri
            var fileName = uri.lastPathSegment ?: "file"
            requireContext().contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (nameIndex >= 0 && cursor.moveToFirst()) fileName = cursor.getString(nameIndex)
            }
            mViewDataBinding.tvAttachLabel.text = fileName
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity

        listData = ArrayList()

        if (arguments != null) {

            try {

                val args: QuickOrderFragmentArgs = QuickOrderFragmentArgs.fromBundle(requireArguments())
                idOffer = args.idOffer ?: 0
                idService = args.idService ?: 0
                idCompany = args.idCompany ?: 0
                type = args.type

                //// service,offer,quick,private
                if (type == "service") {
                    orderType = "service"
                } else if (type == "offer") {
                    orderType = "offer"
                } else if (type == "private") {
                    orderType = "private"
                } else {
                    orderType = "quick"
                }

            } catch (e: Exception) {
            }

        }

        // Local mock category list shown immediately (and kept if the real
        // categories API call below never succeeds while the server is down).
        loadMockCategories()

        onClick()

    }

    private fun loadMockCategories() {
        listData.clear()
        val mockTitles = if (isArabic)
            listOf("استراتيجية العلامة والسوشيال", "الإعلانات المدفوعة والنمو", "الهوية البصرية والتصميم", "إنشاء المحتوى", "تحسين محركات البحث")
        else
            listOf("Brand Strategy & Social", "Paid Ads & Growth", "Visual Identity & Design", "Content Creation", "SEO & Growth")

        listData.addAll(mockTitles.mapIndexed { index, title ->
            DataCategoriesResponse(description = null, icon = null, id = index + 1, is_consultant = 0, title = title)
        })
    }

    private fun initResponse() {

        // resend response
        viewModel.getCategory()
        viewModel.categoriesResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                if (!it.data.isNullOrEmpty()) {
                                    listData.clear()
                                    listData.addAll(it.data)
                                }
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
                }

                is Resource.Loading -> {
                    showProgress(true)
                }
            }
        })


        viewModel.quickOrderResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {

                                showDialogSuccess(it.data!!.order_number!!)

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
                }

                is Resource.Loading -> {
                    showProgress(true)
                }
            }
        })


    }


    private fun onClick() {

        mViewDataBinding.ivClose.setOnClickListener {
            dismiss()
        }

        mViewDataBinding.btnServiceType.setOnClickListener { showServiceTypeMenu(it) }

        mViewDataBinding.btnAttach.setOnClickListener {
            pickFileLauncher.launch("*/*")
        }

        mViewDataBinding.btnSendToAll.setOnClickListener {

            orderTitle = mViewDataBinding.tvOrderAddress.text.toString()
            orderDescription = mViewDataBinding.tvOrderDetails.text.toString()
            order_duration_in_days = mViewDataBinding.tvDurationOfCompletion.text.toString()

            if (catigoryId == 0) {
                Utilities.showToastError(requireActivity(), getString(R.string.select_servic))

            } else if (orderTitle.isEmpty()) {
                mViewDataBinding.tvOrderAddress.error = getString(R.string.this_item_is_required)
            } else if (orderDescription.isEmpty()) {
                mViewDataBinding.tvOrderDetails.error = getString(R.string.this_item_is_required)
            } else if (order_duration_in_days.isEmpty()) {
                mViewDataBinding.tvDurationOfCompletion.error = getString(R.string.this_item_is_required)
            } else {
                // Budget is a UI-only field for now (no backend column for it
                // yet) — folded into the description so it isn't lost. Same
                // for the attached file: it's captured locally and shown to
                // the user, ready to wire up once the quick-order API accepts
                // an attachment field.
                val budgetText = mViewDataBinding.tvBudget.text.toString().trim()
                val budgetLine = if (budgetText.isNotEmpty())
                    "\n\n${getString(R.string.approx_budget)}: $budgetText ${getString(R.string.r_s)}"
                else ""
                val fullDescription = "$orderDescription$budgetLine"

                viewModel.quickOrder(
                    catigoryId!!,
                    if (idCompany != 0) idCompany else null,
                    if (idOffer != 0) idOffer else null,
                    if (idService != 0) idService else null,
                    orderType, orderTitle, fullDescription, order_duration_in_days
                )
            }

        }

    }

    private fun showServiceTypeMenu(anchor: View) {
        val popup = PopupMenu(requireActivity(), anchor)
        listData.forEachIndexed { index, category ->
            popup.menu.add(0, index, index, category.title)
        }
        popup.setOnMenuItemClickListener { item ->
            val category = listData[item.itemId]
            catigoryId = category.id
            mViewDataBinding.tvServiceType.text = category.title
            mViewDataBinding.tvServiceType.setTextColor(requireContext().getColor(R.color.agence_black))
            true
        }
        popup.show()
    }


    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            initResponse()
        }
    }

    fun showDialogSuccess(orderNumber: String) {
        val dialog = android.app.Dialog(requireActivity(), R.style.customDialogTheme)
        dialog.setCancelable(false)
        val inflater = requireActivity().layoutInflater
        val v: View = inflater.inflate(R.layout.dialog_success_order, null)
        dialog.setContentView(v)

        val ivClose = dialog.findViewById<android.widget.ImageView>(R.id.imageViewClose)
        val orderNo = dialog.findViewById<android.widget.TextView>(R.id.tv_order_number)

        orderNo.text = getString(R.string.order_no) + " " + orderNumber

        ivClose.setOnClickListener {
            dialog.dismiss()
            dismiss()
        }

        dialog.show()
    }

}
