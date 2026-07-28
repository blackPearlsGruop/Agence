package com.ksa.agence.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ksa.agence.R
import com.ksa.agence.common.util.Utilities.Companion.onLoadImageFromUrl
import com.ksa.agence.databinding.ItemOnBoardingBinding
import com.ksa.agence.entity.itemOnBoardingResponse.ItemOnBoardingResponse

class OnBoardingAdapter(
    var context: Activity,
    var listData: List<ItemOnBoardingResponse>,
) : RecyclerView.Adapter<OnBoardingAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: ItemOnBoardingBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemOnBoardingBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemOnBoardingBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_on_boarding, parent, false
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

        Glide.with(context)
            .load(context.getDrawable(model.image)) // تحميل الصورة من المورد
            .into(holder.binding.ivImage) // عرض الصورة في ImageView

        holder.binding.tvTitle.text = model.title
        holder.binding.tvDec.text = model.dic
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}