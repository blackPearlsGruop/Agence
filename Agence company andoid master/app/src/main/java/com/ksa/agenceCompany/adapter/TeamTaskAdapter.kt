package com.ksa.agenceCompany.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.databinding.ItemTeamTaskBinding

data class ProjectTask(
    var task: String,
    val assigneeInitials: String,
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
        holder.binding.tvTaskAvatar.text = item.assigneeInitials
        holder.binding.tvTaskText.text = item.task

        if (item.done) {
            holder.binding.btnTaskToggle.setBackgroundResource(R.drawable.bg_task_checkbox_done)
            holder.binding.ivTaskCheck.visibility = android.view.View.VISIBLE
            holder.binding.tvTaskText.paintFlags =
                holder.binding.tvTaskText.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.tvTaskText.setTextColor(holder.itemView.context.getColor(R.color.agence_muted))
        } else {
            holder.binding.btnTaskToggle.setBackgroundResource(R.drawable.bg_task_checkbox_border)
            holder.binding.ivTaskCheck.visibility = android.view.View.GONE
            holder.binding.tvTaskText.paintFlags =
                holder.binding.tvTaskText.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.binding.tvTaskText.setTextColor(holder.itemView.context.getColor(R.color.agence_black))
        }

        holder.binding.btnTaskToggle.setOnClickListener { onToggle(holder.adapterPosition) }
        holder.binding.btnTaskEdit.setOnClickListener { onEdit(holder.adapterPosition) }
        holder.binding.btnTaskDelete.setOnClickListener { onDelete(holder.adapterPosition) }
    }

    override fun getItemCount(): Int = items.size
}
