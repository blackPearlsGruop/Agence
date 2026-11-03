package com.ksa.agence.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agence.R
import com.ksa.agence.common.util.Utilities.Companion.onLoadImageFromUrl
import com.ksa.agence.databinding.ItemMatchedProviderBinding
import com.ksa.agence.entity.MatchedProvider

class MatchedProviderAdapter(
    var context: Activity,
    var listData: List<MatchedProvider>,
    var onItemClick: (MatchedProvider) -> Unit
) : RecyclerView.Adapter<MatchedProviderAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemMatchedProviderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemMatchedProviderBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_matched_provider, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val provider = listData[position]

        holder.binding.tvRank.text = (position + 1).toString()
        onLoadImageFromUrl(context, provider.photoUrl, holder.binding.ivProviderPhoto)
        holder.binding.tvProviderName.text = provider.name
        holder.binding.ratingBar.rating = provider.rating
        holder.binding.tvProviderPrice.text = provider.price
        holder.binding.tvScore.text = "${provider.score}%"

        // Same score-color rule as the reference design: >89 blue, >74 orange, else muted ring
        val (ringRes, scoreColor) = when {
            provider.score > 89 -> R.drawable.bg_score_ring_blue to R.color.agence_blue
            provider.score > 74 -> R.drawable.bg_score_ring_orange to R.color.agence_orange
            else -> R.drawable.bg_score_ring_orange to R.color.agence_muted
        }
        holder.binding.scoreRing.setBackgroundResource(ringRes)
        holder.binding.tvScore.setTextColor(context.getColor(scoreColor))

        // Rank badge: only the top 3 get one ("BEST" for #1, "#2"/"#3" otherwise)
        if (position < 3) {
            holder.binding.tvRankBadge.visibility = android.view.View.VISIBLE
            if (position == 0) {
                holder.binding.tvRankBadge.text = context.getString(R.string.best_badge)
                holder.binding.tvRankBadge.setBackgroundResource(R.drawable.bg_agence_pill_active)
            } else {
                holder.binding.tvRankBadge.text = "#${position + 1}"
                holder.binding.tvRankBadge.setBackgroundResource(R.drawable.bg_pill_orange_solid)
            }
        } else {
            holder.binding.tvRankBadge.visibility = android.view.View.GONE
        }

        holder.binding.rootMatchedProvider.setOnClickListener {
            onItemClick(provider)
        }
    }

    override fun getItemCount(): Int = listData.size
}
