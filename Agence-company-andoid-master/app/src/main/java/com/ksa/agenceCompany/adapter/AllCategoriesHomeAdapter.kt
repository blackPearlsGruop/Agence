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
import com.ksa.agenceCompany.databinding.ItemServiceHomeBinding
import com.ksa.agenceCompany.entity.categoriesResponse.DataCategoriesResponse
import com.ksa.agenceCompany.interfaces.Home

class AllCategoriesHomeAdapter(
    var context: Activity,
    var listData: List<DataCategoriesResponse>, var home: Home
) : RecyclerView.Adapter<AllCategoriesHomeAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: ItemServiceHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemServiceHomeBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemServiceHomeBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_service_home, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        // holder.itemView.startAnimation(animation)
        var model = listData[position]
        onLoadImageFromUrl(
            context, model.images!!.get(0), holder.binding.ivLogoService
        )

        holder.binding.tvName.text = model.title
        holder.binding.tvDicCategory.text =model.description


        holder.binding.btnShow.setOnClickListener {
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