package com.ksa.agenceCompany.ui.fragment.service

import android.app.Dialog
import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.adapter.ImageLocaleAdapter
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.common.CODE200
import com.ksa.agenceCompany.common.CODE422
import com.ksa.agenceCompany.common.Resource
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.common.util.Utilities.Companion.convertFileToMultipartList
import com.ksa.agenceCompany.common.util.Utilities.Companion.convertToRequestBody
import com.ksa.agenceCompany.databinding.FragmentAddAServiceOrOfferBinding
import com.ksa.agenceCompany.interfaces.DeleteImageLocale
import com.ksa.agenceCompany.ui.activity.MainActivity
import com.ksa.agenceCompany.viewModels.HomeViewModel
import gun0912.tedimagepicker.builder.TedImagePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class AddAServiceOrOfferFragment : BaseFragment<FragmentAddAServiceOrOfferBinding>() ,
    DeleteImageLocale {

    override fun getLayoutId(): Int = R.layout.fragment_add_a_service_or_offer
    private  var type: String="Service"
    private lateinit var price: String
    private lateinit var durationOfCompletion: String
    private lateinit var title: String
    private lateinit var orderDescription: String
    private val viewModel: HomeViewModel by viewModel()
    private lateinit var mainActivity: MainActivity

    lateinit var imageLocaleAdapter: ImageLocaleAdapter
    lateinit var listImageLocale: ArrayList<String>
    lateinit var imagesList: ArrayList<File>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()


        onClick()


    }

    private fun initResponse() {


        viewModel.storeNewServiceResponse.observe(viewLifecycleOwner, Observer { result ->
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


    private fun onClick() {

        imagesList = ArrayList()

        mViewDataBinding.rbService.isChecked=true

        mViewDataBinding.ivAddImages.setOnClickListener {
            openGallery()
        }

        mViewDataBinding.radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->

            if (mViewDataBinding.rbOffer.isChecked) { // عرض اللغة الحالية في رسالة توست
                mViewDataBinding.tvName.hint = getString(R.string.offer)
                type = "Offer"

            } else if (mViewDataBinding.rbService.isChecked) {
                mViewDataBinding.tvName.hint = getString(R.string.service)
                type = "Service"


            }
        }

            mViewDataBinding.btnAddition.setOnClickListener {

                title = mViewDataBinding.tvName.text.toString()
                orderDescription = mViewDataBinding.tvDetails.text.toString()
                price = mViewDataBinding.tvPrice.text.toString()
                durationOfCompletion = mViewDataBinding.tvDurationOfCompletion.text.toString()

                if (mViewDataBinding.radioGroup.checkedRadioButtonId == -1) {
                    // إذا لم يكن هناك أي خيار محدد
                    Utilities.showToastError(requireActivity(), getString(R.string.select_the_type_service_or_offer))
                }
                else if (title.isEmpty()) {
                    mViewDataBinding.tvName.error = getString(R.string.this_item_is_required)
                } else if (price.isEmpty()) {
                    mViewDataBinding.tvPrice.error = getString(R.string.this_item_is_required)
                } else if (orderDescription.isEmpty()) {
                    mViewDataBinding.tvDetails.error = getString(R.string.this_item_is_required)
                } else if (durationOfCompletion.isEmpty()) {
                    mViewDataBinding.tvDurationOfCompletion.error = getString(R.string.this_item_is_required)
                } else {
                    if (type == "Service") {
                        viewModel.storeNewService(
                            convertToRequestBody(title),
                            convertToRequestBody(orderDescription),
                            convertToRequestBody(price),
                            convertToRequestBody(durationOfCompletion),
                            convertFileToMultipartList(imagesList)
                        )
                    } else if (type == "Offer") {

                        viewModel.storeNewOffer(
                            convertToRequestBody(title),
                            convertToRequestBody(orderDescription),
                            convertToRequestBody(price),
                            convertToRequestBody(durationOfCompletion),
                            convertFileToMultipartList(imagesList)
                        )

                    }
                }
        }
    }


    private fun openGallery() {
        listImageLocale = ArrayList()

        TedImagePicker.with(requireActivity()).image()
            // .imageAndVideo()
            // .video()
            .cameraTileImage(R.drawable.ic_defult_camera).showTitle(true).title(R.string.select)
            .backButton(R.drawable.ic_back).buttonText(R.string.done)
            .buttonBackground(R.drawable.shape_bottom)
           // .max(5, getString(R.string.you_cannot_add_more_than_5_photos)).drawerAlbum()
            // dropDownAlbum()
            .startMultiImage { uriList ->
                for (i in uriList.indices) {
//                    convertContentUriToFile(uriList[i])
                    listImageLocale.add(convertContentUriToFile(uriList[i]).toString())
                    imageLocaleAdapter = ImageLocaleAdapter(requireActivity(), listImageLocale, this)
                    mViewDataBinding.rvImages.adapter = imageLocaleAdapter
                    imagesList.add(convertContentUriToFile(uriList[i])!!)
                    //convertFileToMultipartList(imagesList)
                }

            }


    }

    fun convertContentUriToFile(contentUri: Uri): File? {
        var inputStream: InputStream? = null
        var fileOutputStream: FileOutputStream? = null
        var file: File? = null
        try {
            val contentResolver = requireActivity().contentResolver
            // إذا لم يتم العثور على الملف باستخدام الطريقة الأولى، استخدم طريقة أخرى للحصول على الملف
            if (file == null) {
                val fileName = "${System.currentTimeMillis()}.jpg"
                val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                file = File(dir, fileName)
                fileOutputStream = FileOutputStream(file)
                inputStream = contentResolver.openInputStream(contentUri)
                val buffer = ByteArray(1024)
                var read = inputStream!!.read(buffer)
                while (read != -1) {
                    fileOutputStream.write(buffer, 0, read)
                    read = inputStream.read(buffer)
                }
                fileOutputStream.flush()

            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
            fileOutputStream?.close()
        }

        return file
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
            mainActivity.navController!!.navigate(R.id.menuHome)
            mainActivity.showHomeToolbar()
        }

        dialog.show()

    }

    override fun delete(position: Int) {
        listImageLocale.removeAt(position)
        imageLocaleAdapter.notifyDataSetChanged()
    }


}