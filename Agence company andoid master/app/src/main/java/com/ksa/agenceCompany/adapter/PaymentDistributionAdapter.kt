package com.ksa.agenceCompany.adapter

import android.graphics.PorterDuff
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agenceCompany.databinding.ItemPaymentMemberBinding

class PaymentDistributionAdapter(
    private val items: MutableList<PaymentDistribution>,
    private var totalProjectAmount: Double,
    private val onDistributionChanged: (totalAmount: Double) -> Unit
) : RecyclerView.Adapter<PaymentDistributionAdapter.PaymentViewHolder>() {

    fun updateTotalProjectAmount(newTotal: Double) {
        totalProjectAmount = newTotal
    }

    inner class PaymentViewHolder(private val binding: ItemPaymentMemberBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var textWatcher: TextWatcher? = null

        fun bind(item: PaymentDistribution, position: Int) {
            binding.tvAvatarBg.text = item.memberInitials

            binding.tvAvatarBg.background?.setColorFilter(
                binding.root.context.getColor(item.colorRes),
                PorterDuff.Mode.SRC_IN
            )

            binding.tvMemberName.text = item.memberName
            binding.tvSpecialty.text = item.specialty

            binding.etPercentage.text = "${item.percentage}%"

            val amountEditText = binding.tvCalculatedAmount as EditText

            textWatcher?.let { amountEditText.removeTextChangedListener(it) }

            amountEditText.setText(String.format("%.2f", item.amount))

            textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val cleanText = s.toString().replace(Regex("[^0-9.]"), "")
                    val amount = cleanText.toDoubleOrNull() ?: 0.0
                    items[position].amount = amount

                    val newPercentage = if (totalProjectAmount > 0) {
                        ((amount / totalProjectAmount) * 100).toInt()
                    } else 0
                    items[position].percentage = newPercentage
                    binding.etPercentage.text = "$newPercentage%"

                    calculateTotalAmount()
                }

                override fun afterTextChanged(s: Editable?) {}
            }
            amountEditText.addTextChangedListener(textWatcher)
        }

        private fun calculateTotalAmount() {
            val total = items.sumOf { it.amount }
            onDistributionChanged(total)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val binding = ItemPaymentMemberBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size
}