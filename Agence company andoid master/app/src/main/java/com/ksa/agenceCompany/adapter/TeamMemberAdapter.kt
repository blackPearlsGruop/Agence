package com.ksa.agenceCompany.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.databinding.ItemTeamMemberBinding

data class TeamMember(
    val name: String,
    val specialty: String,
    val initials: String,
    var isLead: Boolean = false
)

class TeamMemberAdapter(
    private val items: MutableList<TeamMember>,
    private val onMenuClick: (TeamMember, Int, android.view.View) -> Unit,
    private val onDeleteClick: (Int) -> Unit,
    private val onAvatarClick: (TeamMember) -> Unit
) : RecyclerView.Adapter<TeamMemberAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemTeamMemberBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTeamMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvMemberAvatar.text = item.initials
        holder.binding.tvMemberName.text = item.name
        holder.binding.tvMemberSpecialty.text = item.specialty

        if (item.isLead) {
            holder.binding.tvMemberRole.text = holder.itemView.context.getString(R.string.team_lead_role)
            holder.binding.tvMemberRole.setBackgroundResource(R.drawable.bg_pill_orange_light)
            holder.binding.tvMemberRole.setTextColor(holder.itemView.context.getColor(R.color.secondary))
        } else {
            holder.binding.tvMemberRole.text = holder.itemView.context.getString(R.string.member_role)
            holder.binding.tvMemberRole.setBackgroundResource(R.drawable.bg_pill_blue_light)
            holder.binding.tvMemberRole.setTextColor(holder.itemView.context.getColor(R.color.primary))
        }

        holder.binding.tvMemberAvatar.setOnClickListener { onAvatarClick(item) }
        holder.binding.btnMemberMenu.setOnClickListener { onMenuClick(item, holder.adapterPosition, it) }
        holder.binding.btnMemberDelete.setOnClickListener { onDeleteClick(holder.adapterPosition) }
    }

    override fun getItemCount(): Int = items.size
}
