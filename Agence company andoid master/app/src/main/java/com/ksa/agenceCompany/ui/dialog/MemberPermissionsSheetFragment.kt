package com.ksa.agenceCompany.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.base.BaseBottomDialog
import com.ksa.agenceCompany.databinding.FragmentMemberPermissionsSheetBinding

/**
 * Bottom sheet that lets the team leader toggle each permission (create tasks,
 * edit tasks, delete tasks, distribute payments, invite members, upload files)
 * for a specific member.
 */
class MemberPermissionsSheetFragment : BaseBottomDialog<FragmentMemberPermissionsSheetBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_member_permissions_sheet

    private val perms = mutableMapOf(
        "create_tasks" to true,
        "edit_tasks" to true,
        "delete_tasks" to false,
        "distribute_payments" to true,
        "invite_members" to false,
        "upload_files" to true
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            val memberName = arguments?.getString(ARG_MEMBER_NAME) ?: "العضو"
            mViewDataBinding.tvSheetTitle.text = "صلاحيات $memberName"
        } catch (e: Exception) {
            Log.e("PermsSheet", "Failed to set title", e)
        }

        setupToggles()
        setupSave()
    }

    private fun setupToggles() {
        bindToggle(mViewDataBinding.rowCreateTasks, mViewDataBinding.toggleCreateTasks, "create_tasks")
        bindToggle(mViewDataBinding.rowEditTasks, mViewDataBinding.toggleEditTasks, "edit_tasks")
        bindToggle(mViewDataBinding.rowDeleteTasks, mViewDataBinding.toggleDeleteTasks, "delete_tasks")
        bindToggle(mViewDataBinding.rowDistributePayments, mViewDataBinding.toggleDistributePayments, "distribute_payments")
        bindToggle(mViewDataBinding.rowInviteMembers, mViewDataBinding.toggleInviteMembers, "invite_members")
        bindToggle(mViewDataBinding.rowUploadFiles, mViewDataBinding.toggleUploadFiles, "upload_files")
    }

    private fun bindToggle(row: View, toggle: View, key: String) {
        row.setOnClickListener {
            val newState = !(perms[key] ?: false)
            perms[key] = newState
            toggle.setBackgroundResource(
                if (newState) R.drawable.bg_toggle_on else R.drawable.bg_toggle_off
            )
        }
        toggle.setOnClickListener { row.performClick() }
    }

    private fun setupSave() {
        mViewDataBinding.btnSavePerms.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "تم حفظ الصلاحيات ✓",
                Toast.LENGTH_SHORT
            ).show()
            dismiss()
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }

    companion object {
        private const val ARG_MEMBER_NAME = "member_name"

        fun newInstance(memberName: String): MemberPermissionsSheetFragment {
            val fragment = MemberPermissionsSheetFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_MEMBER_NAME, memberName)
            }
            return fragment
        }
    }
}
