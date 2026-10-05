package com.ksa.agenceCompany.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agenceCompany.R

enum class PermissionLevel {
    LEADER, EDITOR, VIEWER
}

data class PermissionMember(
    val id: Int,
    val name: String,
    val role: String,
    val initials: String,
    val avatarColorRes: Int,
    val level: PermissionLevel
)

class PermissionMemberAdapter(
    private val members: List<PermissionMember>,
    private val onMemberClick: (PermissionMember) -> Unit
) : RecyclerView.Adapter<PermissionMemberAdapter.MemberViewHolder>() {

    inner class MemberViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val root: LinearLayout = view.findViewById(R.id.item_root)
        val tvInitials: TextView = view.findViewById(R.id.tv_initials)
        val tvName: TextView = view.findViewById(R.id.tv_name)
        val tvRole: TextView = view.findViewById(R.id.tv_role)
        val tvBadge: TextView = view.findViewById(R.id.tv_badge)
        val ivAvatarBg: ImageView = view.findViewById(R.id.iv_avatar_bg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_permission_member, parent, false)
        return MemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val member = members[position]
        val ctx = holder.view.context

        holder.tvInitials.text = member.initials
        holder.tvName.text = member.name
        holder.tvRole.text = member.role

        holder.ivAvatarBg.imageTintList = ColorStateList.valueOf(
            ctx.resources.getColor(member.avatarColorRes, null)
        )

        when (member.level) {
            PermissionLevel.LEADER -> {
                holder.tvBadge.text = "قائد الفريق"
                holder.tvBadge.setBackgroundResource(R.drawable.bg_badge_leader)
            }
            PermissionLevel.EDITOR -> {
                holder.tvBadge.text = "محرر"
                holder.tvBadge.setBackgroundResource(R.drawable.bg_badge_editor)
            }
            PermissionLevel.VIEWER -> {
                holder.tvBadge.text = "مشاهد"
                holder.tvBadge.setBackgroundResource(R.drawable.bg_badge_viewer)
            }
        }

        holder.root.setOnClickListener { onMemberClick(member) }
    }

    override fun getItemCount(): Int = members.size
}
