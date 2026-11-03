package com.ksa.agence.member.ui.fragment.auth

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.ksa.agence.R
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.databinding.MemberFragmentMyProfileBinding
import com.ksa.agence.member.adapter.ProfileWorksAdapter
import com.ksa.agence.member.base.BaseFragment
import com.ksa.agence.member.common.util.Utilities
import com.ksa.agence.member.common.util.Utilities.Companion.convertFileToMultipart
import com.ksa.agence.member.common.util.Utilities.Companion.convertToRequestBody
import com.ksa.agence.member.entity.categoriesResponse.DataCategoriesResponse
import com.ksa.agence.member.ui.activity.MainActivity
import com.ksa.agence.member.ui.activity.MapsActivity
import com.ksa.agence.member.viewModels.AuthenticationViewModel
import com.ksa.agence.member.viewModels.HomeViewModel
import gun0912.tedimagepicker.builder.TedImagePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.Locale

class MyProfileFragment : BaseFragment<MemberFragmentMyProfileBinding>() {

    override fun getLayoutId(): Int = R.layout.member_fragment_my_profile
    private val viewModel: AuthenticationViewModel by viewModel()
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var mainActivity: MainActivity

    // Logo and cover are separate uploads
    private var logoFile: File? = null
    private var coverFile: File? = null

    private val requestCodeMap = 22
    private val descriptionMax = 300

    // All categories from the server, and the ids this provider offers
    private val allCategories = ArrayList<DataCategoriesResponse>()
    private val selectedCategoryIds = LinkedHashSet<Int>()
    // Names from the profile, used until the full category list arrives
    private val profileCategoryNames = LinkedHashMap<Int, String>()

    private var savedConsultPrice: Int? = null

    private lateinit var worksAdapter: ProfileWorksAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        // Full screen page with its own header and sticky save button
        mainActivity.hideHomeToolbar()

