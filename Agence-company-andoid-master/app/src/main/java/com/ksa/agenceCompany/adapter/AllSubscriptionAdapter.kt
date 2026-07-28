package com.ksa.agenceCompany.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.databinding.ItemSubscriptionsBinding
import com.ksa.agenceCompany.entity.allSubscriptionResponse.DataAllSubscriptionResponse
import com.ksa.agenceCompany.interfaces.Home

class AllSubscriptionAdapter(
    var context: Activity,
    var listData: List<DataAllSubscriptionResponse>, var home: Home
) : RecyclerView.Adapter<AllSubscriptionAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: ItemSubscriptionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemSubscriptionsBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemSubscriptionsBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_subscriptions, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        holder.itemView.startAnimation(animation)
        var model = listData[position]

        holder.binding.tvDuration.text = model.title
        holder.binding.tvNextPayment.text = model.plan_end_at!!
        holder.binding.tvTotal.text =
            "" + model.price + "" + context.getString(R.string.r_s) + " / " + context.getString(R.string.year)




    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}