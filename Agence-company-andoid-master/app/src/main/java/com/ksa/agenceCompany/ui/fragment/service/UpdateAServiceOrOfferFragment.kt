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
import com.ksa.agenceCompany.adapter.SliderImageServiceAdapter
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.common.CODE200
import com.ksa.agenceCompany.common.CODE422
import com.ksa.agenceCompany.common.Resource
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.common.util.Utilities.Companion.convertFileToMultipartList
import com.ksa.agenceCompany.common.util.Utilities.Companion.convertToRequestBody
import com.ksa.agenceCompany.databinding.FragmentAddAServiceOrOfferBinding
import com.ksa.agenceCompany.databinding.FragmentUpdateAServiceOrOfferBinding
import com.ksa.agenceCompany.interfaces.DeleteImageLocale
import com.ksa.agenceCompany.ui.activity.MainActivity
import com.ksa.agenceCompany.viewModels.HomeViewModel
import gun0912.tedimagepicker.builder.TedImagePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class UpdateAServiceOrOfferFragment : BaseFragment<FragmentUpdateAServiceOrOfferBinding>() ,
    DeleteImageLocale {

    override fun getLayoutId(): Int = R.layout.fragment_update_a_service_or_offer
    private var idService: Int=0
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

        if (arguments != null){

            val args: UpdateAServiceOrOfferFragmentArgs = UpdateAServiceOrOfferFragmentArgs.fromBundle(requireArguments())
            idService=args.idServiceOrOffer
            type=args.type

            if (type=="Service"){
                viewModel.categoriesById(idService)
                mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.service_update)
            }
            else{
                viewModel.offerById(idService)
                mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.offer_update)

            }

        }


        onClick()


    }

    private fun initResponse() {

        viewModel.categoriesByIdResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {


                                mViewDataBinding.tvName.setText(it.data!!.title)
                                mViewDataBinding.tvDetails.setText(it.data!!.description)
                                mViewDataBinding.tvDurationOfCompletion.setText(""+it.data!!.service_duration_in_days)
                                mViewDataBinding.tvPrice.setText(""+it.data!!.price)


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


        viewModel.offerByIdResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading
                            CODE200 -> {


                                mViewDataBinding.tvName.setText(it.data!!.title)
                                mViewDataBinding.tvDetails.setText(it.data!!.description)
                                mViewDataBinding.tvDurationOfCompletion.setText(""+it.data!!.offer_duration_in_days)
                                mViewDataBinding.tvPrice.setText(""+it.data!!.price)


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


        viewModel.updateServiceResponse.observe(viewLifecycleOwner, Observer { result ->
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

        viewModel.updateOfferResponse.observe(viewLifecycleOwner, Observer { result ->
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


            mViewDataBinding.btnSave.setOnClickListener {

                title = mViewDataBinding.tvName.text.toString()
                orderDescription = mViewDataBinding.tvDetails.text.toString()
                price = mViewDataBinding.tvPrice.text.toString()
                durationOfCompletion = mViewDataBinding.tvDurationOfCompletion.text.toString()


                if (title.isEmpty()) {
                    mViewDataBinding.tvName.error = getString(R.string.this_item_is_required)
                } else if (price.isEmpty()) {
                    mViewDataBinding.tvPrice.error = getString(R.string.this_item_is_required)
                } else if (orderDescription.isEmpty()) {
                    mViewDataBinding.tvDetails.error = getString(R.string.this_item_is_required)
                } else if (durationOfCompletion.isEmpty()) {
                    mViewDataBinding.tvDurationOfCompletion.error = getString(R.string.this_item_is_required)
                } else {
                    if (type == "Service") {
                        viewModel.updateService(
                            idService,
                           title,
                            orderDescription,
                            price.toInt(),
                            durationOfCompletion.toInt()
                        )
                    } else if (type == "Offer") {

                        viewModel.updateOffer(
                            idService,
                            title,
                            orderDescription,
                            price.toInt(),
                            durationOfCompletion.toInt()
                        )

                    }
                }
        }
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
        if (type=="Service"){
            viewModel.categoriesById(idService)
            mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.service_details)
        }
        else{
            viewModel.offerById(idService)
            mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.offer_details)

        }

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

        orderTitle.text = getString(R.string.saved_successfully)
        orderNo.visibility = View.GONE

        ivClose.setOnClickListener {
            dialog.dismiss()
            mainActivity.navController!!.popBackStack()
        }

        dialog.show()

    }

    override fun delete(position: Int) {
        listImageLocale.removeAt(position)
        imageLocaleAdapter.notifyDataSetChanged()
    }


}