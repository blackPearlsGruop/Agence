package com.ksa.agence.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agence.R
import com.ksa.agence.common.util.Utilities.Companion.onLoadImageFromUrl
import com.ksa.agence.databinding.ItemAllOrderBinding
import com.ksa.agence.entity.allOrdersResponse.DataAllOrdersResponse
import com.ksa.agence.interfaces.Order

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

        // Every card renders the same way regardless of data completeness —
        // matches the Figma reference exactly (no special "awaiting offers"
        // treatment there). Fall back gracefully when a field is missing.
        model.company?.company_logo?.let { onLoadImageFromUrl(context, it, holder.binding.ivLogoCompany) }
        holder.binding.tvNameCompany.text = model.company?.title ?: context.getString(R.string.app_name)
        holder.binding.tvNameCategory.text = model.category?.title ?: model.description ?: ""
        holder.binding.tvNoOrder.text = model.order_number ?: ""
        holder.binding.tvDate.text = model.created_at ?: ""
        holder.binding.tvPrice.text = "${model.price ?: 0} ${context.getString(R.string.r_s)}"

        when (model.order_status) {
            "pending" -> {
                holder.binding.tvStatus.text = context.getString(R.string.under_review)
                holder.binding.tvStatus.setBackgroundResource(R.drawable.bg_status_orange)
                holder.binding.tvStatus.setTextColor(context.getColor(R.color.agence_orange))
            }
            "in-progress" -> {
                holder.binding.tvStatus.text = context.getString(R.string.in_progress)
                holder.binding.tvStatus.setBackgroundResource(R.drawable.bg_status_blue)
                holder.binding.tvStatus.setTextColor(context.getColor(R.color.agence_blue))
            }
            "completed" -> {
                holder.binding.tvStatus.text = context.getString(R.string.delivered)
                holder.binding.tvStatus.setBackgroundResource(R.drawable.bg_status_green)
                holder.binding.tvStatus.setTextColor(context.getColor(R.color.agence_green))
            }
            "canceled" -> {
                holder.binding.tvStatus.text = context.getString(R.string.canceled)
                holder.binding.tvStatus.setBackgroundResource(R.drawable.bg_status_orange)
                holder.binding.tvStatus.setTextColor(context.getColor(R.color.red))
            }
        }

        // Active orders (pending/in-progress) -> simple gray transparent chat
        // icon, no background. Finished orders (completed/canceled) -> blue
        // checkmark on a light blue circle. Same rule as the company app.
        holder.binding.ivActionIcon.visibility = View.VISIBLE
        if (model.order_status == "pending" || model.order_status == "in-progress") {
            holder.binding.ivActionIcon.setImageResource(R.drawable.icon_chat)
            holder.binding.ivActionIcon.setColorFilter(context.getColor(R.color.agence_muted))
            holder.binding.ivActionIcon.background = null
        } else {
            holder.binding.ivActionIcon.setImageResource(R.drawable.icon_show_message)
            holder.binding.ivActionIcon.setColorFilter(context.getColor(R.color.agence_blue))
            holder.binding.ivActionIcon.setBackgroundResource(R.drawable.bg_circle_blue_light)
        }

        // Reorder button: only meaningful once an order has actually finished.
        holder.binding.btnReorder.visibility =
            if (model.order_status == "completed" || model.order_status == "canceled") View.VISIBLE else View.GONE

        holder.binding.rootOrderCard.setOnClickListener {
            model.id?.let { order.clickItemOrder(it) }
        }

        holder.binding.ivActionIcon.setOnClickListener {
            order.clickItemChat(model)
        }

        holder.binding.btnReorder.setOnClickListener {
            model.id?.let { order.clickItemReorder(it) }
        }
    }

    override fun getItemCount(): Int = listData.size

    override fun getItemViewType(position: Int): Int = position
}
