package com.ksa.agenceCompany.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.databinding.ItemOpportunityHomeBinding

data class OpportunityHome(
    val title: String,
    val company: String,
    val budget: String,
    val tags: List<String>,
    val deadline: String
)

class OpportunityHomeAdapter(
    private val items: List<OpportunityHome>,
    private val onApply: (OpportunityHome) -> Unit
) : RecyclerView.Adapter<OpportunityHomeAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemOpportunityHomeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOpportunityHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvOpportunityTitle.text = item.title
        holder.binding.tvOpportunityCompany.text = item.company
        holder.binding.tvOpportunityBudget.text = item.budget
        holder.binding.tvOpportunityDeadline.text = item.deadline

        val tagsLayout = holder.binding.layoutOpportunityTags
        tagsLayout.removeAllViews()
        item.tags.forEachIndexed { index, tag ->
            val chip = TextView(holder.itemView.context)
            chip.text = tag
            chip.textSize = 9f
            chip.setPadding(20, 8, 20, 8)
            val bg = if (index % 2 == 0) R.drawable.bg_pill_blue_light else R.drawable.bg_pill_orange_light
            chip.setBackgroundResource(bg)
            chip.setTextColor(
                holder.itemView.context.getColor(if (index % 2 == 0) R.color.primary else R.color.secondary)
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.marginEnd = 12
            chip.layoutParams = params
            tagsLayout.addView(chip)
        }

        holder.binding.btnOpportunityApply.setOnClickListener { onApply(item) }
    }

    override fun getItemCount(): Int = items.size
}
