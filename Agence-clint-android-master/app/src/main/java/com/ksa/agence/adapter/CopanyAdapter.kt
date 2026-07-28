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
import com.ksa.agence.databinding.ItemCompanyBinding
import com.ksa.agence.entity.companyResponse.DataCompanyResponse
import com.ksa.agence.interfaces.Company

class CopanyAdapter(
    var context: Activity,
    var listData: List<DataCompanyResponse>,var company: Company
) : RecyclerView.Adapter<CopanyAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: ItemCompanyBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemCompanyBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemCompanyBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_company, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        holder.itemView.startAnimation(animation)
        var model = listData[position]
//        onLoadImageFromUrl(
//            context, model.company_logo.toString(), holder.binding.ivLogoCompany
//        )

        holder.binding.tvNameCompany.text = model.title
        holder.binding.ratingBar.rating = model.avg_rate!!.toFloat()
        holder.binding.tvCountRat.text = ""+model.rate_count!!
        holder.binding.tvNameCategory.text = model.categories!![0].title
        holder.binding.tvDicCompany.text = model.description

        if (model.is_added_favourite==false)
        {
            holder.binding.ivFavorite.setImageResource(R.drawable.icon_not_favorite)
        }
        else{
            holder.binding.ivFavorite.setImageResource(R.drawable.icon_action_favorite)

        }
        holder.binding.ivFavorite.setOnClickListener {
            company.clickItemAddCompanyFav(model.id!!,position)
        }


        holder.binding.btnShow.setOnClickListener {
            company.clickItemCompany(model.id!!,"")
        }


    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}