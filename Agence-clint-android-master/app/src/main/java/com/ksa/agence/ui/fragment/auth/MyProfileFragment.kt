package com.ksa.agence.ui.fragment.auth

import android.content.ContentResolver
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.ksa.agence.R
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.common.util.Utilities.Companion.convertFileToMultipart
import com.ksa.agence.common.util.Utilities.Companion.convertToRequestBody
import com.ksa.agence.databinding.FragmentMyProfileBinding
import com.ksa.agence.ui.activity.MainActivity
import com.ksa.agence.viewModels.AuthenticationViewModel
import gun0912.tedimagepicker.builder.TedImagePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_my_profile
    private lateinit var nameUser: String
    private lateinit var phone: String
    private val viewModel: AuthenticationViewModel by viewModel()
    private lateinit var mainActivity: MainActivity

    private var imageFile: File? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.profile_personly)

        mViewDataBinding.tvChangePhoto.paintFlags =
            mViewDataBinding.tvChangePhoto.paintFlags or Paint.UNDERLINE_TEXT_FLAG


        onClick()
    }


    private fun initResponse() {
        // resend response
        viewModel.me()
        viewModel.meResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                Utilities.onLoadImageFromUrl(
                                    requireActivity(),
                                    it.data!!.profile_image,
                                    mViewDataBinding.ivUserLogin
                                )
                                mViewDataBinding.tvNameUser.text = it.data!!.name
                                mViewDataBinding.tvFullName.setText(it.data!!.name)
                                mViewDataBinding.tvMobileNumber.setText(it.data!!.phone)
                                phone=it.data!!.phone!!

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


        viewModel.userUpdateProfileResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                Utilities.showToastSuccess(requireActivity(), it.message!!)

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


    fun onClick() {


        mViewDataBinding.tvChangePhoto.setOnClickListener {
            openGallery()
        }

        mViewDataBinding.btnSave.setOnClickListener {

            nameUser =mViewDataBinding.tvFullName.text.toString()

            if (nameUser.isEmpty()) {
                mViewDataBinding.tvFullName.error = getString(R.string.this_item_is_required)
            } else {
                viewModel.userUpdateProfile(convertToRequestBody(nameUser), convertToRequestBody(phone),convertFileToMultipart(imageFile, "profile_image"),
                    convertToRequestBody("PUT") )
            }


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
                Glide.with(this).load(uri).into(mViewDataBinding.ivUserLogin)

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