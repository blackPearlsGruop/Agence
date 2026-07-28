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
import com.ksa.agenceCompany.databinding.ItemOurBusinessBinding
import com.ksa.agenceCompany.entity.showCompaniesResponse.WorkShowCompaniesResponse

class OurBusinessCompanyAdapter(
    var context: Activity,
    var listData: List<WorkShowCompaniesResponse>,
) : RecyclerView.Adapter<OurBusinessCompanyAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: ItemOurBusinessBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemOurBusinessBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemOurBusinessBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_our_business, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        holder.itemView.startAnimation(animation)
        var model = listData[position]
        onLoadImageFromUrl(
            context, model.work_file.toString(), holder.binding.ivWorkFile
        )
        holder.binding.tvTitle.text = model.title
        holder.binding.tvDescription.text = model.description
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}