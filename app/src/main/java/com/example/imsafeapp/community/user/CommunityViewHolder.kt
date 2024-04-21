package com.example.imsafeapp.community.user

import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.imsafeapp.R
import com.example.imsafeapp.community.admin.RequestsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CommunityViewHolder(itemView: View, private val communities: List<String>, private val selectedConstituency: String) : RecyclerView.ViewHolder(itemView) {
    val communityName: TextView = itemView.findViewById(R.id.community_name)

    init {
        itemView.setOnClickListener {
            val position: Int = adapterPosition
            val community = communities[position]

            if (community == selectedConstituency) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                val requestRef = FirebaseDatabase.getInstance().getReference("Requests")

                requestRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(itemView.context, "You have already sent a request", Toast.LENGTH_SHORT).show()
                        } else {
                            //val requestId = requestRef.push().key
                            val request = hashMapOf(
                                "userId" to userId,
                                "constituency" to community,
                                "approved" to false
                            )
                            //requestId?.let { requestRef.child(it).setValue(request) }
                            requestRef.child(userId!!).setValue(request)
                            Toast.makeText(itemView.context, "Request sent to admin", Toast.LENGTH_SHORT).show()

                            //val intent = Intent(itemView.context, RequestsActivity::class.java)
                            //itemView.context.startActivity(intent)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        //possible errors
                    }
                })
            } else {
                Toast.makeText(itemView.context, "You can only click on $selectedConstituency", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
