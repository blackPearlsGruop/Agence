package com.ksa.agence.ui.fragment.chat

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaRecorder
import android.media.SoundPool
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.SyncStateContract
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.util.Util
import com.devlomi.record_view.OnBasketAnimationEnd
import com.devlomi.record_view.OnRecordClickListener
import com.devlomi.record_view.OnRecordListener
import com.devlomi.record_view.RecordPermissionHandler
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.REQUEST_CODE
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.ksa.agence.R
import com.ksa.agence.adapter.AdapterChat
import com.ksa.agence.app.AgenceApp.Companion.pref
import com.ksa.agence.base.BaseFragment
import com.ksa.agence.common.CacheFolder
import com.ksa.agence.common.FilesExtentin
import com.ksa.agence.common.LOGIN
import com.ksa.agence.common.PermissionCode
import com.ksa.agence.common.USER_DATA
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.common.util.Utilities.Companion.checkPermission
import com.ksa.agence.databinding.FragmentConversationBinding
import com.ksa.agence.entity.AllListChatCompany
import com.ksa.agence.entity.ListChatUser
import com.ksa.agence.entity.Message
import com.ksa.agence.ui.activity.MainActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.UUID
import java.util.concurrent.TimeUnit

class ConversationFragment : BaseFragment<FragmentConversationBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_conversation
    private lateinit var flag: String
    private lateinit var mainActivity: MainActivity

    private lateinit var companyImage: String
    private lateinit var companyName: String
    private var idOrder: Int = 0
    private var idCompany: Int = 0
    private lateinit var orderNO: String
    private lateinit var categoryName: String
    private val messages: MutableList<Message> = mutableListOf()
    private val messageKeys: MutableSet<String> = mutableSetOf()

    private lateinit var messageAdapter: AdapterChat
    private lateinit var orderRef: DatabaseReference
    private lateinit var chatRef: DatabaseReference
    private lateinit var companyRef: DatabaseReference
    private lateinit var usersRef: DatabaseReference

    private lateinit var newCompanyChat: AllListChatCompany
    private lateinit var newUserChat: ListChatUser
    private val firebaseDatabase = FirebaseDatabase.getInstance()


    private var recorder: MediaRecorder? = null
    private var isRecording = false
    private var audioFilePath: String? = null


    var REQUEST_CODE_AUDIO_PERMISSION: Int = 3000
    private lateinit var soundPool: SoundPool
    private var soundId: Int = 0
    private var soundIdFin: Int = 0
    private var soundIdError: Int = 0

    private lateinit var handler: Handler
    private var startTime: Long = 0
    private var isTimerRunning: Boolean = false
    private lateinit var runnable: Runnable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMainActivity()
        initializeArguments()
        setupFirebaseReferences()
        initializeRecyclerView()
        setupListeners()
        checkAndSaveCompany()

        onInitialVoice()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // إعداد Handler
        handler = Handler(Looper.getMainLooper())

        // إعداد Runnable للتايمر
        runnable = Runnable {
            if (isTimerRunning) {
                val currentTime = System.currentTimeMillis() - startTime
                val seconds = (currentTime / 1000).toInt()
                updateTimerUI(seconds) // تحديث واجهة المستخدم
                handler.postDelayed(runnable, 1000) // إعادة التشغيل كل ثانية
            }
        }


        // إعداد SoundPool
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        // تحميل الصوت
        soundId = soundPool.load(
            requireContext(),
            R.raw.record_start,
            1
        ) // تأكد من أن الملف موجود في res/raw  // تحميل الصوت

        soundIdFin = soundPool.load(
            requireContext(),
            R.raw.record_finished,
            1
        ) // تأكد من أن الملف موجود في res/raw


            soundIdError = soundPool.load(
            requireContext(),
            R.raw.record_error,
            1
        ) // تأكد من أن الملف موجود في res/raw
    }


    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        // Handle network changes if necessary
    }

    private fun setupMainActivity() {
        mainActivity = requireActivity() as MainActivity
        mainActivity.hideHomeToolbar()
        mainActivity.mViewDataBinding.constraintLayout2.visibility = View.GONE
        mainActivity.mViewDataBinding.tvSearch.visibility = View.GONE
//        mainActivity.mViewDataBinding.tvTitleToolBar.text = getString(R.string.chat)
    }

    private fun initializeArguments() {
        arguments?.let {
            val args = ConversationFragmentArgs.fromBundle(it)
            flag = args.flag
            idOrder = args.idOrder
            idCompany = args.idCompany
            orderNO = args.orderNO
            categoryName = args.CategoryName!!
            companyImage = args.imageCompany
            companyName = args.nameCompany
            mViewDataBinding.tvNameCat.text = categoryName
            mViewDataBinding.tvNoOrder.text = getString(R.string.order_no) + " " + orderNO
            mViewDataBinding.lyChat.tvNameUser.text = companyName
            Utilities.onLoadImageFromUrl(
                requireActivity(), companyImage, mViewDataBinding.lyChat.ivUser
            )
        }
    }

    private fun setupFirebaseReferences() {
        orderRef = firebaseDatabase.getReference("orders").child(idOrder.toString())
        chatRef = orderRef.child("Chat")
        usersRef = orderRef.child("Users")
        companyRef = orderRef.child("Company")
    }

    private fun initializeRecyclerView() {
        messageAdapter = AdapterChat(requireActivity(), messages)
        mViewDataBinding.lyChat.rvAllMessage.adapter = messageAdapter
        mViewDataBinding.lyChat.rvAllMessage.layoutManager = LinearLayoutManager(requireActivity())
        loadMessages()
    }

    private fun setupListeners() {
        mViewDataBinding.lyChat.ivSend.setOnClickListener {
            val messageText = mViewDataBinding.lyChat.tvYorMessage.text.toString()
            if (messageText.isNotEmpty()) {
                val message = Message(
                    messageType = "TEXT",
                    messageVoice = "messageImage",
                    messageImage = "messageImage",
                    messageText = messageText,
                    isSent = true,
                    isRead = false,
                    isReceived = false,
                    senderId = pref.loadUserData(requireActivity(), USER_DATA)!!.data!!.user!!.id!!,
                    receivedId = idCompany,
                    timestamp = System.currentTimeMillis()
                )
                saveMessage(message)
                mViewDataBinding.lyChat.tvYorMessage.text.clear()
                mViewDataBinding.lyChat.tvYorMessage.text.clear()
                exampleUsage(
                    pref.loadUserData(
                        requireActivity(), USER_DATA
                    )!!.data!!.user!!.name!!, messageText
                )

            } else {
                Toast.makeText(requireActivity(), "Please enter a message", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        mViewDataBinding.lyChat.ivAttachment.setOnClickListener {
            pickImage()
        }
    }

    private fun saveMessage(message: Message) {
        val messageRef = chatRef.push()
        messageRef.setValue(message).addOnSuccessListener {
            mViewDataBinding.lyChat.rvAllMessage.scrollToPosition(messages.size - 1)
        }.addOnFailureListener { e ->
            e.printStackTrace()
        }
    }

    private fun loadMessages() {
        chatRef.orderByKey().addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                val messageKey = snapshot.key
                if (message != null && messageKey != null && !messageKeys.contains(messageKey)) {
                    if (!message.isSent && !message.isReceived) {
                        val updatedMessage = message.copy(isReceived = true)
                        snapshot.ref.setValue(updatedMessage)
                    }
                    messages.add(message)
                    messageKeys.add(messageKey)
                    messageAdapter.notifyItemInserted(messages.size - 1)
                    mViewDataBinding.lyChat.rvAllMessage.scrollToPosition(messages.size - 1)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                val messageKey = snapshot.key
                if (message != null && messageKey != null && messageKeys.contains(messageKey)) {
                    val index = messages.indexOfFirst { it.messageKey == messageKey }
                    if (index != -1) {
                        messages[index] = message
                        messageAdapter.notifyItemChanged(index)
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val messageKey = snapshot.key
                if (messageKey != null && messageKeys.contains(messageKey)) {
                    val index = messages.indexOfFirst { it.messageKey == messageKey }
                    if (index != -1) {
                        messages.removeAt(index)
                        messageKeys.remove(messageKey)
                        messageAdapter.notifyItemRemoved(index)
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {
                error.toException().printStackTrace()
            }
        })
    }

    private fun checkAndSaveCompany() {
        companyRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    saveDataCompanyInFirebase()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", error.message)
            }
        })
    }

    private fun saveDataCompanyInFirebase() {
        newCompanyChat = AllListChatCompany(
            idCompany = idCompany,
            nameCompany = companyName,
            categoryName = categoryName,
            orderNumber = orderNO
        )

        newUserChat = ListChatUser(
            idUser = pref.loadUserData(requireActivity(), USER_DATA)!!.data!!.user!!.id!!,
            nameUser = pref.loadUserData(requireActivity(), USER_DATA)!!.data!!.user!!.name!!,
            imageUser = pref.loadUserData(
                requireActivity(), USER_DATA
            )!!.data!!.user!!.profile_image!!,
            categoryName = categoryName,
            orderNumber = orderNO
        )

        companyRef.setValue(newCompanyChat)
        usersRef.setValue(newUserChat)
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            val imageUri = data?.data
            imageUri?.let {
                uploadImageToFirebase(it)
            }
        }
    }


    private fun updateTimerUI(seconds: Int) {
        // تحديث TextView لعرض الوقت
        val minutes = seconds / 60
        val secs = seconds % 60
        val timeString = String.format("%02d:%02d", minutes, secs)
        mViewDataBinding.lyChat.tvTimer.text = timeString // افترض أن لديك TextView بالمعرف tvTimer
    }

    @SuppressLint("ClickableViewAccessibility")
    fun onInitialVoice() {
        mViewDataBinding.lyChat.ivRecordButton.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startRecording()
                    mViewDataBinding.lyChat.ivRecordButton.setImageResource(R.drawable.ic_recording) // تغيير الصورة للإشارة إلى بدء التسجيل

                    // تشغيل الصوت عند بدء التسجيل
                    soundPool.play(soundId, 1f, 1f, 0, 0, 1f)

                    // بدء التايمر
                    startTime = System.currentTimeMillis()
                    isTimerRunning = true
                    handler.post(runnable) // بدء تشغيل التايمر

                    true
                }

                MotionEvent.ACTION_UP -> {
                    stopRecording()
                    mViewDataBinding.lyChat.ivRecordButton.setImageResource(R.drawable.ic_mic) // إعادة الصورة الأصلية

                    // تشغيل الصوت عند إنتهاء التسجيل
                    soundPool.play(soundIdFin, 1f, 1f, 0, 0, 1f)

                    // إيقاف التايمر
                    isTimerRunning = false
                    handler.removeCallbacks(runnable) // إزالة المهام المتبقية للتايمر

                    true
                }

                else -> false
            }
        }
    }

    private fun startRecording() {
        // تحقق من أذونات الميكروفون
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // طلب إذن الميكروفون من المستخدم
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_CODE_AUDIO_PERMISSION
            )
            return
        }

        // إذا كان التسجيل جارٍ بالفعل، أوقفه أولاً
        if (isRecording) {
            stopRecording()
        }

        try {
            audioFilePath = "${requireActivity().externalCacheDir?.absolutePath}/audio_record.3gp"

            recorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setOutputFile(audioFilePath)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                prepare() // تحضير التسجيل
                start() // بدء التسجيل
            }


            isRecording = true
            // عرض رسالة تخبر المستخدم ببدء التسجيل
            Toast.makeText(requireActivity(), "Recording started...", Toast.LENGTH_SHORT).show()
            mViewDataBinding.lyChat.cardView3.visibility = View.GONE

        } catch (e: IOException) {
            e.printStackTrace()
            // عرض رسالة خطأ في حال حدوث مشكلة
            Toast.makeText(
                requireActivity(),
                "Failed to start recording: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                requireActivity(),
                "An unexpected error occurred: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }



    private fun stopRecording() {
        try {
            if (isRecording) {
                recorder?.apply {
                    stop()
                    release()
                }
                recorder = null
                isRecording = false

                // حساب مدة التسجيل
                val endTime = System.currentTimeMillis()
                val duration = (endTime - startTime) / 1000 // الحصول على المدة بالثواني

                // استدعاء دالة رفع الملف فقط إذا كانت المدة أكثر من ثانية
                audioFilePath?.let { filePath ->
                    val file = File(filePath)

                    if (duration >= 1) { // إذا كانت مدة التسجيل أكثر من ثانية
                        val currentTime = System.currentTimeMillis() // الوقت الحالي
                        uploadAudioToFirebase(file, currentTime) // رفع التسجيل إلى Firebase
                    } else {
                        // إذا كانت المدة أقل من ثانية، قم بحذف الملف
                        if (file.exists()) {
                            file.delete() // حذف الملف إذا كان موجودًا
                        }
                        Toast.makeText(context, "التسجيل قصير جداً ولم يتم رفعه.", Toast.LENGTH_SHORT).show()
                        // تشغيل الصوت عند بدء التسجيل
                        soundPool.play(soundIdError, 1f, 1f, 0, 0, 1f)

                    }
                }

                mViewDataBinding.lyChat.cardView3.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    private fun uploadImageToFirebase(imageUri: Uri) {
        val storageRef =
            FirebaseStorage.getInstance().reference.child("chat_images/${System.currentTimeMillis()}.jpg")
        storageRef.putFile(imageUri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                val message = Message(
                    messageType = "IMAGE",
                    messageImage = uri.toString(),
                    messageVoice = "",
                    isSent = true,
                    isRead = false,
                    isReceived = false,
                    senderId = pref.loadUserData(
                        requireActivity(), USER_DATA
                    )!!.data!!.user!!.id!!,
                    receivedId = idCompany,
                    timestamp = 0

                )
                saveMessage(message)
                exampleUsage(
                    pref.loadUserData(
                        requireActivity(), USER_DATA
                    )!!.data!!.user!!.name!!, getString(R.string.send_pictures)
                )
            }
        }.addOnFailureListener { e ->
            e.printStackTrace()
        }
    }

    private fun uploadAudioToFirebase(audioFile: File, recordTime: Long) {

        val storageRef =
            FirebaseStorage.getInstance().reference.child("chat_audio/${audioFile.name}")
        val audioUri = Uri.fromFile(audioFile)
        storageRef.putFile(audioUri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                val message = Message(
                    messageType = "AUDIO",
                    messageImage = "",
                    messageVoice = uri.toString(),
                    isSent = true,
                    isRead = false,
                    isReceived = false,
                    senderId = pref.loadUserData(
                        requireActivity(), USER_DATA
                    )!!.data!!.user!!.id!!,
                    receivedId = idCompany,
                    timestamp = recordTime
                )
                saveMessage(message)
                exampleUsage(
                    pref.loadUserData(
                        requireActivity(), USER_DATA
                    )!!.data!!.user!!.name!!, getString(R.string.send_pictures)
                )
            }
        }.addOnFailureListener { e ->
            Log.e("FirebaseUpload", "Error uploading audio: ${e.message}")
        }
    }


    companion object {
        const val IMAGE_PICK_CODE = 1001
    }


    override fun onDestroy() {
        super.onDestroy()
        mainActivity.showHomeToolbar()
        mainActivity.mViewDataBinding.constraintLayout2.visibility = View.VISIBLE
        handler.removeCallbacks(runnable) // تأكد من إزالة المهام عند تدمير الـ Fragment

    }


    // ... الكود السابق

    //https://fcm.googleapis.com/v1/projects/myproject-b5ae1/messages:send
    private fun sendNotificationToUser(token: String, title: String, message: String) {
        val url = "https://fcm.googleapis.com/v1/projects/agence-543bf/messages:send"
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val client = OkHttpClient()

        // إعداد بيانات الإشعار
        val jsonObject = JSONObject().apply {
            put("to", token)
            put("notification", JSONObject().apply {
                put("title", title)
                put("body", message)
            })
        }

        // إعداد طلب HTTP
        val requestBody = jsonObject.toString().toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
//            .addHeader("Authorization", "key=YOUR_SERVER_KEY")
            .addHeader("Content-Type", "application/json")
            .build()

        // إرسال الطلب باستخدام OkHttp
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    // الإشعار تم إرساله بنجاح
                    println("Notification sent successfully")
                } else {
                    // فشل إرسال الإشعار
                    println("Failed to send notification: ${response.message}")
                }
            }
        })
    }

    // استخدام الدالة في مكان مناسب
    private fun exampleUsage(title: String, message: String) {
        val userToken =
            "cZY_2gJNQXKhjW0HOCRoGQ:APA91bE8Xuwy_gK5nGOfBD3e_5byG1HXUntadH42a3n8EtKBu8xmRUDdeJHD409t98BCQcuc5EkxcrAwCawToPNKTe42IcStk5tXJEm_vn63OnheWx9TWncHRsokMslh9XXnFvC3xvyc" // استبدل برمز جهاز المستخدم
        val title = title
        val message = message
        sendNotificationToUser(userToken, title, message)
    }

}
