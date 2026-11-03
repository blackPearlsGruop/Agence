package com.ksa.agence.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agence.R
import com.ksa.agence.common.util.Utilities.Companion.onLoadImageFromUrl
import com.ksa.agence.databinding.ItemNewUserChatBinding
import com.ksa.agence.entity.AllListChatCompany
import com.ksa.agence.interfaces.Chat

class ListChatAdapter(
    var context: Activity, var listData: List<AllListChatCompany>, var chat: Chat
) : RecyclerView.Adapter<ListChatAdapter.ViewHolder?>() {

    inner class ViewHolder(var binding: ItemNewUserChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    // Same colored-initial-avatar palette as the Figma messages list design.
    // Deterministic per name so the same company always gets the same color,
    // and automatically stays out of the way once a real photo is set.
    private val avatarColors = listOf(
        Pair("#DDE3FF", "#2505ED"),
        Pair("#FDE8D2", "#E96D07"),
        Pair("#DAF2E4", "#1A8A55"),
        Pair("#FDE0E6", "#C72E5B"),
        Pair("#E8DDFB", "#6E2EC7"),
        Pair("#FFEEC2", "#B88600")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemNewUserChatBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_new_user_chat, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = listData[position]

        holder.binding.tvNameUser.text = model.nameCompany
        holder.binding.tvCategoryBadge.text = model.categoryName
        holder.binding.tvOrderNo.text = model.orderNumber

        if (model.imageCompany.isNullOrBlank()) {
            // No real photo yet: show the colored-initial fallback.
            holder.binding.ivUser.visibility = View.GONE
            holder.binding.viewAvatarBg.visibility = View.VISIBLE
            holder.binding.tvAvatarInitial.visibility = View.VISIBLE

            val name = model.nameCompany ?: ""
            val initial = name.trim().firstOrNull()?.uppercase() ?: "?"
            val (bg, fg) = avatarColors[Math.abs(name.hashCode()) % avatarColors.size]
            holder.binding.viewAvatarBg.background.mutate().setTint(android.graphics.Color.parseColor(bg))
            holder.binding.tvAvatarInitial.text = initial
            holder.binding.tvAvatarInitial.setTextColor(android.graphics.Color.parseColor(fg))
        } else {
            // Real photo available: show it, hide the initial fallback.
            holder.binding.viewAvatarBg.visibility = View.GONE
            holder.binding.tvAvatarInitial.visibility = View.GONE
            holder.binding.ivUser.visibility = View.VISIBLE
            onLoadImageFromUrl(context, model.imageCompany, holder.binding.ivUser)
        }

        holder.itemView.setOnClickListener {
            chat.clickItemChat(
                model.idCompany,
                model.orderNumber,
                model.categoryName,
                model.imageCompany,
                model.nameCompany,
                model.idOrder
            )
        }
    }

    override fun getItemCount(): Int = listData.size
}
