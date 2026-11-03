package com.ksa.agence.member.ui.fragment.activity_log

import com.ksa.agence.common.sampleText
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ksa.agence.R
import com.ksa.agence.member.adapter.ActivityLog
import com.ksa.agence.member.adapter.ActivityLogAdapter
import com.ksa.agence.member.adapter.ActivityType
import com.ksa.agence.databinding.FragmentActivityLogBinding

class ActivityLogFragment : Fragment() {

    private var _binding: FragmentActivityLogBinding? = null
    private val binding get() = _binding!!

    private lateinit var allItems: MutableList<Any>
    private var currentFilter: ActivityType? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivityLogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
        setupRecyclerView(allItems)
        setupFilters()
        setupListeners()
    }

    private fun setupData() {
        val activities = listOf(
            ActivityLog(1, sampleText("فهد العتيبي"), "FA", sampleText("أكمل مهمة"), sampleText("عرض المفاهيم الأولية"),
                sampleText("قبل ساعتين"), ActivityType.TASK, R.color.agence_blue, sampleText("اليوم")),
            ActivityLog(2, sampleText("نجم للتسويق"), "NA", sampleText("أضاف تعليقاً على مهمة"), sampleText("تحليل المنافسين"),
                sampleText("قبل 4 ساعات"), ActivityType.TASK, R.color.member_agence_orange, sampleText("اليوم")),
            ActivityLog(3, sampleText("خالد المنصوري"), "KM", sampleText("عدّل توزيع المدفوعات"), "",
                sampleText("قبل 6 ساعات"), ActivityType.PAYMENT, R.color.agence_blue, sampleText("اليوم")),
            ActivityLog(4, sampleText("فهد العتيبي"), "FA", sampleText("أنشأ مهمة جديدة"), sampleText("جلسة المراجعة النهائية"),
                sampleText("أمس 3:50 م"), ActivityType.TASK, R.color.agence_blue, sampleText("أمس")),
            ActivityLog(5, sampleText("أثير سعودي"), "AS", sampleText("رفع ملفاً في"), sampleText("تصميم الهوية"),
                sampleText("أمس 11:00 ص"), ActivityType.FILE, R.color.member_agence_green, sampleText("أمس")),
            ActivityLog(6, sampleText("خالد المنصوري"), "KM", sampleText("دعا"), sampleText("أثير سعودي للفريق"),
                sampleText("قبل 3 أيام"), ActivityType.MEMBER, R.color.agence_blue, sampleText("هذا الأسبوع"))
        )
        allItems = groupByTime(activities)
    }

    private fun groupByTime(activities: List<ActivityLog>): MutableList<Any> {
        val result = mutableListOf<Any>()
        val grouped = activities.groupBy { it.timeGroup }
        grouped.forEach { (group, items) ->
            result.add(group)
            result.addAll(items)
        }
        return result
    }

    private fun setupRecyclerView(items: List<Any>) {
        binding.rvActivities.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ActivityLogAdapter(items)
        }
    }

    private fun setupFilters() {
        binding.chipAll.setOnClickListener {
            currentFilter = null
            applyFilter()
            selectChip(binding.chipAll)
        }
        binding.chipTasks.setOnClickListener {
            currentFilter = ActivityType.TASK
            applyFilter()
            selectChip(binding.chipTasks)
        }
        binding.chipMembers.setOnClickListener {
            currentFilter = ActivityType.MEMBER
            applyFilter()
            selectChip(binding.chipMembers)
        }
        binding.chipPayments.setOnClickListener {
            currentFilter = ActivityType.PAYMENT
            applyFilter()
            selectChip(binding.chipPayments)
        }
    }

    private fun applyFilter() {
        val filtered = if (currentFilter == null) {
            allItems.filterIsInstance<ActivityLog>()
        } else {
            allItems.filterIsInstance<ActivityLog>().filter { it.type == currentFilter }
        }
        setupRecyclerView(groupByTime(filtered))
    }

    private fun selectChip(selected: View) {
        val chips = listOf(binding.chipAll, binding.chipTasks, binding.chipMembers, binding.chipPayments)
        chips.forEach {
            if (it == selected) {
                it.setBackgroundResource(R.drawable.bg_chip_selected)
                (it as android.widget.TextView).setTextColor(resources.getColor(R.color.white, null))
            } else {
                it.setBackgroundResource(R.drawable.bg_chip_unselected)
                (it as android.widget.TextView).setTextColor(resources.getColor(R.color.agence_black, null))
            }
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
