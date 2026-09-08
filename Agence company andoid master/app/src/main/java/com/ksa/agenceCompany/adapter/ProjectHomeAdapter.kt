package com.ksa.agenceCompany.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agenceCompany.databinding.ItemProjectHomeBinding

data class ProjectHome(
    val name: String,
    val client: String,
    val membersCountLabel: String
)

class ProjectHomeAdapter(
    private val items: List<ProjectHome>,
    private val onClick: (ProjectHome) -> Unit
) : RecyclerView.Adapter<ProjectHomeAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemProjectHomeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProjectHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvProjectHomeName.text = item.name
        holder.binding.tvProjectHomeClient.text = item.client
        holder.binding.tvProjectHomeMembers.text = item.membersCountLabel
        holder.binding.root.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = items.size
}
