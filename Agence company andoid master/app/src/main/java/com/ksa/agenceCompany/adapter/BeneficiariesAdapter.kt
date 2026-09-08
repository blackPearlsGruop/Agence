package com.ksa.agenceCompany.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agenceCompany.databinding.ItemBeneficiaryHomeBinding

data class Beneficiary(
    val companyAr: String,
    val companyEn: String,
    val projectAr: String,
    val projectEn: String,
    val budget: String,
    val progress: Int,
    val statusAr: String,
    val statusEn: String,
    val statusColorGreen: Boolean
)

class BeneficiariesAdapter(
    private val items: List<Beneficiary>,
    private val isArabic: Boolean,
    private val onClick: (Beneficiary) -> Unit
) : RecyclerView.Adapter<BeneficiariesAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemBeneficiaryHomeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBeneficiaryHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvBenCompany.text = if (isArabic) item.companyAr else item.companyEn
        holder.binding.tvBenProject.text = if (isArabic) item.projectAr else item.projectEn
        holder.binding.tvBenBudget.text = item.budget
        holder.binding.tvBenProgress.text = "${item.progress}%"
        holder.binding.progressBen.progress = item.progress
        holder.binding.tvBenStatus.text = if (isArabic) item.statusAr else item.statusEn

        if (item.statusColorGreen) {
            holder.binding.tvBenStatus.setBackgroundResource(com.ksa.agenceCompany.R.drawable.bg_status_pill_green)
            holder.binding.tvBenStatus.setTextColor(holder.itemView.context.getColor(com.ksa.agenceCompany.R.color.green))
            holder.binding.progressBen.progressTintList =
                android.content.res.ColorStateList.valueOf(holder.itemView.context.getColor(com.ksa.agenceCompany.R.color.green))
        } else {
            holder.binding.tvBenStatus.setBackgroundResource(com.ksa.agenceCompany.R.drawable.bg_status_pill_orange)
            holder.binding.tvBenStatus.setTextColor(holder.itemView.context.getColor(com.ksa.agenceCompany.R.color.secondary))
            holder.binding.progressBen.progressTintList =
                android.content.res.ColorStateList.valueOf(holder.itemView.context.getColor(com.ksa.agenceCompany.R.color.secondary))
        }

        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = items.size
}
