package com.ksa.agence.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agence.R
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.ItemOffersBinding
import com.ksa.agence.entity.allOfferCompanyResponse.DataAllOfferCompanyResponse
import com.ksa.agence.interfaces.Offer

class AllOffersAdapter(
    var context: Activity,
    var listData: List<DataAllOfferCompanyResponse>, var clickOffer: Offer
) : RecyclerView.Adapter<AllOffersAdapter.ViewHolder?>() {


    inner class ViewHolder(binding: ItemOffersBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemOffersBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i("ServiceList", "adapter")
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemOffersBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_offers, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_in)
       // holder.itemView.startAnimation(animation)
        var model = listData[position]
//        onLoadImageFromUrl(
//            context, model.icon, holder.binding.ivLogoCompany
//        )
//



//        if (model.order!!.category !=null)
//        {
//            holder.binding.tvNameCategory.text =model.order!!.category!!.title!!
//            holder.binding.btnShow.setOnClickListener {
//                clickOffer.clickItemOffer(model.id!!,model.company!!.company_logo!!,
//                    model.company!!.title!!,model.order!!.category!!.description!!,model.order!!.category!!.title!!,model.price!!.toString(),model!!.order!!.description!!,model.order!!.order_number!!)
//            }
//        }
//
//        holder.binding.tvNameCompany.text = model.company!!.title
//        holder.binding.tvOnOffer.text =context.getString(R.string.order_no)+" "+ model.order!!.order_number
//        holder.binding.tvTotal.text =""+ model.price +" "+context.getString(R.string.r_s)
//
//
//        holder.binding.btnAccept.setOnClickListener {
//            clickOffer.acceptedOffer(model.id!!,position)
//
//        }
//
//        holder.binding.btnReject.setOnClickListener {
//            clickOffer.rejectOffer(model.id!!,position)
//
//        }


        model.order?.category?.let { category ->
            holder.binding.tvNameCategory.text = category.title ?: ""
            holder.binding.btnShow.setOnClickListener {
                clickOffer.clickItemOffer(
                    model.id ?: 0,
                    model.company?.company_logo ?: "",
                    model.company?.title ?: "",
                    category.description ?: "",
                    category.title ?: "",
                    model.price?.toString() ?: "",
                    model.order?.description ?: "",
                    model.order?.order_number ?: ""
                )
            }
        }

        holder.binding.tvNameCompany.text = model.company?.title ?: ""
        holder.binding.tvOnOffer.text = context.getString(R.string.order_no) + " " + (model.order?.order_number ?: "")
        holder.binding.tvTotal.text = (model.price?.toString() ?: "") + " " + context.getString(R.string.r_s)

        holder.binding.btnAccept.setOnClickListener {
            model.id?.let { id ->
                clickOffer.acceptedOffer(id, position)
            }
        }

        holder.binding.btnReject.setOnClickListener {
            model.id?.let { id ->
                clickOffer.rejectOffer(id, position)
            }
        }


    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}