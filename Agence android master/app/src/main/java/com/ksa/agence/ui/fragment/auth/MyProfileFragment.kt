package com.ksa.agence.ui.fragment.auth

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.ksa.agence.R
import com.ksa.agence.app.AgenceApp
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
import java.util.Calendar
import java.util.Locale

class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_my_profile
    private val viewModel: AuthenticationViewModel by viewModel()
    private lateinit var mainActivity: MainActivity

    private var imageFile: File? = null

    companion object {
        // The server only stores name, phone and photo for now. These extra
        // fields are kept on the device until the API supports them.
        private const val KEY_USERNAME = "profile_username"
        private const val KEY_BIRTH_DATE = "profile_birth_date"
        private const val KEY_EMAIL = "profile_email"
        private const val KEY_FIELD = "profile_field"
        private const val KEY_BIO = "profile_bio"
        private const val BIO_MAX = 200
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        // Full screen page with its own header and sticky save button
        mainActivity.hideHomeToolbar()

        loadLocalFields()
        setupBioCounter()
        onClick()
    }

    private fun loadLocalFields() {
        val pref = AgenceApp.pref
        mViewDataBinding.etUsername.setText(pref.getString(KEY_USERNAME, ""))
        mViewDataBinding.etBirthDate.text = pref.getString(KEY_BIRTH_DATE, "")
        mViewDataBinding.etEmail.setText(pref.getString(KEY_EMAIL, ""))
        mViewDataBinding.etField.text = pref.getString(KEY_FIELD, "")
        mViewDataBinding.etBio.setText(pref.getString(KEY_BIO, ""))
        updateUsernameDisplay()
    }

    private fun saveLocalFields() {
        val pref = AgenceApp.pref
        pref.putString(KEY_USERNAME, mViewDataBinding.etUsername.text.toString().trim())
        pref.putString(KEY_BIRTH_DATE, mViewDataBinding.etBirthDate.text.toString())
        pref.putString(KEY_EMAIL, mViewDataBinding.etEmail.text.toString().trim())
        pref.putString(KEY_FIELD, mViewDataBinding.etField.text.toString())
        pref.putString(KEY_BIO, mViewDataBinding.etBio.text.toString().trim())
    }

    private fun updateUsernameDisplay() {
        val username = mViewDataBinding.etUsername.text.toString().trim()
        mViewDataBinding.tvUsernameDisplay.visibility = if (username.isEmpty()) View.GONE else View.VISIBLE
        mViewDataBinding.tvUsernameDisplay.text = "@$username"
    }

    private fun showName(name: String?) {
        val safeName = name.orEmpty()
        mViewDataBinding.tvNameUser.text = safeName
        mViewDataBinding.tvAvatarInitial.text = safeName.trim().firstOrNull()?.uppercase() ?: ""
    }

    private fun showPhoto(url: String?) {
        if (url.isNullOrBlank()) {
            mViewDataBinding.ivUserLogin.visibility = View.GONE
            mViewDataBinding.tvAvatarInitial.visibility = View.VISIBLE
        } else {
            mViewDataBinding.tvAvatarInitial.visibility = View.GONE
            mViewDataBinding.ivUserLogin.visibility = View.VISIBLE
            Utilities.onLoadImageFromUrl(requireActivity(), url, mViewDataBinding.ivUserLogin)
        }
    }

    private fun setupBioCounter() {
        val update = {
            mViewDataBinding.tvBioCounter.text = "${mViewDataBinding.etBio.text.length} / $BIO_MAX"
        }
        update()
        mViewDataBinding.etBio.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) = update()
        })
        mViewDataBinding.etUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) = updateUsernameDisplay()
        })
    }


    private fun initResponse() {
        viewModel.me()
        viewModel.meResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                val user = it.data!!
                                showPhoto(user.profile_image)
                                showName(user.name)
                                mViewDataBinding.tvFullName.setText(user.name)
                                // Field shows the local part only, +966 is fixed in the layout
                                mViewDataBinding.tvMobileNumber.setText(
                                    user.phone.orEmpty().removePrefix("+966").removePrefix("966")
                                )
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
                    Log.i("TestVerification", "error")
                }

                is Resource.Loading -> {
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


    fun onClick() {

        mViewDataBinding.btnBack.setOnClickListener {
            mViewDataBinding.root.findNavController().popBackStack()
        }

        mViewDataBinding.btnChangePhoto.setOnClickListener {
            openGallery()
        }

        mViewDataBinding.layoutBirthDate.setOnClickListener { pickBirthDate() }

        mViewDataBinding.layoutField.setOnClickListener { pickField() }

        mViewDataBinding.btnSave.setOnClickListener {
            val name = mViewDataBinding.tvFullName.text.toString().trim()
            val localPhone = mViewDataBinding.tvMobileNumber.text.toString().trim()
            val email = mViewDataBinding.etEmail.text.toString().trim()

            when {
                name.isEmpty() ->
                    mViewDataBinding.tvFullName.error = getString(R.string.this_item_is_required)

                localPhone.isEmpty() ->
                    mViewDataBinding.tvMobileNumber.error = getString(R.string.this_item_is_required)

                email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    mViewDataBinding.etEmail.error = getString(R.string.profile_invalid_email)

                else -> {
                    saveLocalFields()
                    showName(name)
                    viewModel.userUpdateProfile(
                        convertToRequestBody(name),
                        convertToRequestBody("+966$localPhone"),
                        convertFileToMultipart(imageFile, "profile_image"),
                        convertToRequestBody("PUT")
                    )
                }
            }
        }
    }

    private fun pickBirthDate() {
        val cal = Calendar.getInstance()
        // Start from the saved date if there is one (dd/MM/yyyy)
        mViewDataBinding.etBirthDate.text.toString().split("/").map { it.trim() }.let { parts ->
            if (parts.size == 3) {
                parts[0].toIntOrNull()?.let { cal.set(Calendar.DAY_OF_MONTH, it) }
                parts[1].toIntOrNull()?.let { cal.set(Calendar.MONTH, it - 1) }
                parts[2].toIntOrNull()?.let { cal.set(Calendar.YEAR, it) }
            } else {
                cal.add(Calendar.YEAR, -25)
            }
        }
        val dialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                mViewDataBinding.etBirthDate.text =
                    String.format(Locale.ENGLISH, "%02d/%02d/%04d", day, month + 1, year)
            },
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        )
        dialog.datePicker.maxDate = System.currentTimeMillis()
        dialog.show()
    }

    private fun pickField() {
        val fields = resources.getStringArray(R.array.profile_fields)
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.profile_choose_field)
            .setItems(fields) { _, which ->
                mViewDataBinding.etField.text = fields[which]
            }
            .show()
    }


    private fun openGallery() {
        Utilities.onPermission(requireActivity())
        TedImagePicker.with(requireContext()).image()
            .cameraTileImage(R.drawable.ic_defult_camera).showTitle(true).title(R.string.select)
            .backButton(R.drawable.icon_arrow_back).buttonText(R.string.done)
            .buttonBackground(R.drawable.shape_bottom)
            .start { uri ->
                imageFile = uri.toFile(requireContext().contentResolver)
                mViewDataBinding.tvAvatarInitial.visibility = View.GONE
                mViewDataBinding.ivUserLogin.visibility = View.VISIBLE
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
        if (isConnected) {
            initResponse()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mainActivity.showHomeToolbar()
    }
}
