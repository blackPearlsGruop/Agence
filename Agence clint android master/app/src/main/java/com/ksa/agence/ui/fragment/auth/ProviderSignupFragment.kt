package com.ksa.agence.ui.fragment.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import com.ksa.agence.R
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.FragmentProviderSignupBinding

class ProviderSignupFragment : BaseFragment<FragmentProviderSignupBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_provider_signup

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadMockData()
        onClick()
    }

    private fun loadMockData() {
        // Load mock data for testing
        mViewDataBinding.etFullName.setText("أحمد محمد")
        // Username with @ prefix (read-only, styled like social media)
        mViewDataBinding.etUsername.setText("@ahmad_design")
        mViewDataBinding.etEmail.setText("ahmad@agence.com")
        mViewDataBinding.etBirthdate.setText("15/05/1995")
        mViewDataBinding.etSpecialties.setText("تصميم جرافيك")
        mViewDataBinding.etBio.setText("مصمم جرافيكي متخصص في تصميم الهويات البصرية والبراندينج. أعمل مع الشركات الناشئة والشركات الكبرى")
    }

    private fun onClick() {
        // Back button - navigate back to choose page
        mViewDataBinding.btnBack.setOnClickListener {
            val action = ProviderSignupFragmentDirections
                .actionProviderSignupFragmentToChoosePageFragment2()
            mViewDataBinding.root.findNavController().navigate(action)
        }

        // Continue button - validate and navigate to success screen
        mViewDataBinding.btnContinue.setOnClickListener {
            if (validateForm()) {
                val action = ProviderSignupFragmentDirections
                    .actionProviderSignupFragmentToProviderSuccessFragment()
                mViewDataBinding.root.findNavController().navigate(action)
            }
        }

        // Verification button - show coming soon message
        mViewDataBinding.btnVerification.setOnClickListener {
            Utilities.showToastSuccess(requireActivity(), "قريباً - صفحة التوثيق")
        }

        // Add portfolio button
        mViewDataBinding.btnAddPortfolio.setOnClickListener {
            Utilities.showToastSuccess(requireActivity(), "سيتم إضافة المزيد من الأعمال قريباً")
        }

        // Profile image click for image selection
        mViewDataBinding.flProfile.setOnClickListener {
            Utilities.showToastSuccess(requireActivity(), "تحديث صورة البروفايل قريباً")
        }

        // Birth date - open date picker (placeholder for now)
        mViewDataBinding.etBirthdate.setOnClickListener {
            Utilities.showToastSuccess(requireActivity(), "منتقي التاريخ قريباً")
        }
    }

    private fun validateForm(): Boolean {
        val fullName = mViewDataBinding.etFullName.text.toString().trim()
        val email = mViewDataBinding.etEmail.text.toString().trim()
        val specialties = mViewDataBinding.etSpecialties.text.toString().trim()

        return when {
            fullName.isEmpty() -> {
                Utilities.showToastError(requireActivity(), "الرجاء إدخال الاسم الكامل")
                false
            }
            email.isEmpty() -> {
                Utilities.showToastError(requireActivity(), "الرجاء إدخال البريد الإلكتروني")
                false
            }
            !isValidEmail(email) -> {
                Utilities.showToastError(requireActivity(), "البريد الإلكتروني غير صحيح")
                false
            }
            specialties.isEmpty() -> {
                Utilities.showToastError(requireActivity(), "الرجاء إدخال المجال المهني")
                false
            }
            else -> {
                Utilities.showToastSuccess(requireActivity(), "تم التحقق من البيانات بنجاح")
                true
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
        } else {
        }
    }
}