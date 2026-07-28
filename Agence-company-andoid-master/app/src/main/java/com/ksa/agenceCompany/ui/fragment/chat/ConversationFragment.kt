package com.ksa.agenceCompany.ui.fragment.chat

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaRecorder
import android.media.SoundPool
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.devlomi.record_view.OnRecordListener
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.REQUEST_CODE
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.ksa.agenceCompany.adapter.AdapterChat
import com.ksa.agenceCompany.AgenceCompanyApp.Companion.pref
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.base.BaseFragment
import com.ksa.agenceCompany.common.CacheFolder
import com.ksa.agenceCompany.common.FilesExtentin
import com.ksa.agenceCompany.common.PermissionCode
import com.ksa.agenceCompany.common.USER_DATA
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.common.util.Utilities.Companion.checkPermission
import com.ksa.agenceCompany.databinding.FragmentConversationBinding
import com.ksa.agenceCompany.entity.AllListChatCompany
import com.ksa.agenceCompany.entity.ListChatUser
import com.ksa.agenceCompany.entity.Message
import com.ksa.agenceCompany.ui.activity.MainActivity
import com.yehia.wave_record_util.AudioRecorder
import java.io.File
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class ConversationFragment : BaseFragment<FragmentConversationBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_conversation
    private lateinit var flag: String
    private lateinit var mainActivity: MainActivity

    private lateinit var userImage: String
    private lateinit var userName: String
    private var idOrder: Int = 0
    private var idUser: Int = 0
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
        onInitialVoice()
        checkAndSaveCompany()
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
            requireContext(), R.raw.record_start,
            1
        ) // تأكد من أن الملف موجود في res/raw  // تحميل الصوت

        soundIdFin = soundPool.load(
            requireContext(), R.raw.record_finished,
            1
        ) // تأكد من أن الملف موجود في res/raw


        soundIdError = soundPool.load(
            requireContext(), R.raw.record_error,
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
//        mainActivity.mViewDataBinding.tvTitleToolBar.text = getString(R.string.chat)
    }

    private fun initializeArguments() {
        arguments?.let {
            val args = ConversationFragmentArgs.fromBundle(it)
            flag = args.flag
            idOrder = args.idOrder
            idUser = args.idUser
            orderNO = args.orderNO
            categoryName = args.CategoryName
            userImage = args.imageUser
            userName = args.nameUser
            mViewDataBinding.tvNameCat.text = categoryName
            mViewDataBinding.tvNoOrder.text = getString(R.string.order_no) + " " + orderNO
            mViewDataBinding.lyChat.tvNameUser.text = userName
            Utilities.onLoadImageFromUrl(
                requireActivity(), userImage, mViewDataBinding.lyChat.ivUser
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
                    senderId = pref.loadUserData(
                        requireActivity(),
                        USER_DATA
                    )!!.data!!.company!!.id!!,
                    receivedId = idUser,
                    timestamp = System.currentTimeMillis()
                )
                saveMessage(message)
                mViewDataBinding.lyChat.tvYorMessage.text.clear()
                exampleUsage(pref.loadUserData(
                    requireActivity(), USER_DATA
                )!!.data!!.company!!.title!!,messageText)

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
            idCompany = pref.loadUserData(requireActivity(), USER_DATA)!!.data!!.company!!.id!!,
            nameCompany = pref.loadUserData(
                requireActivity(),
                USER_DATA
            )!!.data!!.company!!.title!!,
            imageCompany = pref.loadUserData(
                requireActivity(),
                USER_DATA
            )!!.data!!.company!!.company_logo!!,
            categoryName = categoryName,
            orderNumber = orderNO
        )

        newUserChat = ListChatUser(
            idUser = idUser,
            nameUser = userName,
            imageUser = userImage,
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
//            Toast.makeText(requireActivity(), "Recording started...", Toast.LENGTH_SHORT).show()
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
//                        Toast.makeText(context, "التسجيل قصير جداً ولم يتم رفعه.", Toast.LENGTH_SHORT).show()
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
                    )!!.data!!.company!!.id!!,
                    receivedId = idUser,
                    timestamp = 0

                )
                saveMessage(message)
                exampleUsage(pref.loadUserData(
                    requireActivity(), USER_DATA
                )!!.data!!.company!!.title!!,getString(R.string.send_pictures))
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
                    )!!.data!!.company!!.id!!,
                    receivedId = idUser,
                    timestamp = recordTime
                )
                saveMessage(message)
                exampleUsage(pref.loadUserData(
                    requireActivity(), USER_DATA
                )!!.data!!.company!!.title!!,getString(R.string.send_voice))
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

    }




        // ... الكود السابق

        private fun sendNotificationToUser(token: String, title: String, message: String) {
            val url = "https://fcm.googleapis.com/fcm/send"
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
                .addHeader("Authorization", "key=YOUR_SERVER_KEY")
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
        private fun exampleUsage(title:String,message:String) {
            val userToken = "dCykEGZLT8imUYT814rzlT:APA91bEseWlF7fQB32AlTi6VGms96x7Er8rrfHZdgol9OEXrcyFWJB1tbWQo3MjFKiv1QzL-eXVDewSBdEuRrytJFKGfOOrjyYHAOIR6CghIXNVrQi8PZZV49cuk7UZNbusLtcz3SABD" // استبدل برمز جهاز المستخدم
            val title = title
            val message = message
            sendNotificationToUser(userToken, title, message)
        }

        // ... الكود السابق
    }



