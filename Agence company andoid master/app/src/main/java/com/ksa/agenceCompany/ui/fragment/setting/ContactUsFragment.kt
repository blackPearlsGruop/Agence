package com.ksa.agenceCompany.ui.fragment.setting

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.common.CODE200
import com.ksa.agenceCompany.common.CODE422
import com.ksa.agenceCompany.common.Resource
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.databinding.FragmentContacUsBinding
import com.ksa.agenceCompany.ui.activity.MainActivity
import com.ksa.agenceCompany.viewModels.InfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContactUsFragment : BaseFragment<FragmentContacUsBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_contac_us
    private val viewModel: InfoViewModel by viewModel()


    private lateinit var mainActivity: MainActivity

    private val faqQuestions by lazy {
        listOf(
            getString(R.string.faq_q1), getString(R.string.faq_q2), getString(R.string.faq_q3),
            getString(R.string.faq_q4), getString(R.string.faq_q5), getString(R.string.faq_q6)
        )
    }
    private val faqAnswers by lazy {
        listOf(
            getString(R.string.faq_a1), getString(R.string.faq_a2), getString(R.string.faq_a3),
            getString(R.string.faq_a4), getString(R.string.faq_a5), getString(R.string.faq_a6)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mainActivity = requireActivity() as MainActivity
        // FIGMA REDESIGN: fragment_contac_us.xml has its own light header now.
        // MainActivity's destination-changed listener already hides the old
        // shared blue toolbar for this screen, same as Home/Account.

        buildFaqList()
        onClick()
    }

    private fun buildFaqList() {
        val container = mViewDataBinding.faqContainer
        container.removeAllViews()

        for (i in faqQuestions.indices) {
            val rowWrapper = LinearLayout(requireActivity())
            rowWrapper.orientation = LinearLayout.VERTICAL
            rowWrapper.setBackgroundResource(R.drawable.bg_agence_faq_row)
            val wrapperParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
            wrapperParams.topMargin = if (i == 0) 0 else dp(8)
            rowWrapper.layoutParams = wrapperParams

            val questionRow = LinearLayout(requireActivity())
            questionRow.orientation = LinearLayout.HORIZONTAL
            questionRow.gravity = android.view.Gravity.CENTER_VERTICAL
            questionRow.setPadding(dp(13), dp(11), dp(13), dp(11))

            val questionText = TextView(requireActivity())
            questionText.text = faqQuestions[i]
            questionText.setTextColor(resources.getColor(R.color.agence_black))
            questionText.textSize = 13f
            questionText.typeface = androidx.core.content.res.ResourcesCompat.getFont(requireActivity(), R.font.somar_semi_bold)
            val qParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            questionText.layoutParams = qParams

            val chevron = ImageView(requireActivity())
            chevron.setImageResource(R.drawable.icon_arrow_down)
            chevron.layoutParams = LinearLayout.LayoutParams(dp(14), dp(14))

            questionRow.addView(questionText)
            questionRow.addView(chevron)

            val answerText = TextView(requireActivity())
            answerText.text = faqAnswers[i]
            answerText.setTextColor(resources.getColor(R.color.agence_muted))
            answerText.textSize = 11.5f
            answerText.setLineSpacing(dp(2).toFloat(), 1f)
            answerText.setPadding(dp(13), 0, dp(13), dp(12))
            answerText.visibility = View.GONE

            rowWrapper.addView(questionRow)
            rowWrapper.addView(answerText)

            questionRow.setOnClickListener {
                val opening = answerText.visibility == View.GONE
                answerText.visibility = if (opening) View.VISIBLE else View.GONE
                val rotation = RotateAnimation(
                    if (opening) 0f else 180f, if (opening) 180f else 0f,
                    android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f,
                    android.view.animation.Animation.RELATIVE_TO_SELF, 0.5f
                )
                rotation.duration = 150
                rotation.fillAfter = true
                chevron.startAnimation(rotation)
            }

            container.addView(rowWrapper)
        }
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

    private fun onClick() {

        mViewDataBinding.btnSend.setOnClickListener {

            val titleMessage=mViewDataBinding.tvTitle.text.toString()
            val dicMessage=mViewDataBinding.tvMessageContent.text.toString()

            if (titleMessage.isEmpty())
            {
                mViewDataBinding.tvTitle.error=getString(R.string.this_item_is_required)
            }
            else     if (dicMessage.isEmpty())
            {
                mViewDataBinding.tvMessageContent.error=getString(R.string.this_item_is_required)
            }
            else{
                viewModel.contactUs(titleMessage,dicMessage)

            }

        }
    }


    private fun initResponse() {
        // resend response
        viewModel.contactUsResponse.observe(viewLifecycleOwner, Observer { result ->
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
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        // يتم استدعاء هذه الدالة عندما يتغير حالة الاتصال
        if (isConnected) {
            // يمكنك إجراء أي إجراءات إضافية هنا عند الاتصال بالإنترنت
            initResponse()

        } else {
        }

    }

}
