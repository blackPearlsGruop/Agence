package com.ksa.agence.member.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agence.R
import com.ksa.agence.member.common.util.Utilities.Companion.onLoadImageFromUrl
import com.ksa.agence.databinding.MemberItemAllOrderBinding
import com.ksa.agence.member.entity.allOrdersResponse.DataAllOrdersResponse
import com.ksa.agence.member.interfaces.Order

class AllOrdersAdapter(
    var context: Activity,
    var listData: List<DataAllOrdersResponse>, var order: Order
) : RecyclerView.Adapter<AllOrdersAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: MemberItemAllOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: MemberItemAllOrderBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: MemberItemAllOrderBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.member_item_all_order, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = listData[position]

        model.user?.profile_image?.let { image ->
            onLoadImageFromUrl(context, image, holder.binding.ivLogoUser)
        }

        holder.binding.tvNameUser.text = model.user?.name
        holder.binding.tvNameCategory.text = model.category?.title ?: model.description
        holder.binding.tvNoOrder.text = model.order_number ?: ""
        holder.binding.tvDate.text = model.created_at

        holder.binding.tvPrice.text = model.price?.toString() ?: ""

        // FIGMA: pending → "under review" (orange), in-progress (blue), completed → "delivered" (green)
        when (model.order_status) {
            "pending" -> {
                holder.binding.tvStatus.text = context.getString(R.string.under_review)
                holder.binding.tvStatus.setBackgroundResource(R.drawable.bg_pill_orange_light)
                holder.binding.tvStatus.setTextColor(context.getColor(R.color.secondary))
            }
            "in-progress" -> {
                holder.binding.tvStatus.text = context.getString(R.string.member_in_progress)
                holder.binding.tvStatus.setBackgroundResource(R.drawable.bg_pill_blue_light)
                holder.binding.tvStatus.setTextColor(context.getColor(R.color.primary))
            }
            "completed" -> {
                holder.binding.tvStatus.text = context.getString(R.string.delivered)
                holder.binding.tvStatus.setBackgroundResource(R.drawable.bg_pill_green_light)
                holder.binding.tvStatus.setTextColor(context.getColor(R.color.green))
            }
            "canceled" -> {
                holder.binding.tvStatus.text = context.getString(R.string.member_canceled)
                holder.binding.tvStatus.setBackgroundResource(R.drawable.bg_circle_light_grey)
                holder.binding.tvStatus.setTextColor(context.getColor(R.color.agence_muted))
            }
        }

        // Active tab (pending/in-progress) shows a chat shortcut, finished tab shows a checkmark
        if (model.order_status == "pending" || model.order_status == "in-progress") {
            holder.binding.ivActionIcon.setImageResource(R.drawable.icon_chat)
            holder.binding.ivActionIcon.setColorFilter(context.getColor(R.color.agence_muted))
            holder.binding.ivActionIcon.background = null
        } else {
            holder.binding.ivActionIcon.setImageResource(R.drawable.member_icon_show_message)
            holder.binding.ivActionIcon.setColorFilter(context.getColor(R.color.primary))
            holder.binding.ivActionIcon.setBackgroundResource(R.drawable.member_bg_circle_blue_light)
        }

        holder.itemView.setOnClickListener {
            order.clickItemOrder(model.id)
        }

    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}