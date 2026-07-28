package com.ksa.agenceCompany.ui.fragment.auth

import android.content.ContentResolver
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.adapter.DropDownCityAdapter
import com.ksa.agenceCompany.adapter.MultiSelectCategoriesAdapter
import com.ksa.agenceCompany.adapter.SelectedCategoriesAdapter
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.common.CODE200
import com.ksa.agenceCompany.common.CODE422
import com.ksa.agenceCompany.common.Resource
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.common.util.Utilities.Companion.convertFileToMultipart
import com.ksa.agenceCompany.common.util.Utilities.Companion.convertToRequestBody
import com.ksa.agenceCompany.databinding.FragmentRegisterBinding
import com.ksa.agenceCompany.entity.categoriesResponse.DataCategoriesResponse
import com.ksa.agenceCompany.entity.cityResponse.DataCityResponse
import com.ksa.agenceCompany.viewModels.AuthenticationViewModel
import com.ksa.agenceCompany.viewModels.HomeViewModel
import gun0912.tedimagepicker.builder.TedImagePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.Locale

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_register
    private var isOpen: Boolean=false
    private lateinit var nationalityId: String
    private var accept_terms_and_conditions: Int = 1
    private lateinit var accountType: String
    private var catigoryId: Int? = 0
    private var countryId: Int? = 1
    private var cityId: Int? = 0
    private lateinit var language: String
    private lateinit var nameUser: String
    private lateinit var phone: String
    private val viewModel: AuthenticationViewModel by viewModel()


    private var imageFile: File? = null


    lateinit var dropDownCityAdapter: DropDownCityAdapter
    lateinit var listCityData: ArrayList<DataCityResponse>


    lateinit var multiSelectCategoriesAdapter: MultiSelectCategoriesAdapter
    lateinit var listData: ArrayList<DataCategoriesResponse>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewDataBinding.titleIAgreeTo.paintFlags =
            mViewDataBinding.titleIAgreeTo.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        onClick()


        val currentLocale: Locale = resources.configuration.locales[0]
        language = currentLocale.language


