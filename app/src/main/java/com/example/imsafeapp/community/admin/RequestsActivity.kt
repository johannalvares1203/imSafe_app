package com.example.imsafeapp.community.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imsafeapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class RequestsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_requests)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/


        val requestRef = FirebaseDatabase.getInstance().getReference("Requests")
        var requests: List<Request> = listOf()

        // Filter requests with "approved" set to false
        val filteredRequestsQuery: Query = requestRef.orderByChild("approved").equalTo(false)

        filteredRequestsQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                requests = dataSnapshot.children.mapNotNull { it.getValue(Request::class.java) }

                val recyclerView: RecyclerView = findViewById(R.id.recyclerViewRequests)
                recyclerView.layoutManager = LinearLayoutManager(this@RequestsActivity)
                recyclerView.adapter = RequestAdapter(requests)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //possible errors
            }
        })


    }
}