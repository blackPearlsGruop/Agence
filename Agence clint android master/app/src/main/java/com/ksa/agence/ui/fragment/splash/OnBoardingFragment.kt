package com.ksa.agence.ui.fragment.splash

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.ksa.agence.R
import com.ksa.agence.adapter.OnBoardingAdapter
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.databinding.FragmentOnBoadingBinding
import com.ksa.agence.entity.itemOnBoardingResponse.ItemOnBoardingResponse

class OnBoardingFragment : BaseFragment<FragmentOnBoadingBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_on_boading

    lateinit var onBoardingAdapter: OnBoardingAdapter
    lateinit var listData: ArrayList<ItemOnBoardingResponse>

    private lateinit var indicatorContainer: LinearLayout
    private lateinit var indicators: Array<ImageView?>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        onClick()
    }

    private fun initData() {
        listData = ArrayList()
        onBoardingAdapter = OnBoardingAdapter(requireActivity(), listData)

        listData.add(
            ItemOnBoardingResponse(
                R.drawable.image_1, "حياك في ايجنس", "التطبيق الأول من نوعه في مجال الاعلام"
            )
        )
        listData.add(
            ItemOnBoardingResponse(
                R.drawable.image_2, "حياك في ايجنس", "التطبيق الأول من نوعه في مجال الاعلام"
            )
        )
        listData.add(
            ItemOnBoardingResponse(
                R.drawable.image_3, "حياك في ايجنس", "التطبيق الأول من نوعه في مجال الاعلام"
            )
        )

        mViewDataBinding.viewPagerOnBoarding.adapter = onBoardingAdapter
        setIndicator()
        setCurrentIndicator(0)

    }

    fun onClick() {

        mViewDataBinding.viewPagerOnBoarding.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })


        mViewDataBinding.btnNext.setOnClickListener {
            if (mViewDataBinding.viewPagerOnBoarding.currentItem + 1 < onBoardingAdapter.itemCount) {
                mViewDataBinding.viewPagerOnBoarding.currentItem += 1
            } else {
                navigateToLogin()
            }
        }
        mViewDataBinding.btnSkip.setOnClickListener {
            navigateToLogin()
        }

    }

    private fun navigateToLogin() {
        val action = OnBoardingFragmentDirections.actionOnBoardingFragmentToLoginFragment()
        mViewDataBinding.root.findNavController().navigate(action)
    }


    private fun setIndicator() {
        indicatorContainer = mViewDataBinding.indicatorContainer
        indicators = arrayOfNulls(onBoardingAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)

        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(), R.drawable.indicator_inactive_bg
                    )
                )
                it.layoutParams = layoutParams
                indicatorContainer.addView(it)
            }
        }
    }


    private fun setCurrentIndicator(position: Int) {
        val childCount = indicatorContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorContainer.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(), R.drawable.indicator_active_bg
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(), R.drawable.indicator_inactive_bg
                    )
                )
            }
        }
    }


    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        // يتم استدعاء هذه الدالة عندما يتغير حالة الاتصال
        if (isConnected) {
            // يمكنك إجراء أي إجراءات إضافية هنا عند الاتصال بالإنترنت

        } else {
        }

    }


}