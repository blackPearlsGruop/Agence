package com.ksa.agence.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agence.R
import com.ksa.agence.common.util.Utilities.Companion.onLoadImageFromUrl
import com.ksa.agence.databinding.ItemAllOrderBinding
import com.ksa.agence.entity.allOrdersResponse.DataAllOrdersResponse
import com.ksa.agence.interfaces.Order

class AllOrdersAdapter(
    var context: Activity,
    var listData: List<DataAllOrdersResponse>,val order: Order
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

        model.company?.let { company ->
            onLoadImageFromUrl(context, company.company_logo, holder.binding.ivLogoCompany)
            holder.binding.tvAddressCompany.text = company.address ?: "No Address"
            holder.binding.tvNameCompany.text = model.company?.title ?: "No Company"

        }

        holder.binding.tvNameCategory.text = model.category?.title ?: "No Category"
        holder.binding.tvNoOrder.text = model.order_number ?: "No Order Number"
        holder.binding.tvPriceService.text = "${model.price ?: 0} ${context.getString(R.string.r_s)}"

      //  in-progress,completed,canceled,pending
        if (model.order_status=="in-progress" || model.order_status=="pending")
        {
            holder.binding.btnReorder.visibility=View.GONE

        }
        else{
            holder.binding.btnReorder.visibility=View.VISIBLE
        }

        if (model.accepted_offer !=null)
        {

            holder.binding.constraintDataCompany.visibility=View.VISIBLE

        }
        else
        {
            holder.binding.constraintDataCompany.visibility=View.GONE
            holder.binding.btnShow.setText(context.getString(R.string.there_are_no_offers))
        }


        holder.binding.btnShow.setOnClickListener {
            order.clickItemOrder(model.id!!)
        }

        holder.binding.btnReorder.setOnClickListener {
            order.clickItemReorder(model.id!!)
        }


    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}