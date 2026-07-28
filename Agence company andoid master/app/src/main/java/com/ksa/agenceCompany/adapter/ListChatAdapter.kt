package com.ksa.agenceCompany.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.common.util.Utilities.Companion.onLoadImageFromUrl
import com.ksa.agenceCompany.databinding.ItemNewUserChatBinding
import com.ksa.agenceCompany.entity.ListChatUser
import com.ksa.agenceCompany.interfaces.Chat

class ListChatAdapter(
    var context: Activity,
    var listData: List<ListChatUser>,
    var chat: Chat
) : RecyclerView.Adapter<ListChatAdapter.ViewHolder?>() {

    inner class ViewHolder(binding: ItemNewUserChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemNewUserChatBinding = binding
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
            context, model.imageUser, holder.binding.ivUser
        )
        holder.binding.tvNameUser.text = model.nameUser
        Log.d("onBindViewHolder:", model.imageUser)

        holder.itemView.setOnClickListener {
            chat.clickItemChat(
                model.idUser,
                model.orderNumber,
                model.categoryName,
                model.imageUser,
                model.nameUser,
                model.idOrder // تمرير idOrder هنا
            )
        }
    }

    override fun getItemCount(): Int = listData.size
}
