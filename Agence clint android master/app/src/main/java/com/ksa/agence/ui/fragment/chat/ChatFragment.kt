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


        database = FirebaseDatabase.getInstance().reference.child("orders")

        val companyList = mutableListOf<AllListChatCompany>()
        val adapter = ListChatAdapter(requireActivity(), companyList, this)
        mViewDataBinding.rvNewUserChat.adapter = adapter

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                companyList.clear()

                for (orderSnapshot in snapshot.children) {
                    val idOrder = orderSnapshot.key?.toIntOrNull()

                    if (idOrder != null) {
                        val companySnapshot = orderSnapshot.child("Company")
                        val company = companySnapshot.getValue(AllListChatCompany::class.java)

                        company?.let {
                            it.idOrder = idOrder
                            companyList.add(it)
                        }
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", error.message)
            }
        })
    }

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
