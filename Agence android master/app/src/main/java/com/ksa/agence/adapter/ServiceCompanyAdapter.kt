package com.ksa.agence.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agence.R
import com.ksa.agence.databinding.ItemCompanyServiceBinding
import com.ksa.agence.entity.showCompaniesResponse.ServiceShowCompaniesResponse
import com.ksa.agence.interfaces.Services

class ServiceCompanyAdapter(
    var context: Activity,
    var listData: List<ServiceShowCompaniesResponse>, var services: Services
) : RecyclerView.Adapter<ServiceCompanyAdapter.ViewHolder?>() {

    inner class ViewHolder(binding: ItemCompanyServiceBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemCompanyServiceBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemCompanyServiceBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.item_company_service, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = listData[position]

        holder.binding.tvServiceName.text = model.title
        holder.binding.tvServicePrice.text = "${model.price ?: 0} ${context.getString(R.string.r_s)}"

        // Mock services carry a ready display string in `description` (e.g.
        // "2 weeks" / "Per hour"). Real API services only have a day count,
        // so fall back to formatting that instead.
        holder.binding.tvServiceDuration.text = model.description
            ?: "${model.service_duration_in_days ?: 0} ${context.getString(R.string.day)}"

        holder.binding.btnGetOffer.setOnClickListener {
            services.clickItemServices(model.id ?: 0)
        }
    }

    override fun getItemCount(): Int = listData.size

    override fun getItemViewType(position: Int): Int = position
}
