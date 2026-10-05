package com.ksa.agenceCompany.ui.dialog

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.base.BaseBottomDialog
import com.ksa.agenceCompany.databinding.FragmentNewTaskSheetBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Bottom sheet for adding a new task from the Team Leader Panel.
 * Mock only — swap for a real POST when the endpoint exists.
 */
class NewTaskBottomSheetFragment : BaseBottomDialog<FragmentNewTaskSheetBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_new_task_sheet

    private var selectedPriority: Priority = Priority.MEDIUM
    private var dueDateMillis: Long = 0

    private enum class Priority { LOW, MEDIUM, HIGH }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDefaultDate()
        setupClicks()
        highlightPriority(selectedPriority)
    }

    private fun setupDefaultDate() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, 3)
        dueDateMillis = cal.timeInMillis
        updateDateLabel()
    }

    private fun updateDateLabel() {
        try {
            val isArabic = Locale.getDefault().language == "ar"
            val formatter = SimpleDateFormat(
                "d MMMM yyyy",
                if (isArabic) Locale("ar") else Locale.ENGLISH
            )
            mViewDataBinding.tvDueDate.text = formatter.format(dueDateMillis)
        } catch (e: Exception) {
            Log.e("NewTaskSheet", "Failed to format date", e)
        }
    }

    private fun setupClicks() {
        mViewDataBinding.btnCloseSheet.setOnClickListener { dismiss() }
        mViewDataBinding.btnCancelSheet.setOnClickListener { dismiss() }

        mViewDataBinding.chipPriorityLow.setOnClickListener {
            selectedPriority = Priority.LOW
            highlightPriority(selectedPriority)
        }
        mViewDataBinding.chipPriorityMed.setOnClickListener {
            selectedPriority = Priority.MEDIUM
            highlightPriority(selectedPriority)
        }
        mViewDataBinding.chipPriorityHigh.setOnClickListener {
            selectedPriority = Priority.HIGH
            highlightPriority(selectedPriority)
        }

        mViewDataBinding.rowDueDate.setOnClickListener { pickDate() }

        mViewDataBinding.rowAssignee.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "اختيار المسؤول قريباً",
                Toast.LENGTH_SHORT
            ).show()
        }

        mViewDataBinding.boxAttach.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "إرفاق ملف قريباً",
                Toast.LENGTH_SHORT
            ).show()
        }

        mViewDataBinding.btnSaveTask.setOnClickListener {
            val title = mViewDataBinding.etTaskTitle.text.toString().trim()
            if (title.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "اكتب عنوان المهمة أولاً",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            Toast.makeText(
                requireContext(),
                "تم حفظ المهمة بنجاح ✓",
                Toast.LENGTH_SHORT
            ).show()
            dismiss()
        }
    }

    private fun pickDate() {
        val cal = Calendar.getInstance()
        cal.timeInMillis = dueDateMillis
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val picked = Calendar.getInstance()
                picked.set(year, month, day)
                dueDateMillis = picked.timeInMillis
                updateDateLabel()
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun highlightPriority(p: Priority) {
        val defaultBg = R.drawable.bg_priority_chip
        val defaultColor = 0xFF7B7B85.toInt()

        mViewDataBinding.chipPriorityLow.setBackgroundResource(defaultBg)
        mViewDataBinding.chipPriorityLow.setTextColor(defaultColor)
        mViewDataBinding.chipPriorityMed.setBackgroundResource(defaultBg)
        mViewDataBinding.chipPriorityMed.setTextColor(defaultColor)
        mViewDataBinding.chipPriorityHigh.setBackgroundResource(defaultBg)
        mViewDataBinding.chipPriorityHigh.setTextColor(defaultColor)

        when (p) {
            Priority.LOW -> {
                mViewDataBinding.chipPriorityLow
                    .setBackgroundResource(R.drawable.bg_priority_chip_low_selected)
                mViewDataBinding.chipPriorityLow.setTextColor(0xFF24BF61.toInt())
            }
            Priority.MEDIUM -> {
                mViewDataBinding.chipPriorityMed
                    .setBackgroundResource(R.drawable.bg_priority_chip_med_selected)
                mViewDataBinding.chipPriorityMed.setTextColor(0xFFF58220.toInt())
            }
            Priority.HIGH -> {
                mViewDataBinding.chipPriorityHigh
                    .setBackgroundResource(R.drawable.bg_priority_chip_high_selected)
                mViewDataBinding.chipPriorityHigh.setTextColor(0xFFE63946.toInt())
            }
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }
}
