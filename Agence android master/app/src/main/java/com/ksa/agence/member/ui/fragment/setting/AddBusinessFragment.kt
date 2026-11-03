package com.ksa.agence.member.ui.fragment.setting

import android.app.Dialog
import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ksa.agence.R
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.databinding.FragmentAddBusinessBinding
import com.ksa.agence.member.base.BaseFragment
import com.ksa.agence.member.common.util.Utilities
import com.ksa.agence.member.ui.activity.MainActivity
import com.ksa.agence.member.viewModels.HomeViewModel
import gun0912.tedimagepicker.builder.TedImagePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class AddBusinessFragment : BaseFragment<FragmentAddBusinessBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_add_business

    private val viewModel: HomeViewModel by viewModel()
    private lateinit var mainActivity: MainActivity

    private var imageFile: File? = null
    private val descriptionMax = 300

    /** true only while an add request sent from this screen is waiting for its result */
    private var addPending = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()

        setupDescriptionCounter()
        showSelectedImage(null)
        onClick()
        observeResponse()
    }

    private fun onClick() {
        mViewDataBinding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        mViewDataBinding.layoutUpload.setOnClickListener { openGallery() }

        mViewDataBinding.btnSave.setOnClickListener { submit() }
    }

    private fun submit() {
        val title = mViewDataBinding.tvTitle.text.toString().trim()
        val description = mViewDataBinding.tvDescription.text.toString().trim()

        when {
            imageFile == null -> {
                Utilities.showToastError(requireActivity(), getString(R.string.works_image_required))
            }

            title.isEmpty() -> {
                mViewDataBinding.tvTitle.error = getString(R.string.member_this_item_is_required)
                mViewDataBinding.tvTitle.requestFocus()
            }

            description.isEmpty() -> {
                mViewDataBinding.tvDescription.error = getString(R.string.member_this_item_is_required)
                mViewDataBinding.tvDescription.requestFocus()
            }

            else -> {
                addPending = true
                viewModel.addWorks(
                    Utilities.convertToRequestBody(title),
                    Utilities.convertToRequestBody(description),
                    Utilities.convertFileToMultipart(imageFile, "work_file"),
                )
            }
        }
    }

    private fun setupDescriptionCounter() {
        val update = {
            mViewDataBinding.tvDescriptionCounter.text =
                "${mViewDataBinding.tvDescription.text.length} / $descriptionMax"
        }
        update()
        mViewDataBinding.tvDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) = update()
        })
    }

    private fun showSelectedImage(uri: Uri?) {
        val hasImage = uri != null
        mViewDataBinding.layoutUploadPlaceholder.visibility = if (hasImage) View.GONE else View.VISIBLE
        mViewDataBinding.ivImage.visibility = if (hasImage) View.VISIBLE else View.GONE
        mViewDataBinding.tvChangeImage.visibility = if (hasImage) View.VISIBLE else View.GONE
        if (hasImage) {
            Glide.with(this).load(uri).centerCrop().into(mViewDataBinding.ivImage)
        }
    }

    private fun observeResponse() {
        viewModel.addWorksResponse.observe(viewLifecycleOwner, Observer { result ->
            if (!addPending) return@Observer
            when (result) {
                is Resource.Success -> {
                    addPending = false
                    showProgress(false)
                    result.data?.let {
                        when (it.code) {
                            CODE200 -> showDialogSuccess()

                            CODE422 -> Utilities.showToastError(requireActivity(), it.message!!)

                            else -> Utilities.showToastError(requireActivity(), it.message!!)
                        }
                    }
                }

                is Resource.Error -> {
                    addPending = false
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
        // nothing to load on this screen
    }

    private fun openGallery() {
        Utilities.onPermission(requireActivity())
        TedImagePicker.with(requireContext()).image()
            .cameraTileImage(R.drawable.ic_defult_camera).showTitle(true).title(R.string.member_select)
            .backButton(R.drawable.icon_arrow_back).buttonText(R.string.done)
            .buttonBackground(R.drawable.shape_bottom)
            .start { uri ->
                imageFile = uri.toFile(requireContext().contentResolver)
                showSelectedImage(uri)
            }
    }

    /** Resolves the picked image to a real file, copying it to the cache when no path is exposed. */
    fun Uri.toFile(contentResolver: ContentResolver): File? {
        if (scheme == "file") return path?.let { File(it) }

        val directPath = try {
            contentResolver.query(this, arrayOf(MediaStore.Images.Media.DATA), null, null, null)
                ?.use { c ->
                    if (c.moveToFirst()) {
                        val index = c.getColumnIndex(MediaStore.Images.Media.DATA)
                        if (index >= 0) c.getString(index) else null
                    } else null
                }
        } catch (e: Exception) {
            null
        }
        if (!directPath.isNullOrBlank() && File(directPath).canRead()) return File(directPath)

        return try {
            val target = File(requireContext().cacheDir, "work_${System.currentTimeMillis()}.jpg")
            contentResolver.openInputStream(this)?.use { input ->
                target.outputStream().use { output -> input.copyTo(output) }
            }
            if (target.length() > 0) target else null
        } catch (e: Exception) {
            null
        }
    }

    fun showDialogSuccess() {
        val dialog = Dialog(requireActivity(), R.style.customDialogTheme)
        dialog.setCancelable(false)
        val v: View = requireActivity().layoutInflater.inflate(R.layout.member_dialog_success_order, null)
        dialog.setContentView(v)

        val ivClose = dialog.findViewById<ImageView>(R.id.imageViewClose)
        val orderTitle = dialog.findViewById<TextView>(R.id.textViewTitel)
        val orderNo = dialog.findViewById<TextView>(R.id.tv_order_number)

        orderTitle.text = getString(R.string.works_added)
        orderNo.visibility = View.GONE

        ivClose.setOnClickListener {
            dialog.dismiss()
            findNavController().popBackStack()
        }

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
}
