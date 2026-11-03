package com.ksa.agence.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agence.R
import com.ksa.agence.databinding.ItemMultiChoiceItemsBinding
import com.ksa.agence.entity.categoriesResponse.DataCategoriesResponse
import kotlin.math.log

class MultiSelectCategoriesAdapter(
    var context: Activity,
    var listData: List<DataCategoriesResponse>,
) : RecyclerView.Adapter<MultiSelectCategoriesAdapter.ViewHolder?>() {


    var selectedItems: ArrayList<Int> = ArrayList<Int>()
    var listCategories: ArrayList<DataCategoriesResponse> = ArrayList<DataCategoriesResponse>()

    inner class ViewHolder(val binding: ItemMultiChoiceItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var checkBoxSelected: Boolean=false

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemMultiChoiceItemsBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_multi_choice_items, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = listData[position]
        holder.binding.textTitle.text = model.title

        holder.itemView.setOnClickListener {
            if (!holder.checkBoxSelected) {
                selectedItems.add(model.id!!)
                listCategories.add(model)
                holder.checkBoxSelected=true
                holder.binding.ivSelected.setImageResource(R.drawable.icon_selected)
                Log.d("Testtskv IDS",selectedItems.size.toString())

            } else {
                selectedItems.remove(model.id)
                listCategories.remove(model)
                holder.checkBoxSelected=false
                holder.binding.ivSelected.setImageDrawable(null)
                Log.d("Testtskv IDS",selectedItems.size.toString())

                if (model.isSelected==true)
                {
                    model.isSelected=false
                    selectedItems.remove(model.id!!)
                    listCategories.remove(model)

                }
            }
        }

        Log.d("onBindViewHolder: ",model.isSelected.toString())

        if (model.isSelected==true)
        {
            holder.binding.ivSelected.setImageResource(R.drawable.icon_selected)
            selectedItems.add(model.id!!)
            listCategories.add(model)

        }
        else{
            holder.binding.ivSelected.setImageDrawable(null)
            selectedItems.remove(model.id)
            listCategories.remove(model)
        }

    }


    override fun getItemCount(): Int {
        return listData.size
    }
}
