package com.ksa.agenceCompany.ui.fragment.team

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.adapter.LeaderTaskStatus
import com.ksa.agenceCompany.adapter.TeamLeaderTask
import com.ksa.agenceCompany.adapter.TeamLeaderTaskAdapter
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.databinding.FragmentTeamLeaderPanelBinding
import com.ksa.agenceCompany.ui.activity.MainActivity

class TeamLeaderPanelFragment : BaseFragment<FragmentTeamLeaderPanelBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_team_leader_panel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            val activity = requireActivity() as MainActivity
            activity.mViewDataBinding.constraintLayout2.visibility = View.GONE
        } catch (e: Exception) {
            Log.e("TeamLeaderPanel", "Failed to hide toolbar", e)
        }

        try {
            setupTasksList()
        } catch (e: Exception) {
            Log.e("TeamLeaderPanel", "setupTasksList failed", e)
        }

        try {
            setupClicks()
        } catch (e: Exception) {
            Log.e("TeamLeaderPanel", "setupClicks failed", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            val activity = requireActivity() as MainActivity
            activity.mViewDataBinding.constraintLayout2.visibility = View.VISIBLE
        } catch (e: Exception) {
            Log.e("TeamLeaderPanel", "Failed to restore toolbar", e)
        }
    }

    private fun setupTasksList() {
        val tasks = buildMockTasks()

        val adapter = TeamLeaderTaskAdapter(
            tasks = tasks,
            onTaskClick = { task ->
                Toast.makeText(requireContext(), "المهمة: ${task.title}", Toast.LENGTH_SHORT).show()
            },
            onMenuClick = { _ ->
                Toast.makeText(requireContext(), "خيارات المهمة", Toast.LENGTH_SHORT).show()
            }
        )

        mViewDataBinding.rvTasks.layoutManager = LinearLayoutManager(requireContext())
        mViewDataBinding.rvTasks.adapter = adapter
    }

    private fun buildMockTasks(): List<TeamLeaderTask> = listOf(
        TeamLeaderTask(
            id = 1,
            title = "تحليل الهوية الحالية والمنافسين",
            assigneeName = "فهد العتيبي",
            assigneeInitials = "FA",
            avatarColorRes = R.color.agence_blue,
            dueDate = "غداً · ٣٠ يوليو",
            status = LeaderTaskStatus.IN_PROGRESS
        ),
        TeamLeaderTask(
            id = 2,
            title = "عرض المفاهيم الأولية",
            assigneeName = "نجم للتسويق",
            assigneeInitials = "NA",
            avatarColorRes = R.color.agence_orange,
            dueDate = "٢٥ يوليو",
            status = LeaderTaskStatus.DONE
        ),
        TeamLeaderTask(
            id = 3,
            title = "تصميم الهوية البصرية النهائية",
            assigneeName = "أثر ستوديو",
            assigneeInitials = "AS",
            avatarColorRes = R.color.agence_green,
            dueDate = "متأخرة يومان",
            status = LeaderTaskStatus.LATE
        ),
        TeamLeaderTask(
            id = 4,
            title = "جلسة المراجعة النهائية مع العميل",
            assigneeName = "فهد العتيبي",
            assigneeInitials = "FA",
            avatarColorRes = R.color.agence_blue,
            dueDate = "٣ أغسطس",
            status = LeaderTaskStatus.IN_PROGRESS
        )
    )

    private fun setupClicks() {
        mViewDataBinding.ivBack.setOnClickListener {
            try {
                mViewDataBinding.root.findNavController().popBackStack()
            } catch (e: Exception) {
                Log.e("TeamLeaderPanel", "popBack failed", e)
            }
        }

        mViewDataBinding.btnAddTask.setOnClickListener {
            try {
                val sheet = com.ksa.agenceCompany.ui.dialog.NewTaskBottomSheetFragment()
                sheet.show(parentFragmentManager, "NewTaskSheet")
            } catch (e: Exception) {
                Log.e("TeamLeaderPanel", "Failed to open new task sheet", e)
                Toast.makeText(requireContext(), "تعذر فتح إضافة المهمة", Toast.LENGTH_SHORT).show()
            }
        }

        mViewDataBinding.tabTasks.setOnClickListener { }

        mViewDataBinding.tabMembers.setOnClickListener {
            try {
                mViewDataBinding.root.findNavController()
                    .navigate(R.id.memberPermissionsFragment)
            } catch (e: Exception) {
                Log.e("TeamLeaderPanel", "Failed to open members", e)
                Toast.makeText(requireContext(), "تعذر فتح الأعضاء", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }
}
