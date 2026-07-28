package com.ksa.agenceCompany.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.databinding.ItemOffersCompanyBinding
import com.ksa.agenceCompany.entity.showCompaniesResponse.OfferShowCompaniesResponse
import com.ksa.agenceCompany.interfaces.Services

class OffersCompanyAdapter(
    var context: Activity,
    var listData: List<OfferShowCompaniesResponse>,var services: Services
) : RecyclerView.Adapter<OffersCompanyAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: ItemOffersCompanyBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemOffersCompanyBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemOffersCompanyBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_offers_company, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        holder.itemView.startAnimation(animation)
        var model = listData[position]
//        onLoadImageFromUrl(
//            context, model.images!!.get(0), holder.binding.iv
//        )
        holder.binding.tvTitle.text = ""+model.title
        holder.binding.tvDec.text = ""+model.description
        holder.binding.tvTotal.text = ""+model.price+" "+context.getString(R.string.r_s)

        holder.binding.btnSend.setOnClickListener {
            services.clickItemOfferCompany(model.id!!)
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}