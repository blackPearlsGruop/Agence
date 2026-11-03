package com.ksa.agence.member.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agence.R
import com.ksa.agence.databinding.ItemNewUserChatBinding
import com.ksa.agence.entity.ListChatUser
import com.ksa.agence.member.common.util.Utilities.Companion.onLoadImageFromUrl
import com.ksa.agence.member.interfaces.Chat

/** Member conversations list, same row design as the client side. */
class ListChatAdapter(
    var context: Activity,
    var listData: List<ListChatUser>,
    var chat: Chat
) : RecyclerView.Adapter<ListChatAdapter.ViewHolder?>() {

    inner class ViewHolder(var binding: ItemNewUserChatBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Colored initial avatar, deterministic per name (same palette as the client side)
    private val avatarColors = listOf(
        Pair("#DDE3FF", "#2505ED"),
        Pair("#FDE8D2", "#E96D07"),
        Pair("#E8DDFB", "#6E2EC7"),
        Pair("#FDE0E6", "#C72E5B"),
        Pair("#FFEEC2", "#B88600")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemNewUserChatBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_new_user_chat, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = listData[position]

        holder.binding.tvNameUser.text = model.nameUser
        holder.binding.tvCategoryBadge.text = model.categoryName
        holder.binding.tvOrderNo.text = model.orderNumber

        if (model.imageUser.isBlank()) {
            holder.binding.ivUser.visibility = View.GONE
            holder.binding.viewAvatarBg.visibility = View.VISIBLE
            holder.binding.tvAvatarInitial.visibility = View.VISIBLE

            val name = model.nameUser
            val initial = name.trim().firstOrNull()?.uppercase() ?: "?"
            val (bg, fg) = avatarColors[Math.abs(name.hashCode()) % avatarColors.size]
            holder.binding.viewAvatarBg.background.mutate().setTint(android.graphics.Color.parseColor(bg))
            holder.binding.tvAvatarInitial.text = initial
            holder.binding.tvAvatarInitial.setTextColor(android.graphics.Color.parseColor(fg))
        } else {
            holder.binding.viewAvatarBg.visibility = View.GONE
            holder.binding.tvAvatarInitial.visibility = View.GONE
            holder.binding.ivUser.visibility = View.VISIBLE
            onLoadImageFromUrl(context, model.imageUser, holder.binding.ivUser)
        }

        holder.itemView.setOnClickListener {
            chat.clickItemChat(
                model.idUser,
                model.orderNumber,
                model.categoryName,
                model.imageUser,
                model.nameUser,
                model.idOrder
            )
        }
    }

    override fun getItemCount(): Int = listData.size
}
