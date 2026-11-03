package com.ksa.agence.member.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agence.databinding.ItemWorkGalleryBinding
import com.ksa.agence.member.common.util.Utilities.Companion.onLoadImageFromUrl
import com.ksa.agence.member.entity.getCompanyWorksResponse.DataGetCompanyWorksResponse
import com.ksa.agence.member.interfaces.Home

/**
 * Portfolio grid: each card shows the work image, title, short description
 * and a delete button that reports back through [Home.clickItemDeleteService].
 */
class AllWorksAdapter(
    var context: Activity,
    var listData: List<DataGetCompanyWorksResponse>, var home: Home
) : RecyclerView.Adapter<AllWorksAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemWorkGalleryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemWorkGalleryBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = listData[position]

        if (!model.work_file.isNullOrBlank()) {
            onLoadImageFromUrl(context, model.work_file, holder.binding.ivWork)
        } else {
            holder.binding.ivWork.setImageDrawable(null)
        }

        holder.binding.tvWorkTitle.text = model.title.orEmpty()
        holder.binding.tvWorkDescription.text = model.description.orEmpty()

        holder.binding.btnDelete.setOnClickListener {
            val current = holder.bindingAdapterPosition
            if (current != RecyclerView.NO_POSITION) {
                home.clickItemDeleteService(model.id, current)
            }
        }
    }

    override fun getItemCount(): Int = listData.size
}
