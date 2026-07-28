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
import com.ksa.agence.databinding.ItemServicesBinding
import com.ksa.agence.entity.showCompaniesResponse.OfferShowCompaniesResponse
import com.ksa.agence.entity.showCompaniesResponse.ServiceShowCompaniesResponse
import com.ksa.agence.interfaces.Services

class ServiceCompanyAdapter(
    var context: Activity,
    var listData: List<ServiceShowCompaniesResponse>,var services: Services
) : RecyclerView.Adapter<ServiceCompanyAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: ItemServicesBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemServicesBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemServicesBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_services, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        holder.itemView.startAnimation(animation)
        var model = listData[position]
        onLoadImageFromUrl(
            context, model.images!!.get(0), holder.binding.ivLogoService
        )
        holder.binding.tvNameService.text = model.title
        holder.binding.tvPriceService.text = ""+model.price+" "+context.getString(R.string.r_s)

        holder.binding.btnSend.setOnClickListener {
            services.clickItemServices(model.id!!)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}