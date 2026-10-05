package com.ksa.agenceCompany.ui.fragment.payment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.adapter.PaymentDistribution
import com.ksa.agenceCompany.adapter.PaymentDistributionAdapter
import com.ksa.agenceCompany.databinding.FragmentPaymentDistributionBinding

class PaymentDistributionFragment : Fragment() {

    private var _binding: FragmentPaymentDistributionBinding? = null
    private val binding get() = _binding!!

    private lateinit var paymentItems: MutableList<PaymentDistribution>
    private lateinit var adapter: PaymentDistributionAdapter
    private var totalProjectAmount = 10000.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentDistributionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPaymentData()
        setupRecyclerView()
        setupTotalAmountEditor()
        setupListeners()
    }

    private fun setupPaymentData() {
        paymentItems = mutableListOf(
            PaymentDistribution(
                memberId = 1,
                memberName = "فهد العتيبي",
                memberInitials = "FA",
                specialty = "استراتيجية العلامة - قائد",
                percentage = 32,
                amount = 3200.0,
                colorRes = R.color.agence_blue
            ),
            PaymentDistribution(
                memberId = 2,
                memberName = "نجم للتسويق",
                memberInitials = "NA",
                specialty = "الحملات المدفوعة - عضو",
                percentage = 35,
                amount = 3500.0,
                colorRes = R.color.agence_orange
            ),
            PaymentDistribution(
                memberId = 3,
                memberName = "أثير سعودي",
                memberInitials = "AS",
                specialty = "الهوية البصرية - عضو",
                percentage = 33,
                amount = 3300.0,
                colorRes = R.color.agence_green
            )
        )
    }

    private fun setupRecyclerView() {
        adapter = PaymentDistributionAdapter(paymentItems, totalProjectAmount) { totalAmount ->
            updateTotalDistributed(totalAmount)
        }

        binding.rvPayments.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@PaymentDistributionFragment.adapter
        }
    }

    private fun setupTotalAmountEditor() {
        binding.etTotalAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val cleanText = s.toString().replace(Regex("[^0-9.]"), "")
                val newTotal = cleanText.toDoubleOrNull() ?: 0.0
                totalProjectAmount = newTotal
                adapter.updateTotalProjectAmount(newTotal)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateTotalDistributed(totalAmount: Double) {
        binding.tvTotalDistributed.text = "ر.س ${String.format("%.2f", totalAmount)}"
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.btnEqualDistribution.setOnClickListener {
            distributeEqually()
        }

        binding.btnConfirmDistribution.setOnClickListener {
            showConfirmDialog()
        }

        binding.btnResetDistribution.setOnClickListener {
            Toast.makeText(requireContext(), "جاري تصدير PDF", Toast.LENGTH_SHORT).show()
        }
    }

    private fun distributeEqually() {
        if (paymentItems.isEmpty() || totalProjectAmount <= 0) return

        val equalAmount = totalProjectAmount / paymentItems.size
        val equalPercentage = (100 / paymentItems.size)

        paymentItems.forEach {
            it.amount = equalAmount
            it.percentage = equalPercentage
        }

        adapter.notifyDataSetChanged()
        updateTotalDistributed(totalProjectAmount)
        Toast.makeText(requireContext(), "تم التوزيع بالتساوي", Toast.LENGTH_SHORT).show()
    }

    private fun showConfirmDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("تأكيد التوزيع")
            .setMessage("هل أنت متأكد من توزيع المدفوعات؟")
            .setPositiveButton("تأكيد") { _, _ ->
                Toast.makeText(requireContext(), "تم حفظ التوزيع", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("إلغاء", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}