package com.ksa.agenceCompany.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.databinding.ItemTeamTaskBinding

/**
 * TeamTaskAdapter — used by TeamProjectFragment. Each row is a task with a
 * checkbox, task text, assignee initials, and edit/delete icons.
 */
data class ProjectTask(
    var task: String,
    val assignee: String,
    var done: Boolean = false
)

class TeamTaskAdapter(
    private val items: MutableList<ProjectTask>,
    private val onToggle: (Int) -> Unit,
    private val onEdit: (Int) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<TeamTaskAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemTeamTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTeamTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val ctx = holder.itemView.context

        holder.binding.tvTaskText.text = item.task
        holder.binding.tvTaskAssignee.text = item.assignee

        // Checkbox visuals
        if (item.done) {
            holder.binding.ivTaskCheck.setBackgroundResource(R.drawable.bg_task_checkbox_done)
            holder.binding.ivTaskCheck.setImageResource(R.drawable.ic_check_single)
            holder.binding.tvTaskText.paintFlags =
                holder.binding.tvTaskText.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.tvTaskText.alpha = 0.5f
        } else {
            holder.binding.ivTaskCheck.setBackgroundResource(R.drawable.bg_task_checkbox_border)
            holder.binding.ivTaskCheck.setImageDrawable(null)
            holder.binding.tvTaskText.paintFlags =
                holder.binding.tvTaskText.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.binding.tvTaskText.alpha = 1f
        }

        holder.binding.ivTaskCheck.setOnClickListener { onToggle(holder.adapterPosition) }
        holder.itemView.setOnClickListener { onToggle(holder.adapterPosition) }
        holder.binding.ivTaskEdit.setOnClickListener { onEdit(holder.adapterPosition) }
        holder.binding.ivTaskDelete.setOnClickListener { onDelete(holder.adapterPosition) }
    }

    override fun getItemCount(): Int = items.size
}
