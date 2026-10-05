package com.ksa.agenceCompany.ui.fragment.home

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.adapter.ProjectTask
import com.ksa.agenceCompany.adapter.TeamMember
import com.ksa.agenceCompany.adapter.TeamMemberAdapter
import com.ksa.agenceCompany.adapter.TeamTaskAdapter
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.databinding.FragmentTeamProjectBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TeamProjectFragment : BaseFragment<FragmentTeamProjectBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_team_project

    private val avatarColorRes = listOf(R.color.primary, R.color.secondary, R.color.green)

    private val members = mutableListOf(
        TeamMember("Fahad Al-Otaibi", "Brand Strategy", "FA", isLead = true),
        TeamMember("Najm Agency", "Paid Acquisition", "NA"),
        TeamMember("Athar Studio", "Visual Identity", "AS"),
    )

    private val candidatePool = listOf(
        TeamMember("Sara Al-Qahtani", "Content Writing", "SQ"),
        TeamMember("Omar Rashid", "SEO Specialist", "OR"),
        TeamMember("Lama Fahad", "Video Production", "LF"),
    )

    private val tasks = mutableListOf(
        ProjectTask("Finalize logo concept directions", "AS", done = true),
        ProjectTask("Draft brand voice guidelines", "FA", done = true),
        ProjectTask("Set up campaign tracking pixels", "NA", done = false),
        ProjectTask("Review positioning statement", "FA", done = false),
        ProjectTask("Prepare social media templates", "AS", done = false),
    )

    private var startDateMillis: Long = 0
    private var deliveryDateMillis: Long = 0

    private lateinit var memberAdapter: TeamMemberAdapter
    private lateinit var taskAdapter: TeamTaskAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCalendarDefaults()
        setupHeader()
        setupQuickActions()

        lifecycleScope.launch {
            try {
                withContext(Dispatchers.Default) {
                    setupMemberAvatarsRowBackground()
                    setupMembersListBackground()
                    setupTasksListBackground()
                }

                updateProgress()
                updateDateLabels()
            } catch (e: Exception) {
                Log.e("TeamProject", "Error during setup", e)
            }
        }
    }

    private fun setupCalendarDefaults() {
        val start = Calendar.getInstance()
        start.set(2026, Calendar.JULY, 2)
        startDateMillis = start.timeInMillis

        val delivery = Calendar.getInstance()
        delivery.set(2026, Calendar.JULY, 30)
        deliveryDateMillis = delivery.timeInMillis
    }

    private fun setupHeader() {
        mViewDataBinding.ivBackTeam.setOnClickListener {
            mViewDataBinding.root.findNavController().popBackStack()
        }

        mViewDataBinding.btnTeamMenu.setOnClickListener {
            try {
                val popup = PopupMenu(requireContext(), it)
                popup.menuInflater.inflate(R.menu.menu_team_project_options, popup.menu)

                val isCurrentUserLead = true
                popup.menu.findItem(R.id.action_team_leader_panel)?.isVisible = isCurrentUserLead

                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_team_leader_panel -> {
                            try {
                                mViewDataBinding.root.findNavController()
                                    .navigate(R.id.teamLeaderPanelFragment)
                            } catch (e: Exception) {
                                Log.e("TeamProject", "Failed to open leader panel", e)
                                Toast.makeText(
                                    requireContext(),
                                    "خطأ: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            true
                        }
                        R.id.action_edit_project -> {
                            showEditProjectNameDialog()
                            true
                        }
                        R.id.action_delete_project -> {
                            showDeleteProjectConfirm()
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            } catch (e: Exception) {
                Log.e("TeamProject", "Error creating menu", e)
                Toast.makeText(requireContext(), "خطأ في القائمة", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showEditProjectNameDialog() {
        val editText = EditText(requireContext())
        editText.setText(mViewDataBinding.tvProjectName.text)
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.edit_project))
            .setView(editText)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                mViewDataBinding.tvProjectName.text = editText.text.toString()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun showDeleteProjectConfirm() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_project_confirm_title))
            .setMessage(getString(R.string.delete_project_confirm_body))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                mViewDataBinding.root.findNavController().popBackStack()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun dpToPx(dp: Int): Int =
        (dp * resources.displayMetrics.density).toInt()

    private fun setupMemberAvatarsRowBackground() {
        try {
            mViewDataBinding.layoutMemberAvatars.removeAllViews()
            members.forEachIndexed { index, member ->
                val avatar = android.widget.TextView(requireContext())
                val size = dpToPx(28)
                val params = android.widget.LinearLayout.LayoutParams(size, size)
                params.marginEnd = dpToPx(4)
                avatar.layoutParams = params
                avatar.gravity = android.view.Gravity.CENTER
                avatar.text = member.initials
                avatar.setTextColor(resources.getColor(R.color.white, null))
                avatar.textSize = 9f
                avatar.setBackgroundResource(R.drawable.bg_avatar_circle)
                avatar.backgroundTintList = android.content.res.ColorStateList.valueOf(
                    resources.getColor(avatarColorRes[index % avatarColorRes.size], null)
                )
                avatar.setOnClickListener { showMemberProfile(member) }
                mViewDataBinding.layoutMemberAvatars.addView(avatar)
            }

            val countLabel = android.widget.TextView(requireContext())
            countLabel.text = "${members.size} ${getString(R.string.members_count_suffix)}"
            countLabel.setTextColor(resources.getColor(R.color.black, null))
            countLabel.textSize = 10f
            val params = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.marginStart = dpToPx(6)
            countLabel.layoutParams = params
            mViewDataBinding.layoutMemberAvatars.addView(countLabel)
        } catch (e: Exception) {
            Log.e("TeamProject", "Error setting avatars", e)
        }
    }

    private fun setupQuickActions() {
        val comingSoon = View.OnClickListener {
            Toast.makeText(requireContext(), getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }
        mViewDataBinding.btnPaymentDistribution.setOnClickListener {
            mViewDataBinding.root.findNavController().navigate(R.id.paymentDistributionFragment)
        }
        mViewDataBinding.btnProjectFiles.setOnClickListener(comingSoon)
        mViewDataBinding.btnTeamChat.setOnClickListener {
            mViewDataBinding.root.findNavController().navigate(R.id.menuChat)
        }

        mViewDataBinding.btnEditProjectName.setOnClickListener { showEditProjectNameDialog() }

        mViewDataBinding.tvStartDate.setOnClickListener { pickDate(true) }
        mViewDataBinding.tvDeliveryDate.setOnClickListener { pickDate(false) }
    }

    private fun pickDate(isStart: Boolean) {
        val cal = Calendar.getInstance()
        cal.timeInMillis = if (isStart) startDateMillis else deliveryDateMillis
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val picked = Calendar.getInstance()
                picked.set(year, month, day)
                if (isStart) startDateMillis = picked.timeInMillis else deliveryDateMillis = picked.timeInMillis
                updateDateLabels()
            },
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateLabels() {
        val isArabic = Locale.getDefault().language == "ar"
        val formatter = SimpleDateFormat("d MMMM yyyy", if (isArabic) Locale("ar") else Locale.ENGLISH)
        mViewDataBinding.tvStartDate.text = formatter.format(startDateMillis)
        mViewDataBinding.tvDeliveryDate.text = formatter.format(deliveryDateMillis)
    }

    private fun setupMembersListBackground() {
        try {
            memberAdapter = TeamMemberAdapter(
                items = members,
                onMenuClick = { _, position, anchor ->
                    val popup = PopupMenu(requireContext(), anchor)
                    popup.menuInflater.inflate(R.menu.menu_member_options, popup.menu)
                    popup.setOnMenuItemClickListener { item ->
                        if (item.itemId == R.id.action_set_team_lead) {
                            members.forEachIndexed { i, m -> m.isLead = (i == position) }
                            memberAdapter.notifyDataSetChanged()
                            true
                        } else false
                    }
                    popup.show()
                },
                onDeleteClick = { position ->
                    members.removeAt(position)
                    memberAdapter.notifyDataSetChanged()
                    mViewDataBinding.rvTeamMembers.post { mViewDataBinding.rvTeamMembers.requestLayout() }
                    lifecycleScope.launch {
                        withContext(Dispatchers.Default) {
                            setupMemberAvatarsRowBackground()
                        }
                    }
                },
                onAvatarClick = { member -> showMemberProfile(member) }
            )
            mViewDataBinding.rvTeamMembers.layoutManager = LinearLayoutManager(requireContext())
            mViewDataBinding.rvTeamMembers.adapter = memberAdapter

            mViewDataBinding.btnAddMember.setOnClickListener {
                val available = candidatePool.filter { c -> members.none { it.name == c.name } }
                if (available.isEmpty()) {
                    Toast.makeText(requireContext(), getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val names = available.map { "${it.name} — ${it.specialty}" }.toTypedArray()
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.add_member))
                    .setItems(names) { _, which ->
                        members.add(available[which])
                        memberAdapter.notifyDataSetChanged()
                        mViewDataBinding.rvTeamMembers.post { mViewDataBinding.rvTeamMembers.requestLayout() }
                        lifecycleScope.launch {
                            withContext(Dispatchers.Default) {
                                setupMemberAvatarsRowBackground()
                            }
                        }
                    }
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show()
            }
        } catch (e: Exception) {
            Log.e("TeamProject", "Error setting up members list", e)
        }
    }

    private fun setupTasksListBackground() {
        try {
            taskAdapter = TeamTaskAdapter(
                items = tasks,
                onToggle = { position ->
                    tasks[position].done = !tasks[position].done
                    taskAdapter.notifyItemChanged(position)
                    updateProgress()
                },
                onEdit = { position -> showEditTaskDialog(position) },
                onDelete = { position ->
                    tasks.removeAt(position)
                    taskAdapter.notifyDataSetChanged()
                    mViewDataBinding.rvProjectTasks.post { mViewDataBinding.rvProjectTasks.requestLayout() }
                    updateProgress()
                }
            )
            mViewDataBinding.rvProjectTasks.layoutManager = LinearLayoutManager(requireContext())
            mViewDataBinding.rvProjectTasks.adapter = taskAdapter

            mViewDataBinding.btnAddTask.setOnClickListener { showAddTaskDialog() }
        } catch (e: Exception) {
            Log.e("TeamProject", "Error setting up tasks list", e)
        }
    }

    private fun showEditTaskDialog(position: Int) {
        val editText = EditText(requireContext())
        editText.setText(tasks[position].task)
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.add_task))
            .setView(editText)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                tasks[position].task = editText.text.toString()
                taskAdapter.notifyItemChanged(position)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun showAddTaskDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_task, null)
        val editText = dialogView.findViewById<EditText>(R.id.et_task_text)
        val spinner = dialogView.findViewById<Spinner>(R.id.spinner_task_assignee)

        val names = members.map { it.name }
        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, names)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.add_task))
            .setView(dialogView)
            .setPositiveButton(getString(R.string.add_label)) { _, _ ->
                val text = editText.text.toString().trim()
                if (text.isNotEmpty() && members.isNotEmpty()) {
                    val assignee = members[spinner.selectedItemPosition].initials
                    tasks.add(ProjectTask(text, assignee))
                    taskAdapter.notifyDataSetChanged()
                    mViewDataBinding.rvProjectTasks.post { mViewDataBinding.rvProjectTasks.requestLayout() }
                    updateProgress()
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun updateProgress() {
        val total = tasks.size.coerceAtLeast(1)
        val done = tasks.count { it.done }
        val percent = ((done.toFloat() / total.toFloat()) * 100).toInt()
        mViewDataBinding.tvProgressPercent.text = "$percent%"

        val fillParams = mViewDataBinding.viewProgressFill.layoutParams
        if (fillParams is android.widget.LinearLayout.LayoutParams) {
            fillParams.weight = percent.coerceAtLeast(1).toFloat()
            mViewDataBinding.viewProgressFill.layoutParams = fillParams
        }
    }

    private fun showMemberProfile(member: TeamMember) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_member_profile, null)
        dialogView.findViewById<android.widget.TextView>(R.id.tv_profile_initials).text = member.initials
        dialogView.findViewById<android.widget.TextView>(R.id.tv_profile_name).text = member.name
        dialogView.findViewById<android.widget.TextView>(R.id.tv_profile_specialty).text = member.specialty
        dialogView.findViewById<android.widget.TextView>(R.id.tv_profile_role).text =
            if (member.isLead) getString(R.string.team_lead_role) else getString(R.string.member_role)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        dialogView.findViewById<View>(R.id.btn_profile_close).setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }
}