        setupWorks()
        setupDescriptionCounter()
        renderServiceChips()
        onClick()
    }

    private fun setupWorks() {
        worksAdapter = ProfileWorksAdapter(
            requireActivity(),
            onWorkClick = { openAllWorks() },
            onAddClick = {
                mViewDataBinding.root.findNavController().navigate(R.id.addBusinessFragment2)
            }
        )
        mViewDataBinding.rvWorks.adapter = worksAdapter
        worksAdapter.submit(emptyList())
        mViewDataBinding.tvWorksCount.text = getString(R.string.profile_works_count, 0)
    }

    private fun openAllWorks() {
        mViewDataBinding.root.findNavController().navigate(R.id.allBusinessFragment)
    }

    private fun setupDescriptionCounter() {
        val update = {
            mViewDataBinding.tvBioCounter.text =
                "${mViewDataBinding.tvAboutYou.text.length} / $descriptionMax"
        }
        update()
        mViewDataBinding.tvAboutYou.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) = update()
        })
    }

    // ---------------------------------------------------------------- display helpers

    private fun showName(name: String?) {
        val safeName = name.orEmpty()
        mViewDataBinding.tvNameUser.text = safeName
        mViewDataBinding.tvLogoInitial.text = safeName.trim().firstOrNull()?.uppercase() ?: ""
    }

    private fun showLogo(url: String?) {
        if (url.isNullOrBlank()) {
            mViewDataBinding.ivLogo.visibility = View.GONE
            mViewDataBinding.tvLogoInitial.visibility = View.VISIBLE
        } else {
            mViewDataBinding.tvLogoInitial.visibility = View.GONE
            mViewDataBinding.ivLogo.visibility = View.VISIBLE
            Utilities.onLoadImageFromUrl(requireActivity(), url, mViewDataBinding.ivLogo)
        }
    }

    private fun showCover(url: String?) {
        if (!url.isNullOrBlank()) {
            Utilities.onLoadImageFromUrl(requireActivity(), url, mViewDataBinding.ivCover)
        }
    }

    /** Account type is shown only; it is set when the provider registers. */
    private fun showAccountType(type: String?) {
        val isCompany = type == "company"
        val isPersonal = type == "personal"

        styleSegment(mViewDataBinding.tvTypeCompany, isCompany)
        styleSegment(mViewDataBinding.tvTypeIndividual, isPersonal)

        mViewDataBinding.tvAccountTypeChip.visibility = if (isCompany || isPersonal) View.VISIBLE else View.GONE
        mViewDataBinding.tvAccountTypeChip.text = getString(
            if (isCompany) R.string.profile_type_company else R.string.profile_type_individual
        )

        mViewDataBinding.tvNameLabel.text = getString(
            if (isPersonal) R.string.profile_full_name else R.string.profile_org_name
        )
        mViewDataBinding.ivNameIcon.setImageResource(
            if (isPersonal) R.drawable.ic_profile_user else R.drawable.ic_profile_building
        )
        mViewDataBinding.layoutNationality.visibility = if (isPersonal) View.VISIBLE else View.GONE
    }

    private fun styleSegment(view: android.widget.TextView, selected: Boolean) {
        if (selected) {
            view.setBackgroundResource(R.drawable.bg_profile_segment_on)
            view.setTextColor(resources.getColor(R.color.primary, null))
            view.elevation = 2f
        } else {
            view.background = null
            view.setTextColor(Color.parseColor("#5F5F5F"))
            view.elevation = 0f
        }
    }

    private fun showRating(avg: Int?, count: Int?) {
        val ratingCount = count ?: 0
        if (ratingCount == 0) {
            mViewDataBinding.layoutRating.visibility = View.GONE
            return
        }
        mViewDataBinding.layoutRating.visibility = View.VISIBLE
        mViewDataBinding.tvRating.text = String.format(Locale.ENGLISH, "%.1f", (avg ?: 0).toDouble())
        mViewDataBinding.tvRatingCount.text = getString(R.string.profile_rating_count, ratingCount)
    }

    // ---------------------------------------------------------------- services

    private fun categoryName(id: Int): String =
        allCategories.firstOrNull { it.id == id }?.title ?: profileCategoryNames[id].orEmpty()

    private fun renderServiceChips() {
        val group = mViewDataBinding.chipGroupServices
        group.removeAllViews()
        val font = ResourcesCompat.getFont(requireContext(), R.font.somar_bold)

        for (id in selectedCategoryIds) {
            val name = categoryName(id)
            if (name.isBlank()) continue
            group.addView(Chip(requireContext()).apply {
                text = name
                typeface = font
                setTextColor(resources.getColor(R.color.primary, null))
                chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#EAE6FF"))
                chipStrokeWidth = 0f
                isClickable = false
                isCheckable = false
            })
        }

        group.addView(Chip(requireContext()).apply {
            text = getString(R.string.profile_add_service)
            typeface = font
            setTextColor(Color.parseColor("#5F5F5F"))
            chipBackgroundColor = ColorStateList.valueOf(Color.WHITE)
            chipStrokeColor = ColorStateList.valueOf(Color.parseColor("#CFC8C8"))
            chipStrokeWidth = 3f
            setOnClickListener { pickServices() }
        })
    }

    private fun pickServices() {
        if (allCategories.isEmpty()) {
            viewModel.getCategoryNotToken()
            return
        }
        val titles = allCategories.map { it.title.orEmpty() }.toTypedArray()
        val checked = allCategories.map { it.id in selectedCategoryIds }.toBooleanArray()

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.profile_choose_services)
            .setMultiChoiceItems(titles, checked) { _, which, isChecked -> checked[which] = isChecked }
            .setPositiveButton(R.string.profile_done) { _, _ ->
                selectedCategoryIds.clear()
                allCategories.forEachIndexed { i, category ->
                    if (checked[i]) category.id?.let { selectedCategoryIds.add(it) }
                }
                renderServiceChips()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    // ---------------------------------------------------------------- server

    private fun initResponse() {

        viewModel.getCategoryNotToken()
        viewModel.categoriesNotTokenResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                allCategories.clear()
                                allCategories.addAll(it.data.orEmpty())
                                renderServiceChips()
                            }

                            CODE422 -> Utilities.showToastError(requireActivity(), it.message!!)
                            else -> Utilities.showToastError(requireActivity(), it.message!!)
                        }
                    }
                }

                is Resource.Error -> {
                    showProgress(false)
                    Log.i("TestVerification", "Error: ${result.message}")
                }

                is Resource.Loading -> showProgress(true)
            }
        })

        viewModel.me()
        viewModel.meResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                val data = it.data!!
                                showLogo(data.company_logo)
                                showCover(data.company_background_image)
                                showName(data.title)
                                showRating(data.avg_rate, data.rate_count)
                                showAccountType(data.account_type)

                                mViewDataBinding.tvFullName.setText(data.title)
                                mViewDataBinding.tvAddress.text = data.address.orEmpty()
                                mViewDataBinding.tvAboutYou.setText(data.description)
                                data.nationality_id?.data?.let { nid ->
                                    mViewDataBinding.tvNationalityId.text = nid.toString()
                                }

                                savedConsultPrice = data.consultant_price
                                mViewDataBinding.etConsultPrice.setText(
                                    data.consultant_price?.takeIf { p -> p > 0 }?.toString().orEmpty()
                                )

                                selectedCategoryIds.clear()
                                profileCategoryNames.clear()
                                data.categories.orEmpty().forEach { c ->
                                    c.id?.let { id ->
                                        selectedCategoryIds.add(id)
                                        profileCategoryNames[id] = c.title.orEmpty()
                                    }
                                }
                                renderServiceChips()
                            }

                            CODE422 -> Utilities.showToastError(requireActivity(), it.message!!)
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

                is Resource.Loading -> showProgress(true)
            }
        })

        homeViewModel.getCompanyWorks()
        homeViewModel.getCompanyWorksResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    val works = result.data?.data.orEmpty()
                    worksAdapter.submit(works)
                    mViewDataBinding.tvWorksCount.text = getString(R.string.profile_works_count, works.size)
                    mViewDataBinding.btnViewAllWorks.visibility = if (works.isEmpty()) View.GONE else View.VISIBLE
                }

                is Resource.Error -> showProgress(false)
                is Resource.Loading -> {}
            }
        })

        viewModel.updateConsultationPriceResponse.observe(viewLifecycleOwner, Observer { result ->
            if (result is Resource.Error) {
                Log.i("MemberProfile", "consultation price update failed")
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
                                mainActivity.navController!!.popBackStack()
                            }

                            CODE422 -> Utilities.showToastError(requireActivity(), it.message!!)
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

                is Resource.Loading -> showProgress(true)
            }
        })
    }

    // ---------------------------------------------------------------- clicks

    fun onClick() {

        mViewDataBinding.btnBack.setOnClickListener {
            mViewDataBinding.root.findNavController().popBackStack()
        }

        mViewDataBinding.btnChangeLogo.setOnClickListener { openGallery(isCover = false) }
        mViewDataBinding.btnChangeCover.setOnClickListener { openGallery(isCover = true) }

        mViewDataBinding.layoutAccountType.setOnClickListener {
            Toast.makeText(requireContext(), R.string.profile_account_type_locked, Toast.LENGTH_SHORT).show()
        }
        mViewDataBinding.tvTypeIndividual.setOnClickListener {
            Toast.makeText(requireContext(), R.string.profile_account_type_locked, Toast.LENGTH_SHORT).show()
        }
        mViewDataBinding.tvTypeCompany.setOnClickListener {
            Toast.makeText(requireContext(), R.string.profile_account_type_locked, Toast.LENGTH_SHORT).show()
        }

        mViewDataBinding.layoutAddress.setOnClickListener {
            startActivityForResult(Intent(requireActivity(), MapsActivity::class.java), requestCodeMap)
        }

        mViewDataBinding.btnEditServices.setOnClickListener { pickServices() }
        mViewDataBinding.btnViewAllWorks.setOnClickListener { openAllWorks() }

        // No verification endpoint on the server yet
        mViewDataBinding.btnRequestVerification.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }

        mViewDataBinding.btnSave.setOnClickListener { save() }
    }

    private fun save() {
        val name = mViewDataBinding.tvFullName.text.toString().trim()
        val description = mViewDataBinding.tvAboutYou.text.toString().trim()
        val address = mViewDataBinding.tvAddress.text.toString().trim()
        val required = getString(R.string.this_item_is_required)

        when {
            name.isEmpty() -> mViewDataBinding.tvFullName.error = required
            address.isEmpty() -> Toast.makeText(requireContext(), R.string.profile_address_hint, Toast.LENGTH_SHORT).show()
            description.isEmpty() -> mViewDataBinding.tvAboutYou.error = required
            selectedCategoryIds.isEmpty() ->
                Toast.makeText(requireContext(), R.string.profile_select_service_required, Toast.LENGTH_SHORT).show()

            else -> {
                // Consultation price has its own endpoint: send it only when it changed
                val price = mViewDataBinding.etConsultPrice.text.toString().trim().toIntOrNull()
                if (price != null && price != savedConsultPrice) {
                    viewModel.updateConsultationPrice(price)
                    savedConsultPrice = price
                }

                viewModel.userUpdateProfile(
                    convertToRequestBody(name),
                    convertToRequestBody(description),
                    convertToRequestBody(address),
                    selectedCategoryIds.toList(),
                    convertFileToMultipart(logoFile, "company_logo"),
                    convertFileToMultipart(coverFile, "company_background_image"),
                )
            }
        }
    }

    private fun openGallery(isCover: Boolean) {
        Utilities.onPermission(requireActivity())
        TedImagePicker.with(requireContext()).image()
            .cameraTileImage(R.drawable.ic_defult_camera).showTitle(true).title(R.string.member_select)
            .backButton(R.drawable.icon_arrow_back).buttonText(R.string.done)
            .buttonBackground(R.drawable.shape_bottom)
            .start { uri ->
                val file = uri.toFile(requireContext().contentResolver)
                if (isCover) {
                    coverFile = file
                    Glide.with(this).load(uri).into(mViewDataBinding.ivCover)
                } else {
                    logoFile = file
                    mViewDataBinding.tvLogoInitial.visibility = View.GONE
                    mViewDataBinding.ivLogo.visibility = View.VISIBLE
                    Glide.with(this).load(uri).into(mViewDataBinding.ivLogo)
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
        if (isConnected) {
            initResponse()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mainActivity.showHomeToolbar()
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == requestCodeMap && data != null) {
            mViewDataBinding.tvAddress.text = data.getStringExtra("ADDRESS").orEmpty()
        }
    }
}
