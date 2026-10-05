package com.ksa.agenceCompany.ui.fragment.team

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.adapter.PermissionLevel
import com.ksa.agenceCompany.adapter.PermissionMember
import com.ksa.agenceCompany.adapter.PermissionMemberAdapter
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.databinding.FragmentMemberPermissionsBinding
import com.ksa.agenceCompany.ui.activity.MainActivity
import com.ksa.agenceCompany.ui.dialog.MemberPermissionsSheetFragment

class MemberPermissionsFragment : BaseFragment<FragmentMemberPermissionsBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_member_permissions

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            val activity = requireActivity() as MainActivity
            activity.mViewDataBinding.constraintLayout2.visibility = View.GONE
        } catch (e: Exception) {
            Log.e("MemberPermissions", "Failed to hide toolbar", e)
        }

        try {
            setupMembersList()
        } catch (e: Exception) {
            Log.e("MemberPermissions", "setupMembersList failed", e)
        }

        try {
            setupClicks()
        } catch (e: Exception) {
            Log.e("MemberPermissions", "setupClicks failed", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            val activity = requireActivity() as MainActivity
            activity.mViewDataBinding.constraintLayout2.visibility = View.VISIBLE
        } catch (e: Exception) {
            Log.e("MemberPermissions", "Failed to restore toolbar", e)
        }
    }

    private fun setupMembersList() {
        val members = buildMockMembers()

        val adapter = PermissionMemberAdapter(
            members = members,
            onMemberClick = { _ ->
                try {
                    val sheet = MemberPermissionsSheetFragment()
                    sheet.show(parentFragmentManager, "MemberPermissionsSheet")
                } catch (e: Exception) {
                    Log.e("MemberPermissions", "Failed to open sheet", e)
                    Toast.makeText(requireContext(), "تعذر فتح الصلاحيات", Toast.LENGTH_SHORT).show()
                }
            }
        )

        mViewDataBinding.rvMembers.layoutManager = LinearLayoutManager(requireContext())
        mViewDataBinding.rvMembers.adapter = adapter
    }

    private fun buildMockMembers(): List<PermissionMember> = listOf(
        PermissionMember(
            id = 1,
            name = "فهد العتيبي",
            role = "استراتيجية العلامة التجارية",
            initials = "FA",
            avatarColorRes = R.color.agence_blue,
            level = PermissionLevel.LEADER
        ),
        PermissionMember(
            id = 2,
            name = "نجم للتسويق",
            role = "الحملات الممولة",
            initials = "NA",
            avatarColorRes = R.color.agence_orange,
            level = PermissionLevel.EDITOR
        ),
        PermissionMember(
            id = 3,
            name = "أثر ستوديو",
            role = "الهوية البصرية",
            initials = "AS",
            avatarColorRes = R.color.agence_green,
            level = PermissionLevel.VIEWER
        )
    )

    private fun setupClicks() {
        mViewDataBinding.ivBack.setOnClickListener {
            try {
                mViewDataBinding.root.findNavController().popBackStack()
            } catch (e: Exception) {
                Log.e("MemberPermissions", "popBack failed", e)
            }
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }
}
