package com.example.awesomedialog

import android.app.Activity
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.yehia.awesomedialoga.R

class AwesomeDialog {

    /***
     * Positions For Alert Dialog
     * */
    enum class POSITIONS {
        CENTER, BOTTOM
    }

    companion object {
        private lateinit var layoutInflater: LayoutInflater

        /***
         * core method For Alert Dialog
         * */
        fun build(
            context: Activity
        ): AlertDialog {
            layoutInflater = LayoutInflater.from(context)
            val alertDialog = AlertDialog.Builder(
                context, R.style.full_screen_dialog
            ).setView(R.layout.awesome_dilaog)
            val alert: AlertDialog = alertDialog.create()
            // Let's start with animation work. We just need to create a style and use it here as follows.
            //Pop In and Pop Out Animation yet pending
//            alert.window?.attributes?.windowAnimations = R.style.SlidingDialogAnimation
            alert.show()
            return alert
        }
    }
}

/***
 * Title Properties For Alert Dialog
 * */
fun AlertDialog.title(
    title: String, fontStyle: Typeface? = null, titleColor: Int = 0
): AlertDialog {
    this.findViewById<TextView>(R.id.title)?.text = title.trim()
    if (fontStyle != null) {
        this.findViewById<TextView>(R.id.title)?.typeface = fontStyle
    }
    if (titleColor != 0) {
        this.findViewById<TextView>(R.id.title)?.setTextColor(titleColor)
    }
    this.findViewById<TextView>(R.id.title)?.show()
    return this
}

/***
 * Dialog Background properties For Alert Dialog
 * */
fun AlertDialog.background(
    dialogBackgroundColor: Int? = null
): AlertDialog {
    if (dialogBackgroundColor != null) {
        this.findViewById<ConstraintLayout>(R.id.mainLayout)?.setBackgroundResource(dialogBackgroundColor)
    }
    return this
}

/***
 * Positions of Alert Dialog
 * */
fun AlertDialog.position(
    position: AwesomeDialog.POSITIONS = AwesomeDialog.POSITIONS.BOTTOM
): AlertDialog {
    val layoutParams = this.findViewById<ConstraintLayout>(R.id.mainLayout)?.layoutParams as RelativeLayout.LayoutParams
    if (position == AwesomeDialog.POSITIONS.CENTER) {
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
    } else if (position == AwesomeDialog.POSITIONS.BOTTOM) {
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
    }
    this.findViewById<ConstraintLayout>(R.id.mainLayout)!!.layoutParams = layoutParams
    return this
}

/***
 * Sub Title or Body of Alert Dialog
 * */
fun AlertDialog.body(
    body: String, fontStyle: Typeface? = null, color: Int = 0
): AlertDialog {
    this.findViewById<TextView>(R.id.subHeading)?.text = body.trim()
//    this.findViewById<TextView>(R.id.subHeading)?.text = Html.from Html(body.trim(), Html.FROM_HTML_MODE_LEGACY)
    this.findViewById<TextView>(R.id.subHeading)?.show()
    if (fontStyle != null) {
        this.findViewById<TextView>(R.id.subHeading)?.typeface = fontStyle
    }
    if (color != 0) {
        this.findViewById<TextView>(R.id.subHeading)?.setTextColor(color)
    }
    return this
}

/***
 * Icon of  Alert Dialog
 * */
fun AlertDialog.icon(
    icon: Int, animateIcon: Boolean = false
): AlertDialog {
    this.findViewById<ImageView>(R.id.image)?.setImageResource(icon)
    this.findViewById<ImageView>(R.id.image)?.show()
    // Pulse Animation for Icon
    if (animateIcon) {
        val pulseAnimation = AnimationUtils.loadAnimation(context, R.anim.pulse)
        this.findViewById<ImageView>(R.id.image)?.startAnimation(pulseAnimation)
    }
    return this
}

/***
 * onPositive Button Properties For Alert Dialog
 *
 * No Need to call dismiss(). It is calling already
 * */
fun AlertDialog.onPositive(
    text: String,
    buttonBackgroundColor: Int? = null,
    textColor: Int? = null,
    action: (() -> Unit)? = null
): AlertDialog {
    this.findViewById<TextView>(R.id.yesButton)?.show()
    if (buttonBackgroundColor != null) {
        this.findViewById<TextView>(R.id.yesButton)?.setBackgroundResource(buttonBackgroundColor)
    }
    if (textColor != null) {
        this.findViewById<TextView>(R.id.yesButton)?.setTextColor(textColor)
    }
    this.findViewById<TextView>(R.id.yesButton)?.text = text.trim()
    this.findViewById<TextView>(R.id.yesButton)?.setOnClickListener {
        action?.invoke()
        dismiss()
    }
    return this
}

/***
 * onNegative Button Properties For Alert Dialog
 *
 * No Need to call dismiss(). It is calling already
 * */
fun AlertDialog.onNegative(
    text: String,
    buttonBackgroundColor: Int? = null,
    textColor: Int? = null,
    action: (() -> Unit)? = null
): AlertDialog {
    this.findViewById<TextView>(R.id.noButton)?.show()
    this.findViewById<TextView>(R.id.noButton)?.text = text.trim()
    if (textColor != null) {
        this.findViewById<TextView>(R.id.noButton)?.setTextColor(textColor)
    }
    if (buttonBackgroundColor != null) {
        this.findViewById<TextView>(R.id.noButton)?.setBackgroundResource(buttonBackgroundColor)
    }
    this.findViewById<TextView>(R.id.noButton)?.setOnClickListener {
        action?.invoke()
        dismiss()
    }
    return this
}

private fun View.show() {
    this.visibility = View.VISIBLE
}


/*
 * 21300 * 12.7 = 270000
 * 15 * 3578.25 = 53500
 * 3100 * 47.44 = 147000
 * 40000
 * 150000
 * 270000 + 53500 + 147000 + 40000 + 150000 = 660000

 */