//        try {
//
//            mViewDataBinding.root.findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<List<DataCategoriesResponse>>(
//                "key"
//            )
//                ?.observe(
//                    viewLifecycleOwner
//                ) { result ->
//                    //Write code here
//                    resultIDS = ArrayList()
//                    selectedCategories = ArrayList()
//
//                    selectedCategoriesAdapter =
//                        SelectedCategoriesAdapter(requireActivity(), selectedCategories)
//                    selectedCategories.addAll(result)
//                    mViewDataBinding.rvSelectedCategories.adapter = selectedCategoriesAdapter
//                    selectedCategoriesAdapter.notifyDataSetChanged()
//
//                    for (i in selectedCategories.indices) {
//                        resultIDS.add(selectedCategories[i].id!!)
//
//                        Log.d("onViewCreated: ", "" + resultIDS)
//                    }
//
//                }
//
//        }catch (e:Exception)
//        {}

    }


    private fun initResponse() {

        listCityData = ArrayList()


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


        listCityData.add(
            DataCityResponse(
                0, getString(R.string.select)
            )
        )

        // resend response
        viewModel.getCity()
        viewModel.cityResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {

                                listCityData.addAll(it.data!!)
                                dropDownCityAdapter =
                                    DropDownCityAdapter(requireActivity(), listCityData)
                                mViewDataBinding.spCity.adapter = dropDownCityAdapter
                                dropDownCityAdapter.notifyDataSetChanged()

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


        // resend response
        viewModel.registerResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {
                                Utilities.showToastSuccess(requireActivity(), it.message!!)
                                val action =
                                    RegisterFragmentDirections.actionRegisterFragmentToConfirmOtpFragment(
                                        phone, "+966"
                                    )
                                mViewDataBinding.root.findNavController().navigate(action)

                            }

                            CODE422 -> {
                                showProgress(false)
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

        mViewDataBinding.spCity?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    if (position != 0) {
                        cityId = listCityData.get(position).id
                    } else {

                    }
                }

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


        mViewDataBinding.radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->

            if (mViewDataBinding.rbCompany.isChecked) {
                accountType = "company"
                // عرض اللغة الحالية في رسالة توست
                mViewDataBinding.tvFullName.hint = getString(R.string.company)
                mViewDataBinding.titleImage.text = getString(R.string.commercial_registration_certificate)
                mViewDataBinding.tvNationalityId.visibility=View.GONE

                // تحقق من اللغة واتخاذ إجراء بناءً على ذلك
                if (language == "ar") {
                    // اللغة العربية
                    val drawable =
                        ContextCompat.getDrawable(requireContext(), R.drawable.icon_company)
                    mViewDataBinding.tvFullName.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        drawable,
                        null
                    )
                } else {
                    // لغات أخرى
                    val drawable =
                        ContextCompat.getDrawable(requireContext(), R.drawable.icon_company)
                    mViewDataBinding.tvFullName.setCompoundDrawablesWithIntrinsicBounds(
                        drawable,
                        null,
                        null,
                        null
                    )


                }


            }
            else if (mViewDataBinding.rbAnIndividual.isChecked) {

                accountType = "personal"

                mViewDataBinding.tvFullName.hint = getString(R.string.full_name)
                mViewDataBinding.titleImage.text = getString(R.string.self_employment_document)
                mViewDataBinding.tvNationalityId.visibility=View.VISIBLE
                // تحقق من اللغة واتخاذ إجراء بناءً على ذلك
                if (language == "ar") {
                    // اللغة العربية
                    val drawable =
                        ContextCompat.getDrawable(requireContext(), R.drawable.icon_person)
                    mViewDataBinding.tvFullName.setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        null,
                        drawable,
                        null
                    )
                } else {
                    // لغات أخرى
                    val drawable =
                        ContextCompat.getDrawable(requireContext(), R.drawable.icon_person)
                    mViewDataBinding.tvFullName.setCompoundDrawablesWithIntrinsicBounds(
                        drawable,
                        null,
                        null,
                        null
                    )


                }

            }


        }


        mViewDataBinding.ivImage.setOnClickListener {
            openGallery()
        }


        mViewDataBinding.btnRegisterNow.setOnClickListener {


            nameUser = mViewDataBinding.tvFullName.text.toString()
            phone = mViewDataBinding.tvMobileNumber.text.toString()
            nationalityId = mViewDataBinding.tvNationalityId.text.toString()


            if (nameUser.isEmpty()) {
                mViewDataBinding.tvFullName.error = getString(R.string.this_item_is_required)
            } else if (phone.isEmpty()) {
                mViewDataBinding.tvMobileNumber.error = getString(R.string.this_item_is_required)
            } else if (mViewDataBinding.checkBoxIAgreeTo.isChecked == false) {
                Utilities.showToastError(
                    requireActivity(),
                    getString(R.string.terms_and_conditions)
                )
                accept_terms_and_conditions = 1
            } else if ( multiSelectCategoriesAdapter.selectedItems.size==0) {
                Utilities.showToastError(
                    requireActivity(),
                    getString(R.string.services)
                )
            } else {
                phone = "+966" + phone

                Log.d("TestVerificationData",accountType+"\n"+
                        nameUser+"\n"+
                        phone+"\n"+
                        countryId+"\n"+
                        cityId+"\n"+
                        accept_terms_and_conditions+"\n"+
                        imageFile+"\n"+
                        multiSelectCategoriesAdapter.selectedItems+"\n"+
                        nationalityId)

                viewModel.userRegister(
                    convertToRequestBody(accountType),
                    convertToRequestBody(nameUser),
                    convertToRequestBody(phone),
                    convertToRequestBody(countryId.toString()),
                    convertToRequestBody(cityId.toString()),
                    convertToRequestBody(accept_terms_and_conditions.toString()),
                    convertFileToMultipart(imageFile, "commercial_licence"),
                    multiSelectCategoriesAdapter.selectedItems,
                    convertToRequestBody(nationalityId),
                )

            }


        }



        mViewDataBinding.tvSignIn.setOnClickListener {
            val action = RegisterFragmentDirections
                .actionRegisterFragmentToLoginFragment()
            mViewDataBinding.root.findNavController().navigate(action)
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


    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        // يتم استدعاء هذه الدالة عندما يتغير حالة الاتصال
        if (isConnected) {
            // يمكنك إجراء أي إجراءات إضافية هنا عند الاتصال بالإنترنت
            initResponse()
        } else {
        }

    }


}