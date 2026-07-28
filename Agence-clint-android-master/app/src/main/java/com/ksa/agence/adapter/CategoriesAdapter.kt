package com.ksa.agence.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agence.R
import com.ksa.agence.databinding.ItemCategoryBinding
import com.ksa.agence.entity.categoriesResponse.DataCategoriesResponse
import com.ksa.agence.interfaces.Company

class CategoriesAdapter(
    var context: Activity,
    var listData: List<DataCategoriesResponse>,var company: Company
) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemCategoryBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemCategoryBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_category, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        holder.itemView.startAnimation(animation)
        var model = listData[position]
//        onLoadImageFromUrl(
//            context, model.image.toString(), holder.binding.ivImage
//        )

        holder.binding.tvTitle.text = model.title


        holder.itemView.setOnClickListener {
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