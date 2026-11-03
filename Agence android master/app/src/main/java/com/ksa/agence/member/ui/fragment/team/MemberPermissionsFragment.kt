package com.ksa.agence.member.ui.fragment.team

import com.ksa.agence.common.sampleText
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ksa.agence.R
import com.ksa.agence.member.adapter.PermissionLevel
import com.ksa.agence.member.adapter.PermissionMember
import com.ksa.agence.member.adapter.PermissionMemberAdapter
import com.ksa.agence.member.base.BaseFragment
import com.ksa.agence.databinding.FragmentMemberPermissionsBinding
import com.ksa.agence.member.ui.dialog.MemberPermissionsSheetFragment

class MemberPermissionsFragment : BaseFragment<FragmentMemberPermissionsBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_member_permissions

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    Toast.makeText(requireContext(), getString(R.string.msg_open_failed), Toast.LENGTH_SHORT).show()
                }
            }
        )

        mViewDataBinding.rvMembers.layoutManager = LinearLayoutManager(requireContext())
        mViewDataBinding.rvMembers.adapter = adapter
    }

    private fun buildMockMembers(): List<PermissionMember> = listOf(
        PermissionMember(
            id = 1,
            name = sampleText("فهد العتيبي"),
            role = sampleText("استراتيجية العلامة التجارية"),
            initials = "FA",
            avatarColorRes = R.color.agence_blue,
            level = PermissionLevel.LEADER
        ),
        PermissionMember(
            id = 2,
            name = sampleText("نجم للتسويق"),
            role = sampleText("الحملات الممولة"),
            initials = "NA",
            avatarColorRes = R.color.member_agence_orange,
            level = PermissionLevel.EDITOR
        ),
        PermissionMember(
            id = 3,
            name = sampleText("أثر ستوديو"),
            role = sampleText("الهوية البصرية"),
            initials = "AS",
            avatarColorRes = R.color.member_agence_green,
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