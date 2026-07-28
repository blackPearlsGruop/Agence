package com.ksa.agenceCompany.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.databinding.ItemMultiChoiceItemsBinding
import com.ksa.agenceCompany.entity.categoriesResponse.DataCategoriesResponse

class SelectedCategoriesAdapter(
    var context: Activity,
    var listData: List<DataCategoriesResponse>
) : RecyclerView.Adapter<SelectedCategoriesAdapter.ViewHolder?>() {

    private val selectedItems = mutableSetOf<DataCategoriesResponse>() // حفظ العناصر المحددة


    inner class ViewHolder(binding: ItemMultiChoiceItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: ItemMultiChoiceItemsBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemMultiChoiceItemsBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_multi_choice_items, parent, false
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

        holder.binding.textTitle.text = model.title


//        if (model.isSelected == true){
//            holder.binding.checkBox.isChecked
//            holder.binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
//                    selectedItems.remove(model)
//                notifyDataSetChanged()
//                    Log.d("MultiSelectAdapter", "Item deselected: ${model.id}")
//
//                Log.d("MultiSelectAdapter", "Current selected items: ${selectedItems.joinToString { it.id.toString() }}")
//            }
//        }

    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}