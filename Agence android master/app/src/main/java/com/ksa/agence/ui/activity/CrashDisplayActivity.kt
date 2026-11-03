package com.ksa.agence.ui.activity

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

// TEMPORARY DEBUG SCREEN: shows the last crash's full stack trace directly on
// the device, so it can be read without Logcat or Device Explorer. Launched
// automatically by AgenceApp's crash handler. Remove once done debugging.
class CrashDisplayActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val trace = intent.getStringExtra(EXTRA_TRACE) ?: "لا يوجد تفاصيل"

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.WHITE)
            setPadding(32, 64, 32, 32)
        }

        val title = TextView(this).apply {
            text = "صار كراش — هذا تفاصيل الخطأ"
            textSize = 18f
            setTextColor(Color.parseColor("#E96D07"))
            setPadding(0, 0, 0, 24)
        }
        root.addView(title)

        val copyButton = Button(this).apply {
            text = "نسخ النص"
            setOnClickListener {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("crash", trace))
                Toast.makeText(this@CrashDisplayActivity, "انتسخ", Toast.LENGTH_SHORT).show()
            }
        }
        root.addView(copyButton)

        val closeButton = Button(this).apply {
            text = "إغلاق"
            setOnClickListener { finishAffinity() }
        }
        root.addView(closeButton)

        val scroll = ScrollView(this)
        val traceView = TextView(this).apply {
            text = trace
            textSize = 13f
            setTextColor(Color.BLACK)
            setTextIsSelectable(true)
            gravity = Gravity.START
        }
        scroll.addView(traceView)
        root.addView(
            scroll,
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f)
        )

        setContentView(root)
    }

    companion object {
        const val EXTRA_TRACE = "extra_trace"
    }
}
