package com.ksa.agence.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemNewUserChatBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_new_user_chat, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = listData[position]

        onLoadImageFromUrl(
            context, model.imageCompany, holder.binding.ivUser
        )
        holder.binding.tvNameUser.text = model.nameCompany

        holder.itemView.setOnClickListener {
            chat.clickItemChat(
                model.idCompany,
                model.orderNumber,
                model.categoryName,
                model.imageCompany,
                model.nameCompany,
                model.idOrder // تمرير idOrder هنا
            )
        }
    }

    override fun getItemCount(): Int = listData.size
}
