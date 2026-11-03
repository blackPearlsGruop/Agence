package com.ksa.agence.ui.fragment.chat

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ksa.agence.R
import com.ksa.agence.adapter.ListChatAdapter
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.databinding.FragmentChatBinding
import com.ksa.agence.entity.AllListChatCompany
import com.ksa.agence.interfaces.Chat
import com.ksa.agence.ui.activity.MainActivity

class ChatFragment : BaseFragment<FragmentChatBinding>(), Chat {

    override fun getLayoutId(): Int = R.layout.fragment_chat
    lateinit var mainActivity: MainActivity

    private lateinit var database: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = requireActivity() as MainActivity

        // Belt-and-suspenders: hide the old shared blue toolbar here too, in case
        // this screen is ever reached without going through MainActivity's bottom
        // nav click handler (e.g. state restoration) — same fix as Home/Account/Orders.
        try {
            mainActivity.mViewDataBinding.constraintLayout2.visibility = View.GONE
            mainActivity.mViewDataBinding.btnQuickOrder.visibility = View.GONE
        } catch (e: Exception) {
            Log.e("ChatFragment", "constraintLayout2 hide failed", e)
        }


        database = FirebaseDatabase.getInstance().reference.child("orders")

        val companyList = mutableListOf<AllListChatCompany>()
        val adapter = ListChatAdapter(requireActivity(), companyList, this)
        mViewDataBinding.rvNewUserChat.adapter = adapter

        // Local mock data so the screen isn't blank while there are no real
        // orders/conversations yet — same pattern used across Home/Orders.
        // Replaced automatically the moment Firebase returns real rows below.
        companyList.addAll(mockConversations())
        adapter.notifyDataSetChanged()
        mViewDataBinding.rvNewUserChat.visibility = View.VISIBLE
        mViewDataBinding.layoutEmptyChat.visibility = View.GONE

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val realList = mutableListOf<AllListChatCompany>()

                for (orderSnapshot in snapshot.children) {
                    val idOrder = orderSnapshot.key?.toIntOrNull()

                    if (idOrder != null) {
                        val companySnapshot = orderSnapshot.child("Company")
                        val company = companySnapshot.getValue(AllListChatCompany::class.java)

                        company?.let {
                            it.idOrder = idOrder
                            realList.add(it)
                        }
                    }
                }

                // Only replace the mock rows once there's real data to show —
                // an empty snapshot just means no orders yet, not "hide the list".
                if (realList.isNotEmpty()) {
                    companyList.clear()
                    companyList.addAll(realList)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", error.message)
            }
        })
    }

    private fun mockConversations(): List<AllListChatCompany> = listOf(
        AllListChatCompany(idOrder = 9001, idCompany = 1, nameCompany = "فهد العتيبي", imageCompany = "", categoryName = "استراتيجية العلامة والسوشيال", orderNumber = "ORD-00419"),
        AllListChatCompany(idOrder = 9002, idCompany = 2, nameCompany = "وكالة نجم", imageCompany = "", categoryName = "الإعلانات المدفوعة والنمو", orderNumber = "ORD-00387"),
        AllListChatCompany(idOrder = 9003, idCompany = 3, nameCompany = "استوديو أثر", imageCompany = "", categoryName = "الهوية البصرية والتصميم", orderNumber = "ORD-00301"),
        AllListChatCompany(idOrder = 9004, idCompany = 4, nameCompany = "ريم ميديا", imageCompany = "", categoryName = "إنشاء المحتوى", orderNumber = "ORD-00276"),
        AllListChatCompany(idOrder = 9005, idCompany = 5, nameCompany = "Sky Brand Co.", imageCompany = "", categoryName = "تحسين محركات البحث والنمو", orderNumber = "ORD-00250"),
        AllListChatCompany(idOrder = 9006, idCompany = 6, nameCompany = "Pulse Ads", imageCompany = "", categoryName = "التسويق عبر المؤثرين", orderNumber = "ORD-00214")
    )

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }

    override fun clickItemChat(
        idCompany: Int,
        orderNO: String,
        categoryName: String,
        companyImage: String,
        companyName: String,
        idOrder: Int
    ) {
        val action = ChatFragmentDirections.actionMenuChatToConversationFragment(
            idCompany,
            orderNO,
            categoryName,
            companyName,
            companyImage,
            idOrder, // استخدام idOrder الصحيح هنا
            "LIST_CHAT"
        )
        mViewDataBinding.root.findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity.mViewDataBinding.tvSearch.visibility = View.VISIBLE

    }
}
