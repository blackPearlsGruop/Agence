package com.ksa.agence.member.adapter

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agence.R
import com.ksa.agence.databinding.ItemActivityLogBinding
import com.ksa.agence.databinding.ItemTimeHeaderBinding

class ActivityLogAdapter(
    private val items: List<Any>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_ACTIVITY = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is String) TYPE_HEADER else TYPE_ACTIVITY
    }

    inner class HeaderViewHolder(private val binding: ItemTimeHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String) {
            binding.tvHeader.text = title
        }
    }

    inner class ActivityViewHolder(private val binding: ItemActivityLogBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ActivityLog) {
            binding.tvAvatar.text = item.memberInitials
            binding.tvAvatar.background?.setColorFilter(
                binding.root.context.getColor(item.colorRes),
                PorterDuff.Mode.SRC_IN
            )

            val fullText = "${item.memberName} ${item.action} ${item.subject}"
            binding.tvActionText.text = fullText

            binding.tvTime.text = item.timeAgo

            val iconRes = when (item.type) {
                ActivityType.TASK -> android.R.drawable.ic_menu_agenda
                ActivityType.MEMBER -> android.R.drawable.ic_menu_myplaces
                ActivityType.PAYMENT -> android.R.drawable.ic_menu_sort_by_size
                ActivityType.FILE -> android.R.drawable.ic_menu_upload
            }
            binding.ivTypeIcon.setImageResource(iconRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            HeaderViewHolder(ItemTimeHeaderBinding.inflate(inflater, parent, false))
        } else {
            ActivityViewHolder(ItemActivityLogBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(items[position] as String)
            is ActivityViewHolder -> holder.bind(items[position] as ActivityLog)
        }
    }

    override fun getItemCount(): Int = items.size
}
