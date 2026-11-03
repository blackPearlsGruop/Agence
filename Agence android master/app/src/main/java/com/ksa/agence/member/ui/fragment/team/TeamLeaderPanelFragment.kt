package com.ksa.agence.member.ui.fragment.team

import com.ksa.agence.common.sampleText
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ksa.agence.R
import com.ksa.agence.member.adapter.LeaderTaskStatus
import com.ksa.agence.member.adapter.TeamLeaderTask
import com.ksa.agence.member.adapter.TeamLeaderTaskAdapter
import com.ksa.agence.member.base.BaseFragment
import com.ksa.agence.databinding.FragmentTeamLeaderPanelBinding

class TeamLeaderPanelFragment : BaseFragment<FragmentTeamLeaderPanelBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_team_leader_panel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    private fun setupTasksList() {
        val tasks = buildMockTasks()

        val adapter = TeamLeaderTaskAdapter(
            tasks = tasks,
            onTaskClick = { task ->
                Toast.makeText(requireContext(), getString(R.string.msg_task_title, task.title), Toast.LENGTH_SHORT).show()
            },
            onMenuClick = { _ ->
                Toast.makeText(requireContext(), getString(R.string.msg_task_options), Toast.LENGTH_SHORT).show()
            }
        )

        mViewDataBinding.rvTasks.layoutManager = LinearLayoutManager(requireContext())
        mViewDataBinding.rvTasks.adapter = adapter
    }

    private fun buildMockTasks(): List<TeamLeaderTask> = listOf(
        TeamLeaderTask(
            id = 1,
            title = sampleText("تحليل الهوية الحالية والمنافسين"),
            assigneeName = sampleText("فهد العتيبي"),
            assigneeInitials = "FA",
            avatarColorRes = R.color.agence_blue,
            dueDate = sampleText("غداً · ٣٠ يوليو"),
            status = LeaderTaskStatus.IN_PROGRESS
        ),
        TeamLeaderTask(
            id = 2,
            title = sampleText("عرض المفاهيم الأولية"),
            assigneeName = sampleText("نجم للتسويق"),
            assigneeInitials = "NA",
            avatarColorRes = R.color.member_agence_orange,
            dueDate = sampleText("٢٥ يوليو"),
            status = LeaderTaskStatus.DONE
        ),
        TeamLeaderTask(
            id = 3,
            title = sampleText("تصميم الهوية البصرية النهائية"),
            assigneeName = sampleText("أثر ستوديو"),
            assigneeInitials = "AS",
            avatarColorRes = R.color.member_agence_green,
            dueDate = sampleText("متأخرة يومان"),
            status = LeaderTaskStatus.LATE
        ),
        TeamLeaderTask(
            id = 4,
            title = sampleText("جلسة المراجعة النهائية مع العميل"),
            assigneeName = sampleText("فهد العتيبي"),
            assigneeInitials = "FA",
            avatarColorRes = R.color.agence_blue,
            dueDate = sampleText("٣ أغسطس"),
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
                val sheet = com.ksa.agence.member.ui.dialog.NewTaskBottomSheetFragment()
                sheet.show(parentFragmentManager, "NewTaskSheet")
            } catch (e: Exception) {
                Log.e("TeamLeaderPanel", "Failed to open new task sheet", e)
                Toast.makeText(requireContext(), getString(R.string.msg_open_failed), Toast.LENGTH_SHORT).show()
            }
        }

        mViewDataBinding.tabTasks.setOnClickListener { }

        mViewDataBinding.tabMembers.setOnClickListener {
            try {
                mViewDataBinding.root.findNavController()
                    .navigate(R.id.memberPermissionsFragment)
            } catch (e: Exception) {
                Log.e("TeamLeaderPanel", "Failed to open members", e)
                Toast.makeText(requireContext(), getString(R.string.msg_open_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }
}