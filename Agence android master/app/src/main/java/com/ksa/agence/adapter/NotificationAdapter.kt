package com.ksa.agence.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agence.R
import com.ksa.agence.common.util.Utilities.Companion.onLoadImageFromUrl
import com.ksa.agence.databinding.ItemAllOrderBinding
import com.ksa.agence.databinding.ItemNotifactionBinding
import com.ksa.agence.entity.allOrdersResponse.DataAllOrdersResponse
import com.ksa.agence.entity.notificationResponse.DataNotificationResponse

class NotificationAdapter(
    var context: Activity,
    var listData: List<DataNotificationResponse>,
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: ItemNotifactionBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemNotifactionBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemNotifactionBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_notifaction, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
       // holder.itemView.startAnimation(animation)
        val model = listData[position]

        // Log the model to check for null values
        Log.d("AllOrdersAdapter", "Binding view holder for position: $position, model: $model")

        holder.binding.tvTitle.text = model.title
        holder.binding.tvBody.text = model.description
        holder.binding.tvTime.text = model.created_at_for_humans
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}