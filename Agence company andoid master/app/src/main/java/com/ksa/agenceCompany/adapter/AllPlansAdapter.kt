package com.ksa.agenceCompany.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.databinding.ItemPlansBinding
import com.ksa.agenceCompany.entity.allSubscriptionResponse.DataAllSubscriptionResponse
import com.ksa.agenceCompany.interfaces.Home

class AllPlansAdapter(
    var context: Activity,
    var listData: List<DataAllSubscriptionResponse>,val home: Home
) : RecyclerView.Adapter<AllPlansAdapter.ViewHolder?>() {

    lateinit var featuresPlanAdapter: FeaturesPlanAdapter


    inner class ViewHolder(binding: ItemPlansBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemPlansBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemPlansBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_plans, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        holder.itemView.startAnimation(animation)
        var model = listData[position]

        holder.binding.tvTotal.text =
            "" + model.price + "" + context.getString(R.string.r_s) + " / " + context.getString(R.string.year)


        holder.binding.tvDuration.text = model.title
        featuresPlanAdapter = FeaturesPlanAdapter(context, model.features!!)
        holder.binding.rvFeatures.adapter = featuresPlanAdapter
        featuresPlanAdapter.notifyDataSetChanged()



        holder.binding.btnDetails.setOnClickListener {
            home.clickItemOpportunitiesDetails(model.id!!)
        }


    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}