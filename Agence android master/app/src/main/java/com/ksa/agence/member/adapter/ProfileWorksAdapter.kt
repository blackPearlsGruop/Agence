package com.ksa.agence.member.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agence.databinding.ItemProfileWorkAddBinding
import com.ksa.agence.databinding.ItemProfileWorkBinding
import com.ksa.agence.member.common.util.Utilities
import com.ksa.agence.member.entity.getCompanyWorksResponse.DataGetCompanyWorksResponse

/**
 * Portfolio preview on the member profile: up to [maxWorks] works in a
 * two column grid, followed by an "add work" tile.
 */
class ProfileWorksAdapter(
    private val context: Activity,
    private val onWorkClick: (DataGetCompanyWorksResponse) -> Unit,
    private val onAddClick: () -> Unit,
    private val maxWorks: Int = 3
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val works = ArrayList<DataGetCompanyWorksResponse>()

    companion object {
        private const val TYPE_WORK = 0
        private const val TYPE_ADD = 1
    }

    fun submit(list: List<DataGetCompanyWorksResponse>) {
        works.clear()
        works.addAll(list.take(maxWorks))
        notifyDataSetChanged()
    }

    inner class WorkHolder(val binding: ItemProfileWorkBinding) : RecyclerView.ViewHolder(binding.root)
    inner class AddHolder(val binding: ItemProfileWorkAddBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int =
        if (position < works.size) TYPE_WORK else TYPE_ADD

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_WORK) {
            WorkHolder(ItemProfileWorkBinding.inflate(inflater, parent, false))
        } else {
            AddHolder(ItemProfileWorkAddBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WorkHolder -> {
                val work = works[position]
                holder.binding.tvWorkTitle.text = work.title.orEmpty()
                if (!work.work_file.isNullOrBlank()) {
                    Utilities.onLoadImageFromUrl(context, work.work_file, holder.binding.ivWork)
                } else {
                    holder.binding.ivWork.setImageDrawable(null)
                }
                holder.itemView.setOnClickListener { onWorkClick(work) }
            }

            is AddHolder -> holder.itemView.setOnClickListener { onAddClick() }
        }
    }

    override fun getItemCount(): Int = works.size + 1
}
