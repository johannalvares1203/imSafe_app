package com.example.imsafeapp.community.user

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imsafeapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CommunityFeedActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var requestAdapter: RequestAdapter
    private lateinit var requestList: MutableList<Request>

    private val btnJoinCommunity: Button
        get()=findViewById(R.id.btnJoinCommunity)
    private val tvCommunity: TextView
        get()=findViewById(R.id.tvCommunity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_community_feed)


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        requestList = mutableListOf()
        requestAdapter = RequestAdapter(requestList)
        recyclerView.adapter = requestAdapter

        val database = FirebaseDatabase.getInstance()
        val requestsRef = database.getReference("Requests")

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val userRef = userId?.let { database.getReference("Users").child(it) }

        val userRequestRef = FirebaseDatabase.getInstance().getReference("Requests").child(userId!!)

        userRequestRef.child("approved").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val approved = dataSnapshot.getValue(Boolean::class.java)
                if (approved == true) {
                    btnJoinCommunity.text = "Approved"
                    btnJoinCommunity.isEnabled = false
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle onCancelled
            }
        })

        userRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(User::class.java)
                    val constituency = user?.selectedConstituency
                    tvCommunity.text = constituency
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle onCancelled
            }
        })

        requestsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                requestList.clear()
                for (snapshot in dataSnapshot.children) {
                    val request = snapshot.getValue(Request::class.java)
                    request?.let {
                        if (it.approved == true) {
                            requestList.add(it)
                        }
                    }
                }
                requestAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })


        btnJoinCommunity.setOnClickListener{
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val requestRef = FirebaseDatabase.getInstance().getReference("Requests")

            requestRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(this@CommunityFeedActivity, "You have already sent a request", Toast.LENGTH_SHORT).show()
                    } else {



                        userId?.let { uid ->
                            val userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid)
                            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        val user = dataSnapshot.getValue(User::class.java)
                                        val userName = user?.userName
                                        val phoneNumber = user?.PhoneNumber
                                        val community = user?.selectedConstituency

                                        val request = hashMapOf(
                                            "userId" to userId,
                                            "constituency" to community,
                                            "userName" to userName,
                                            "phoneNumber" to phoneNumber,
                                            "approved" to false
                                        )
                                        requestRef.child(uid).setValue(request)
                                        Toast.makeText(this@CommunityFeedActivity, "Request sent to admin", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Handle onCancelled
                                }
                            })
                        }

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //possible errors
                }
            })
        }

    }
}