package com.ksa.agenceCompany.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.common.util.Utilities.Companion.onLoadImageFromUrl
import com.ksa.agenceCompany.databinding.ItemAllOrderBinding
import com.ksa.agenceCompany.entity.allOrdersResponse.DataAllOrdersResponse
import com.ksa.agenceCompany.interfaces.Order

class AllOrdersAdapter(
    var context: Activity,
    var listData: List<DataAllOrdersResponse>, var order: Order
) : RecyclerView.Adapter<AllOrdersAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: ItemAllOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemAllOrderBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemAllOrderBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_all_order, parent, false
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
                holder.binding.tvStatus.text = context.getString(R.string.in_progress)
                holder.binding.tvStatus.setBackgroundResource(R.drawable.bg_pill_blue_light)
                holder.binding.tvStatus.setTextColor(context.getColor(R.color.primary))
            }
            "completed" -> {
                holder.binding.tvStatus.text = context.getString(R.string.delivered)
                holder.binding.tvStatus.setBackgroundResource(R.drawable.bg_pill_green_light)
                holder.binding.tvStatus.setTextColor(context.getColor(R.color.green))
            }
            "canceled" -> {
                holder.binding.tvStatus.text = context.getString(R.string.canceled)
                holder.binding.tvStatus.setBackgroundResource(R.drawable.bg_circle_light_grey)
                holder.binding.tvStatus.setTextColor(context.getColor(R.color.agence_muted))
            }
        }

        // Active tab (pending/in-progress) shows a chat shortcut, finished tab shows a checkmark
        if (model.order_status == "pending" || model.order_status == "in-progress") {
            holder.binding.ivAction.setImageResource(R.drawable.icon_chat)
            holder.binding.ivAction.setBackgroundResource(R.drawable.bg_circle_light_grey)
        } else {
            holder.binding.ivAction.setImageResource(R.drawable.icon_check_white_small)
            holder.binding.ivAction.setBackgroundResource(R.drawable.bg_circle_blue_light)
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
