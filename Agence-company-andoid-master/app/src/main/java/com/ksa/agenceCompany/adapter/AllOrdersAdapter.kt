package com.ksa.agenceCompany.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.common.util.Utilities.Companion.onLoadImageFromUrl
import com.ksa.agenceCompany.databinding.ItemAllOrderBinding
import com.ksa.agenceCompany.entity.allOrdersResponse.DataAllOrdersResponse
import com.ksa.agenceCompany.interfaces.Order

class AllOrdersAdapter(
    var context: Activity,
    var listData: List<DataAllOrdersResponse>,var order:Order
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
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
       // holder.itemView.startAnimation(animation)
        val model = listData[position]

        // Log the model to check for null values
        Log.d("AllOrdersAdapter", "Binding view holder for position: $position, model: $model")

        model.user?.let { company ->
            onLoadImageFromUrl(context, company.profile_image, holder.binding.ivLogoUser)

        }

        holder.binding.tvNameUser.text = model.user?.name
        holder.binding.tvNameCategory.text = model.category?.title ?: "No Category"
        holder.binding.tvNoOrder.text = model.order_number ?: "No Order Number"
        holder.binding.tvDate.text = model.created_at
        holder.binding.tvTime.text = context.getString(R.string.duration_of_completion)+" : "+model.order_duration_in_days +" "+context.getString(R.string.day)
        holder.binding.tvDicOrder.text = model.description

        // service,offer,quick,private
        if (model.order_type =="service")
        {
            holder.binding.tvTypeOrder.text = context.getString(R.string.the_service)

        }
        else   if (model.order_type =="offer")
        {
            holder.binding.tvTypeOrder.text = context.getString(R.string.offers)

        }
        else   if (model.order_type =="quick")
        {
            holder.binding.tvTypeOrder.text = context.getString(R.string.quick_order)

        }
        else   if (model.order_type =="private")
        {
            holder.binding.tvTypeOrder.text = context.getString(R.string.privates)

        }

//        holder.binding.tvPriceService.text = "${model.price ?: 0} ${context.getString(R.string.r_s)}"

      //  in-progress,completed,canceled,pending
        if ( model.order_status=="pending")
        {
            holder.binding.layoutStatus.visibility=View.GONE

        }
        else  if (model.order_status=="in-progress" ){
            holder.binding.layoutStatus.visibility=View.VISIBLE
            holder.binding.tvStatus.text=context.getString(R.string.in_progress)
            holder.binding.ivAction.setImageResource(R.drawable.icon_complet)
        }
        else  if (model.order_status=="completed"){
            holder.binding.layoutStatus.visibility=View.VISIBLE
            holder.binding.tvStatus.text=context.getString(R.string.completed)
            holder.binding.ivAction.setImageResource(R.drawable.icon_complet)
        }
        else  if (model.order_status=="canceled"){
            holder.binding.tvStatus.text=context.getString(R.string.canceled)
            holder.binding.ivAction.setImageResource(R.drawable.icon_reject)
            holder.binding.layoutStatus.visibility=View.VISIBLE

        }

        if (model.has_offers!! ==true)
        {

            holder.binding.constraintDataCompany.visibility=View.VISIBLE

        }
        else
        {
            holder.binding.constraintDataCompany.visibility=View.GONE
     //       holder.binding.btnShow.setText(context.getString(R.string.there_are_no_offers))
        }


        holder.itemView.setOnClickListener {
            order.clickItemOrder(model.id!!)
        }
//
//        holder.binding.btnReorder.setOnClickListener {
//            order.clickItemReorder(model.id!!)
//        }


    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}