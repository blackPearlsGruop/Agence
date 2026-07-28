package com.ksa.agenceCompany.ui.fragment.setting

import android.app.Dialog
import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
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
import com.ksa.agenceCompany.viewModels.HomeViewModel
import gun0912.tedimagepicker.builder.TedImagePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class AddBusinessFragment : BaseFragment<FragmentAddBusinessBinding>(),Home {

    override fun getLayoutId(): Int = R.layout.fragment_add_business
    private val viewModel: HomeViewModel by viewModel()


    private lateinit var mainActivity: MainActivity

    lateinit var allWorksAdapter: AllWorksAdapter
    lateinit var listData: ArrayList<DataGetCompanyWorksResponse>
    private var imageFile: File? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.add_business)

        listData= ArrayList()

        onClick()

    }


    private fun onClick() {

        mViewDataBinding.ivImage.setOnClickListener {

            openGallery()

        }

        mViewDataBinding.btnSave.setOnClickListener {

            val title=mViewDataBinding.tvTitle.text.toString()
            val description=mViewDataBinding.tvDescription.text.toString()

            if (title.isEmpty())
            {
                mViewDataBinding.tvTitle.error=getString(R.string.this_item_is_required )
            }
            else     if (description.isEmpty())
            {
                mViewDataBinding.tvDescription.error=getString(R.string.this_item_is_required )
            }
            else{
                viewModel.addWorks(
                    Utilities.convertToRequestBody(title),
                    Utilities.convertToRequestBody(title),
                    Utilities.convertFileToMultipart(imageFile, "work_file"),)

            }


        }
    }


    private fun initResponse() {
        // resend response
        viewModel.addWorksResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {

                                showDialogSuccess()
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


    private fun openGallery() {

        Utilities.onPermission(requireActivity())
        TedImagePicker.with(requireContext()).image()
            // .imageAndVideo()
            // .video()
            .cameraTileImage(R.drawable.ic_defult_camera).showTitle(true).title(R.string.select)
            .backButton(R.drawable.icon_arrow_back).buttonText(R.string.done)
            .buttonBackground(R.drawable.shape_bottom)
            // dropDownAlbum()
            .start { uri ->
                (uri)
                imageFile = uri.toFile(requireContext().contentResolver)
                Glide.with(this).load(uri).into(mViewDataBinding.ivImage)

            }

    }


    fun Uri.toFile(contentResolver: ContentResolver): File? {
        val cursor = contentResolver.query(this, null, null, null, null)
        cursor?.use {
            it.moveToFirst()
            val filePathColumn = it.getColumnIndex(MediaStore.Images.Media.DATA)
            val filePath = it.getString(filePathColumn)
            return File(filePath)
        }
        return null
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

        orderTitle.text = getString(R.string.sent_successfully)
        orderNo.visibility = View.GONE

        ivClose.setOnClickListener {
            dialog.dismiss()
            mainActivity.navController!!.popBackStack()
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

    override fun clickItemDeleteService(idService: Int, position: Int) {
        TODO("Not yet implemented")
    }


}