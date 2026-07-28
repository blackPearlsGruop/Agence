package com.ksa.agenceCompany.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.databinding.ItemImagesLocalBinding
import com.ksa.agenceCompany.interfaces.DeleteImageLocale

class ImageLocaleAdapter(
    var context: Activity,
    var list: List<String>, var deleteImageLocale: DeleteImageLocale
) : RecyclerView.Adapter<ImageLocaleAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: ItemImagesLocalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemImagesLocalBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemImagesLocalBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_images_local, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
        holder.itemView.startAnimation(animation)
        var model = list[position]
        Utilities.onLoadImageFromUrl(
            context, model, holder.binding.ivImageCar
        )



        holder.binding.ivDelete.setOnClickListener {
            deleteImageLocale.delete(position)
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }


}