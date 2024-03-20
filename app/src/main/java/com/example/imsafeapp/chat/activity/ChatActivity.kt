package com.example.imsafeapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {

    lateinit var chatRecyclerView: RecyclerView


    private val imgBack: ImageView
        get() = findViewById(R.id.imgBack)
    private val imgProfile: CircleImageView
        get() = findViewById(R.id.imgProfile)
    private val tvUserName: TextView
        get() = findViewById(R.id.tvUserName)
    private val btnSendMessage: ImageButton
        get() = findViewById(R.id.btnSendMessage)
    private val etMessage: EditText
        get() = findViewById(R.id.etMessage)
    private val btnSendFile: ImageButton
        get() = findViewById(R.id.btnSendFile)
    private val progressBar: ProgressBar
        get() = findViewById(R.id.progressBar)

    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    var chatList = ArrayList<Chat>()
    var topic = ""

    private lateinit var userId: String
    private val PICK_FILE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        btnSendFile.setOnClickListener {
            Log.d("********************", "in btnSendFile")
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, PICK_FILE_REQUEST)
        }


        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        chatRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        var intent = getIntent()
        userId = intent.getStringExtra("userId")!!
        var userName = intent.getStringExtra("userName")


        imgBack.setOnClickListener {
            onBackPressed()
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)


        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(User::class.java)
                tvUserName.text = user!!.userName
                if (user.profileImage == "") {
                    imgProfile.setImageResource(R.drawable.profile_image)
                } else {
                    Glide.with(this@ChatActivity).load(user.profileImage).into(imgProfile)
                }
            }
        })

        btnSendMessage.setOnClickListener {
            var message: String = etMessage.text.toString()

            if (message.isEmpty()) {
                Toast.makeText(applicationContext, "message is empty", Toast.LENGTH_SHORT).show()
                etMessage.setText("")
            } else {
                sendMessage(firebaseUser!!.uid, userId, message, "", "text")
                etMessage.setText("")
                topic = "/topics/$userId"
                PushNotification(
                    NotificationData(userName!!, message),
                    topic
                ).also {
                    sendNotification(it)
                }

            }
        }

        readMessage(firebaseUser!!.uid, userId)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val filePath = data.data!!
            uploadFile(filePath)
        }
    }


    private fun uploadFile(filePath: Uri) {

        val fileName = filePath.lastPathSegment

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        val storageReference = FirebaseStorage.getInstance().getReference("uploads")
        val fileReference = storageReference.child(System.currentTimeMillis().toString())
        val uploadTask = fileReference.putFile(filePath)

        uploadTask.addOnProgressListener { taskSnapshot ->
            val progress =
                (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
            progressBar.progress = progress
        }

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            fileReference.downloadUrl
        }.addOnCompleteListener { task ->
            progressBar.visibility = View.GONE
            if (task.isSuccessful) {
                val downloadUri = task.result
                sendMessage(firebaseUser!!.uid, userId, downloadUri.toString(), fileName!!, "file")
            }
        }
    }


    private fun sendMessage(
        senderId: String,
        receiverId: String,
        message: String,
        fileName: String,
        type: String
    ) {
        var reference: DatabaseReference? = FirebaseDatabase.getInstance().getReference()

        var hashMap: HashMap<String, String> = HashMap()
        hashMap.put("senderId", senderId)
        hashMap.put("receiverId", receiverId)
        hashMap.put("message", message)
        hashMap.put("type", type)
        hashMap.put("fileName", fileName)

        reference!!.child("Chat").push().setValue(hashMap)

    }

    fun readMessage(senderId: String, receiverId: String) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(Chat::class.java)

                    if (chat!!.senderId.equals(senderId) && chat!!.receiverId.equals(receiverId) ||
                        chat!!.senderId.equals(receiverId) && chat!!.receiverId.equals(senderId)
                    ) {
                        chatList.add(chat)
                    }
                }

                val chatAdapter = ChatAdapter(this@ChatActivity, chatList)

                chatRecyclerView.adapter = chatAdapter
            }
        })
    }

    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    Log.d("TAG", "Response: ${Gson().toJson(response)}")
                } else {
                    Log.e("TAG", response.errorBody()!!.string())
                }
            } catch (e: Exception) {
                Log.e("TAG", e.toString())
            }
        }

}