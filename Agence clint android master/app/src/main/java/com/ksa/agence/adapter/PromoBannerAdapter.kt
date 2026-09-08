package com.ksa.agence.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agence.R
import com.ksa.agence.databinding.ItemPromoBannerBinding

data class PromoBanner(
    val titleAr: String,
    val titleEn: String,
    val subtitleAr: String,
    val subtitleEn: String,
    val backgroundRes: Int,
    val isAd: Boolean = false
)

class PromoBannerAdapter(
    private val banners: List<PromoBanner>,
    private val isArabic: Boolean
) : RecyclerView.Adapter<PromoBannerAdapter.PromoViewHolder>() {

    class PromoViewHolder(val binding: ItemPromoBannerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPromoBannerBinding.inflate(inflater, parent, false)
        return PromoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PromoViewHolder, position: Int) {
        val banner = banners[position]
        holder.binding.tvPromoTitle.text = if (isArabic) banner.titleAr else banner.titleEn
        holder.binding.tvPromoSubtitle.text = if (isArabic) banner.subtitleAr else banner.subtitleEn
        holder.binding.layoutPromoBg.setBackgroundResource(banner.backgroundRes)
        holder.binding.tvPromoAdBadge.visibility = if (banner.isAd) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int = banners.size
}
