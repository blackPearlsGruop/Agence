package com.ksa.agence.ui.fragment.home

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.adapter.CopanyAdapter
import com.ksa.agence.adapter.CategoriesAdapter
import com.ksa.agence.adapter.DropDownCityAdapter
import com.ksa.agence.adapter.MultiSelectCategoriesAdapter
import com.ksa.agence.adapter.SliderAdapter
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CODE200
import com.ksa.agence.common.CODE422
import com.ksa.agence.common.Resource
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentFilterBinding
import com.ksa.agence.databinding.FragmentHomeBinding
import com.ksa.agence.entity.bannerResponse.DataBannerResponse
import com.ksa.agence.entity.categoriesResponse.CategoriesResponse
import com.ksa.agence.entity.categoriesResponse.DataCategoriesResponse
import com.ksa.agence.entity.companyResponse.CompanyResponse
import com.ksa.agence.entity.companyResponse.DataCompanyResponse
import com.ksa.agence.interfaces.Company
import com.ksa.agence.ui.activity.MainActivity
import com.ksa.agence.viewModels.AuthenticationViewModel
import com.ksa.agence.viewModels.HomeViewModel
import com.ksa.agenceCompany.entity.cityResponse.DataCityResponse
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Timer
import java.util.TimerTask

class FilterFragment : BaseFragment<FragmentFilterBinding>(), Company {

    override fun getLayoutId(): Int = R.layout.fragment_filter
    private  var rate: String=""
    private  var price: String=""
    private  var account_type: String=""
    private var isOpen: Boolean=false
    private var cityId: Int=0
    private lateinit var mainActivity: MainActivity


    private var position: Int=0
    private val viewModel: HomeViewModel by viewModel()


    lateinit var multiSelectCategoriesAdapter: MultiSelectCategoriesAdapter
    lateinit var listCategories: ArrayList<DataCategoriesResponse>

    lateinit var companyAdapter: CopanyAdapter
    lateinit var  listCompany: ArrayList<DataCompanyResponse>

    private val authenticationViewModel: AuthenticationViewModel by viewModel()



    lateinit var dropDownCityAdapter: DropDownCityAdapter
    lateinit var listCityData: ArrayList<DataCityResponse>



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()
        mainActivity.mViewDataBinding.constraintLayout2.visibility=View.GONE
        mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.search)

        listCityData = ArrayList()
        listCategories = ArrayList()
        listCompany = ArrayList()

        onClick()


    }




    private fun initResponse() {

        listCityData.add(
            DataCityResponse(
                0, getString(R.string.select)
            )
        )
        // resend response
        authenticationViewModel.getCity()
        authenticationViewModel.cityResponse.observe(viewLifecycleOwner, Observer { result ->
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
        viewModel.getCategory()
        viewModel.categoriesResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    result.data?.let { it ->
                        when (it.code) {
                            CODE200 -> {
                                listCategories.clear() // Clear the existing data
                                listCategories.addAll(it.data!!)
                                multiSelectCategoriesAdapter = MultiSelectCategoriesAdapter(requireActivity(), listCategories)
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



        viewModel.companyResultFilterResponse.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Success -> {
                    showProgress(false)
                    listCompany.clear()
                    result.data?.let { it ->
                        when (it.code) {
                            // dismiss loading

                            CODE200 -> {
                                listCompany.addAll(it.data!!)
                                companyAdapter =
                                    CopanyAdapter(requireActivity(), listCompany,this)
                                mViewDataBinding.rvCompany.adapter = companyAdapter
                                companyAdapter.notifyDataSetChanged()

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


        mViewDataBinding.ivBackPage.setOnClickListener {
            mainActivity.navController!!.popBackStack()
        }

        mViewDataBinding.ivNotification.setOnClickListener {
            mainActivity.navController!!.navigate(R.id.notificationFragment)
            mainActivity.showHomeToolbar()
            mainActivity.mViewDataBinding.tvTitleToolBar.setText(R.string.notification)
            mainActivity.mViewDataBinding.constraintLayout2.visibility=View.VISIBLE

        }



        mViewDataBinding.spCity?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    if (position != 0) {
                        cityId = listCityData.get(position).id!!
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

        mViewDataBinding.radioGroup.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.rb_companies -> {
                        // do something when radio button 1 is selected
                        account_type = "company"
                    }

                    R.id.rb_individuals -> {
                        // do something when radio button 2 is selected
                        account_type = "personal"
                    }
                    // add more cases here to handle other buttons in the your RadioGroup
                }
            }
        }

        //low_to_high,high_to_low
        if(mViewDataBinding.rbFromLowestToHighestPrice.isChecked==false)
        {
            price="low_to_high"
        }
        if(mViewDataBinding.rbFromHighestToLowestRating.isChecked==false)
        {
            rate="low_to_high"
        }

        mViewDataBinding.btnSearch.setOnClickListener {

            viewModel.getCompanyResultFilter(account_type,cityId,multiSelectCategoriesAdapter.selectedItems,price,rate,"")
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

    override fun clickItemCompany(idCompany: Int,flag:String) {
        val action=FilterFragmentDirections.actionFilterFragmentToShowCompanyFragment(idCompany,"FIlir")
        mViewDataBinding.root.findNavController().navigate(action)
        mainActivity.mViewDataBinding.constraintLayout2.visibility=View.VISIBLE

    }

    override fun clickItemAddCompanyFav(idCompany: Int, pos: Int) {
        position=pos
        viewModel.addFavourites(idCompany)
    }

    override fun clickItemShowService(idService: Int) {


    }


    override fun onDestroy() {
        super.onDestroy()
        mainActivity.showHomeToolbar()
        mainActivity.mViewDataBinding.constraintLayout2.visibility=View.VISIBLE

    }

}