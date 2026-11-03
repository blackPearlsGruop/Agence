package com.ksa.agence.member.ui.fragment.chat

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
import com.ksa.agence.member.adapter.ListChatAdapter
import com.ksa.agence.member.base.BaseFragment
import com.ksa.agence.databinding.MemberFragmentChatBinding
import com.ksa.agence.entity.ListChatUser
import com.ksa.agence.member.interfaces.Chat
import com.ksa.agence.member.ui.activity.MainActivity

class ChatFragment : BaseFragment<MemberFragmentChatBinding>(), Chat {

    override fun getLayoutId(): Int = R.layout.member_fragment_chat

    private lateinit var database: DatabaseReference
    lateinit var mainActivity:MainActivity


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity=requireActivity() as MainActivity
        // FIGMA REDESIGN: fragment_chat.xml has its own light header now.
        // MainActivity's destination-changed listener already hides the old
        // shared blue toolbar for this screen, same as Home/Account/Support.

        database = FirebaseDatabase.getInstance().reference.child("orders")

        val companyList = mutableListOf<ListChatUser>()
        val adapter = ListChatAdapter(requireActivity(), companyList, this)
        mViewDataBinding.rvNewUserChat.adapter = adapter

        // Sample conversations so the screen isn't empty while there are no
        // real conversations yet (same as the client side). Replaced
        // automatically as soon as Firebase returns real rows below.
        companyList.addAll(mockConversations())
        adapter.notifyDataSetChanged()
        updateEmptyState(false)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val realList = mutableListOf<ListChatUser>()

                for (orderSnapshot in snapshot.children) {
                    val idOrder = orderSnapshot.key?.toIntOrNull()

                    if (idOrder != null) {
                        val userSnapshot = orderSnapshot.child("Users")
                        val user = userSnapshot.getValue(ListChatUser::class.java)

                        user?.let {
                            it.idOrder = idOrder
                            realList.add(it)
                        }
                    }
                }

                // Only replace the samples once there is real data to show
                if (realList.isNotEmpty()) {
                    companyList.clear()
                    companyList.addAll(realList)
                    adapter.notifyDataSetChanged()
                    updateEmptyState(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", error.message)
            }
        })
    }

    private fun mockConversations(): List<ListChatUser> = listOf(
        ListChatUser(idOrder = 9101, idUser = 101, nameUser = "شركة الرياض التقنية", imageUser = "", categoryName = "الهوية البصرية والتصميم", orderNumber = "ORD-00512"),
        ListChatUser(idOrder = 9102, idUser = 102, nameUser = "الفارس للتجزئة", imageUser = "", categoryName = "حملات السوشيال ميديا", orderNumber = "ORD-00498"),
        ListChatUser(idOrder = 9103, idUser = 103, nameUser = "صيدلية المدينة", imageUser = "", categoryName = "إعلانات جوجل", orderNumber = "ORD-00471"),
        ListChatUser(idOrder = 9104, idUser = 104, nameUser = "تِك فيجن", imageUser = "", categoryName = "إنشاء المحتوى", orderNumber = "ORD-00455"),
        ListChatUser(idOrder = 9105, idUser = 105, nameUser = "مطاعم نخلة", imageUser = "", categoryName = "التسويق عبر المؤثرين", orderNumber = "ORD-00430")
    )

    private fun updateEmptyState(isEmpty: Boolean) {
        mViewDataBinding.layoutEmptyChat.visibility = if (isEmpty) View.VISIBLE else View.GONE
        mViewDataBinding.rvNewUserChat.visibility = if (isEmpty) View.GONE else View.VISIBLE
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
