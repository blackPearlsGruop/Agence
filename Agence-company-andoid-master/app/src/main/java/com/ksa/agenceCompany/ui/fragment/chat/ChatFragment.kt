package com.ksa.agenceCompany.ui.fragment.chat

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.adapter.ListChatAdapter
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.databinding.FragmentChatBinding
import com.ksa.agenceCompany.entity.AllListChatCompany
import com.ksa.agenceCompany.entity.ListChatUser
import com.ksa.agenceCompany.interfaces.Chat
import com.ksa.agenceCompany.ui.activity.MainActivity

class ChatFragment : BaseFragment<FragmentChatBinding>(), Chat {

    override fun getLayoutId(): Int = R.layout.fragment_chat

    private lateinit var database: DatabaseReference
    lateinit var mainActivity:MainActivity


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity=requireActivity() as MainActivity
        mainActivity.mViewDataBinding.constraintLayout2.setBackgroundColor(resources.getColor(R.color.primary))


        database = FirebaseDatabase.getInstance().reference.child("orders")

        val companyList = mutableListOf<ListChatUser>()
        val adapter = ListChatAdapter(requireActivity(), companyList, this)
        mViewDataBinding.rvNewUserChat.adapter = adapter

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                companyList.clear()

                for (orderSnapshot in snapshot.children) {
                    val idOrder = orderSnapshot.key?.toIntOrNull()

                    if (idOrder != null) {
                        val companySnapshot = orderSnapshot.child("Users")
                        val company = companySnapshot.getValue(ListChatUser::class.java)

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
        idUser: Int,
        orderNO: String,
        categoryName: String,
        userImage: String,
       userName: String,
        idOrder: Int
    ) {
        val action = ChatFragmentDirections.actionChatFragmentToConversationFragment(
            idUser,
            orderNO,
            categoryName,
            userName,
            userImage,
            idOrder, // استخدام idOrder الصحيح هنا
            "LIST_CHAT"
        )
        mViewDataBinding.root.findNavController().navigate(action)
    }


}
