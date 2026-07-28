package com.ksa.agenceCompany.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.common.util.Utilities.Companion.onLoadImageFromUrl
import com.ksa.agenceCompany.databinding.ItemAnOpportunityBinding
import com.ksa.agenceCompany.entity.allOpportunitiesResponse.DataAllOpportunitiesResponse
import com.ksa.agenceCompany.interfaces.Home

class AllOpportunitiesAdapter(
    var context: Activity,
    var listData: List<DataAllOpportunitiesResponse>, var home: Home
) : RecyclerView.Adapter<AllOpportunitiesAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: ItemAnOpportunityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemAnOpportunityBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemAnOpportunityBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_an_opportunity, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        holder.itemView.startAnimation(animation)
        var model = listData[position]

        onLoadImageFromUrl(context, model.logo, holder.binding.ivLogoCompany)
        holder.binding.tvNameCompany.text = model.company_name
        holder.binding.tvNameCategory.text = model.title
        holder.binding.tvDic.text = model.description


        holder.binding.btnShow.setOnClickListener {
            home.clickItemOpportunitiesDetails(model.id)
        }


    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}