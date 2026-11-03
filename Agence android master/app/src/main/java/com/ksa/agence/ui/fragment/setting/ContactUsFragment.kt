package com.ksa.agence.ui.fragment.setting

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.RotateAnimation
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ksa.agence.R
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.databinding.FragmentContacUsBinding
import com.ksa.agence.ui.activity.MainActivity

class ContactUsFragment : BaseFragment<FragmentContacUsBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_contac_us

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
        // FIGMA REDESIGN: fragment_contac_us.xml has its own light header now,
        // same pattern as Home/Account/Orders/Messages — hide the old shared
        // blue toolbar entirely instead of the previous hideHomeToolbar()
        // approach (which just toggled the search box, not the whole bar).
        try {
            mainActivity.mViewDataBinding.constraintLayout2.visibility = View.GONE
            mainActivity.mViewDataBinding.btnQuickOrder.visibility = View.GONE
        } catch (e: Exception) {
            Log.e("ContactUsFragment", "constraintLayout2 hide failed", e)
        }

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
        mViewDataBinding.btnStartChat.setOnClickListener {
            mViewDataBinding.btnStartChat.visibility = View.GONE
            mViewDataBinding.layoutChatWidget.visibility = View.VISIBLE

            // Same as the Figma reference: the widget opens with one greeting
            // bubble from support already in it.
            if (mViewDataBinding.chatMessagesContainer.childCount == 0) {
                addChatBubble(getString(R.string.support_greeting), fromMe = false)
            }
        }

        mViewDataBinding.btnSendChat.setOnClickListener { sendChatMessage() }

        mViewDataBinding.etChatMessage.setOnEditorActionListener { _, _, _ ->
            sendChatMessage()
            true
        }
    }

    private fun sendChatMessage() {
        val text = mViewDataBinding.etChatMessage.text.toString().trim()
        if (text.isEmpty()) return
        addChatBubble(text, fromMe = true)
        mViewDataBinding.etChatMessage.text.clear()
    }

    // Local-only widget for now (matches the Figma reference exactly, which
    // is also local state with no real backend) — swap in a real support
    // conversation later without changing the layout.
    private fun addChatBubble(text: String, fromMe: Boolean) {
        val row = LinearLayout(requireActivity())
        row.orientation = LinearLayout.HORIZONTAL
        row.gravity = if (fromMe) Gravity.END else Gravity.START
        val rowParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        rowParams.bottomMargin = dp(8)
        row.layoutParams = rowParams

        val bubble = TextView(requireActivity())
        bubble.text = text
        bubble.setTextColor(resources.getColor(if (fromMe) R.color.white else R.color.agence_black))
        bubble.textSize = 12f
        bubble.typeface = androidx.core.content.res.ResourcesCompat.getFont(requireActivity(), R.font.somar_regular)
        bubble.setPadding(dp(12), dp(9), dp(12), dp(9))
        bubble.setBackgroundResource(if (fromMe) R.drawable.message_background else R.drawable.message_background_dark)
        val bubbleParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        bubble.layoutParams = bubbleParams

        row.addView(bubble)
        mViewDataBinding.chatMessagesContainer.addView(row)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }

}
