package com.ksa.agenceCompany.ui.fragment.home

import android.os.Bundle
import android.view.View
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.databinding.FragmentOfferDetailsBinding
import com.ksa.agenceCompany.viewModels.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class OfferDetailsFragment : BaseFragment<FragmentOfferDetailsBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_offer_details

    private val viewModel: HomeViewModel by viewModel()


    private var id_offer: Int=0
    private var imageCompany:String=""
    private var nameCompany:String =""
    private var dicCategory:String=""
    private var nameCategory:String=""
    private var priceOffer:String=""
    private var descriptionOrder:String=""
    private var orderNo:String=""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null){

            val args:OfferDetailsFragmentArgs=OfferDetailsFragmentArgs.fromBundle(requireArguments())
            id_offer=args.idOffer
            imageCompany=args.imageCompany
            nameCompany=args.nameCompany
            dicCategory=args.dicCategory
            nameCategory=args.nameCategory
            priceOffer=args.priceOffer
            descriptionOrder=args.descriptionOrder
            orderNo=args.orderNo
        }



        Utilities.onLoadImageFromUrl(requireActivity(),
            imageCompany,mViewDataBinding.ivLogoCompany )

        mViewDataBinding.tvNameCompany.text=nameCompany
        mViewDataBinding.tvDescriptionCat.text=dicCategory
        mViewDataBinding.tvOrderDescription.text=descriptionOrder
        mViewDataBinding.tvNoOrder.text=getString(R.string.order_no)+" "+orderNo
        mViewDataBinding.tvPrice.text=" "+priceOffer+" "+getString(R.string.r_s)
    }


    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        // يتم استدعاء هذه الدالة عندما يتغير حالة الاتصال
        if (isConnected) {
            // يمكنك إجراء أي إجراءات إضافية هنا عند الاتصال بالإنترنت

        } else {
        }

    }


}