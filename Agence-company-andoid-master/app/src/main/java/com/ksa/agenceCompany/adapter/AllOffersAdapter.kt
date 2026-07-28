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
import com.ksa.agenceCompany.databinding.ItemAllServiceBinding
import com.ksa.agenceCompany.databinding.ItemOfferBinding
import com.ksa.agenceCompany.entity.categoriesResponse.DataCategoriesResponse
import com.ksa.agenceCompany.interfaces.Home

class AllOffersAdapter(
    var context: Activity,
    var listData: List<DataCategoriesResponse>, var home: Home
) : RecyclerView.Adapter<AllOffersAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: ItemOfferBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemOfferBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemOfferBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_offer, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        // holder.itemView.startAnimation(animation)
        var model = listData[position]


        holder.binding.titleShowOfTheDay.text = model.title
        holder.binding.titleDetails.text = model.description
        holder.binding.tvPrice.text =""+ model.price +" "+context.getString(R.string.r_s)


        holder.binding.btnDetails.setOnClickListener {
            home.clickItemShowService(model.id!!)
        }

    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}