package com.ksa.agenceCompany.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.databinding.ItemFeaturesBinding
import com.ksa.agenceCompany.entity.allSubscriptionResponse.Features

class FeaturesPlanAdapter(
    var context: Activity,
    var listData: List<Features>
) : RecyclerView.Adapter<FeaturesPlanAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: ItemFeaturesBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemFeaturesBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemFeaturesBinding= DataBindingUtil.inflate(
            layoutInflater, R.layout.item_features, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        holder.itemView.startAnimation(animation)
        var model = listData[position]

        holder.binding.tvTitle.text = model.title


    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}