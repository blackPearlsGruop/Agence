package com.ksa.agence.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.SpinnerAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agence.R
import com.ksa.agence.common.util.Utilities.Companion.onLoadImageFromUrl
import com.ksa.agence.databinding.ItemAllServiceBinding
import com.ksa.agence.databinding.ItemBillsBinding
import com.ksa.agence.databinding.ItemCategoryBinding
import com.ksa.agence.entity.categoriesResponse.DataCategoriesResponse
import com.ksa.agence.interfaces.Company

class InvoicesAdapter(
    var context: Activity,
    var listData: List<DataCategoriesResponse>,var company: Company
) : RecyclerView.Adapter<InvoicesAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: ItemBillsBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemBillsBinding= binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemBillsBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_bills, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
       // holder.itemView.startAnimation(animation)
        var model = listData[position]
//        onLoadImageFromUrl(
//            context, model.icon, holder.binding.imageView3
//        )

        holder.binding.tvTitle.text = model.title
        holder.binding.tvBody.text = model.description
        holder.binding.tvNameCompany.text = model.description
        holder.binding.tvDate.text = model.description


        holder.binding.btnShow.setOnClickListener {
            company.clickItemShowService(model.id!!)
        }

    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}