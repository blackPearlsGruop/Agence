package com.ksa.agence.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.ItemFeaturedMemberBinding

data class FeaturedMember(
    val photoUrl: String,
    val nameAr: String,
    val nameEn: String,
    val specialtyAr: String,
    val specialtyEn: String
)

class FeaturedMembersAdapter(
    private val context: android.content.Context,
    private val members: List<FeaturedMember>,
    private val isArabic: Boolean,
    private val onClick: (FeaturedMember) -> Unit
) : RecyclerView.Adapter<FeaturedMembersAdapter.MemberViewHolder>() {

    class MemberViewHolder(val binding: ItemFeaturedMemberBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFeaturedMemberBinding.inflate(inflater, parent, false)
        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val member = members[position]
        Utilities.onLoadImageFromUrl(context, member.photoUrl, holder.binding.ivMemberPhoto)
        holder.binding.tvMemberName.text = if (isArabic) member.nameAr else member.nameEn
        holder.binding.tvMemberSpecialty.text = if (isArabic) member.specialtyAr else member.specialtyEn
        holder.itemView.setOnClickListener { onClick(member) }
    }

    override fun getItemCount(): Int = members.size
}
