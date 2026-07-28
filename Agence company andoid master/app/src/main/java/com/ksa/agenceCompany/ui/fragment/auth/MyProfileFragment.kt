package com.ksa.agenceCompany.ui.fragment.auth

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.adapter.MultiSelectCategoriesAdapter
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.common.CODE200
import com.ksa.agenceCompany.common.CODE422
import com.ksa.agenceCompany.common.Resource
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.common.util.Utilities.Companion.convertFileToMultipart
import com.ksa.agenceCompany.common.util.Utilities.Companion.convertToRequestBody
import com.ksa.agenceCompany.databinding.FragmentMyProfileBinding
import com.ksa.agenceCompany.entity.categoriesResponse.DataCategoriesResponse
import com.ksa.agenceCompany.ui.activity.MainActivity
import com.ksa.agenceCompany.ui.activity.MapsActivity
import com.ksa.agenceCompany.viewModels.AuthenticationViewModel
import gun0912.tedimagepicker.builder.TedImagePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_my_profile
    private lateinit var nameUser: String
    private lateinit var description: String
    private lateinit var myAddress: String
    private lateinit var phone: String
    private val viewModel: AuthenticationViewModel by viewModel()
    private lateinit var mainActivity: MainActivity

    private var imageFile: File? = null


    lateinit var addressStr: String
    var lat :Double? =null
    var lon: Double? =null
    var request_code = 22

    private var isOpen: Boolean=false


    lateinit var multiSelectCategoriesAdapter: MultiSelectCategoriesAdapter
    lateinit var listData: ArrayList<DataCategoriesResponse>



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

        listData=ArrayList()
        viewModel.getCategoryNotToken()
        viewModel.categoriesNotTokenResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                listData.clear() // Clear the existing data
                                listData.addAll(it.data!!)
                                multiSelectCategoriesAdapter = MultiSelectCategoriesAdapter(requireActivity(), listData)
                                mViewDataBinding.rvSelectedCategories.adapter = multiSelectCategoriesAdapter
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
                                    it.data!!.company_logo,
                                    mViewDataBinding.ivUserLogin
                                )
                                Utilities.onLoadImageFromUrl(
                                    requireActivity(),
                                    it.data!!.company_background_image,
                                    mViewDataBinding.ivImage
                                )
                                mViewDataBinding.tvNameUser.text = it.data!!.title
                                mViewDataBinding.tvFullName.setText(it.data!!.title)
                                mViewDataBinding.tvAddress.setText(it.data!!.address)
                                mViewDataBinding.tvAboutYou.setText(it.data!!.description)
                                if (it.data!!.nationality_id!!.data !=null)
                                {
                                    mViewDataBinding.tvNationalityId.setText(it.data!!.nationality_id!!.data!!)

                                }

                                if (it.data!!.account_type == "company") {
                                    // عرض اللغة الحالية في رسالة توست
                                    mViewDataBinding.tvFullName.hint = getString(R.string.company)
                                    mViewDataBinding.tvNationalityId.visibility = View.GONE

                                    // لغات أخرى
                                    val drawable =
                                        ContextCompat.getDrawable(
                                            requireContext(),
                                            R.drawable.icon_company
                                        )
                                    mViewDataBinding.tvFullName.setCompoundDrawablesWithIntrinsicBounds(
                                        drawable,
                                        null,
                                        null,
                                        null
                                    )


                                } else if (it.data!!.account_type == "personal") {

                                    mViewDataBinding.tvFullName.hint = getString(R.string.full_name)
                                    mViewDataBinding.tvNationalityId.visibility = View.VISIBLE

                                    val drawable =
                                        ContextCompat.getDrawable(
                                            requireContext(),
                                            R.drawable.icon_person
                                        )
                                    mViewDataBinding.tvFullName.setCompoundDrawablesWithIntrinsicBounds(
                                        null,
                                        null,
                                        drawable,
                                        null
                                    )


                                }

                                for (i in it.data!!.categories!!.indices) {
                                    for (j in listData.indices) {
                                        if (it.data!!.categories!![i].id == listData[j].id) {
                                            listData[j].isSelected = true
                                        }
                                    }
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
                                mainActivity.navController!!.popBackStack()
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

        mViewDataBinding.tvAddress.setOnClickListener {
            startActivityForResult(
                Intent(
                    requireActivity(), MapsActivity::class.java
                ), request_code
            )
        }


        mViewDataBinding.constraintOpen.setOnClickListener {
            if ( isOpen==false)
            {
                isOpen=true
                mViewDataBinding.viewRv.visibility=View.VISIBLE
                mViewDataBinding.rvSelectedCategories.visibility=View.VISIBLE
                mViewDataBinding.ivOpen.setImageResource(R.drawable.icon_arraw_up)
            }
            else if ( isOpen==true)
            {
                isOpen=false
                mViewDataBinding.viewRv.visibility=View.GONE
                mViewDataBinding.rvSelectedCategories.visibility=View.GONE
                mViewDataBinding.ivOpen.setImageResource(R.drawable.icon_arrow_down)

            }


        }

        mViewDataBinding.tvChangePhoto.setOnClickListener {
            openGallery("imageProfile")
        }

        mViewDataBinding.titleImage.setOnClickListener {
            openGallery("company_background_image")

        }

        mViewDataBinding.btnSave.setOnClickListener {

            nameUser = mViewDataBinding.tvFullName.text.toString()
            description = mViewDataBinding.tvAboutYou.text.toString()
            myAddress = mViewDataBinding.tvAddress.text.toString()

            if (nameUser.isEmpty()) {
                mViewDataBinding.tvFullName.error = getString(R.string.this_item_is_required)
            }
            else if (myAddress.isEmpty()) {
                mViewDataBinding.tvAddress.error = getString(R.string.this_item_is_required)
            }
            else if (description.isEmpty()) {
                mViewDataBinding.tvAboutYou.error = getString(R.string.this_item_is_required)
            }
            else if (multiSelectCategoriesAdapter.selectedItems.size==0) {
                Toast.makeText(requireActivity(), getString(R.string.services), Toast.LENGTH_SHORT).show()
            } else {
                viewModel.userUpdateProfile(
                    convertToRequestBody(nameUser),
                    convertToRequestBody(description),
                    convertToRequestBody(myAddress),
                    multiSelectCategoriesAdapter.selectedItems,
                    convertFileToMultipart(imageFile, "company_logo"),
                    convertFileToMultipart(imageFile, "company_background_image"),
                )
            }


        }


    }


    private fun openGallery(type: String) {

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
                if (type=="company_background_image")
                {
                    Glide.with(this).load(uri).into(mViewDataBinding.ivImage)

                }
              else{
                    Glide.with(this).load(uri).into(mViewDataBinding.ivUserLogin)

                }
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                // Uri object will not be null for RESULT_OK
                val uri = data!!.data
                when (requestCode) {

                    request_code -> {
                        addressStr = data!!.getStringExtra("ADDRESS")!!
                        lat = data.getDoubleExtra("LAT", 0.0)
                        lon = data.getDoubleExtra("LON", 0.0)
                        mViewDataBinding.tvAddress.text = addressStr!!
                    }
                }
            }

            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            }

            else -> {
                //  Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    }



}