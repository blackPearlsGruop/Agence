package com.ksa.agenceCompany.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agenceCompany.R
import de.hdodenhof.circleimageview.CircleImageView

/**
 * TeamLeaderTaskAdapter — used by TeamLeaderPanelFragment.
 * Renamed from TeamTaskAdapter to avoid clashing with the existing simple task
 * adapter used by TeamProjectFragment.
 */
class TeamLeaderTaskAdapter(
    private val tasks: List<TeamLeaderTask>,
    private val onTaskClick: (TeamLeaderTask) -> Unit,
    private val onMenuClick: (TeamLeaderTask) -> Unit
) : RecyclerView.Adapter<TeamLeaderTaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_team_leader_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_task_title)
        private val ivAvatar: CircleImageView = itemView.findViewById(R.id.iv_assignee_avatar)
        private val tvInitials: TextView = itemView.findViewById(R.id.tv_assignee_initials)
        private val tvAssignee: TextView = itemView.findViewById(R.id.tv_assignee_name)
        private val tvDue: TextView = itemView.findViewById(R.id.tv_due_date)
        private val pill: TextView = itemView.findViewById(R.id.status_pill)
        private val ivMenu: ImageView = itemView.findViewById(R.id.iv_menu)

        fun bind(task: TeamLeaderTask) {
            val ctx = itemView.context

            tvTitle.text = task.title
            tvAssignee.text = task.assigneeName
            tvInitials.text = task.assigneeInitials
            tvDue.text = task.dueDate

            ivAvatar.setImageDrawable(null)
            ivAvatar.circleBackgroundColor = ContextCompat.getColor(ctx, task.avatarColorRes)

            when (task.status) {
                LeaderTaskStatus.IN_PROGRESS -> {
                    pill.text = "قيد التنفيذ"
                    pill.setBackgroundResource(R.drawable.bg_team_pill_progress)
                    pill.setTextColor(0xFFF58220.toInt())
                }
                LeaderTaskStatus.DONE -> {
                    pill.text = "مكتملة"
                    pill.setBackgroundResource(R.drawable.bg_team_pill_done)
                    pill.setTextColor(0xFF24BF61.toInt())
                }
                LeaderTaskStatus.LATE -> {
                    pill.text = "متأخرة"
                    pill.setBackgroundResource(R.drawable.bg_team_pill_late)
                    pill.setTextColor(0xFFE63946.toInt())
                }
            }

            itemView.setOnClickListener { onTaskClick(task) }
            ivMenu.setOnClickListener { onMenuClick(task) }
        }
    }
}

/** Data model for a task in the Team Leader Panel. */
data class TeamLeaderTask(
    val id: Int,
    val title: String,
    val assigneeName: String,
    val assigneeInitials: String,
    val avatarColorRes: Int,
    val dueDate: String,
    val status: LeaderTaskStatus
)

enum class LeaderTaskStatus { IN_PROGRESS, DONE, LATE }
