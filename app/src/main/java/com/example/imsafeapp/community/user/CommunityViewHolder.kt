package com.example.imsafeapp.community.user

import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
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

                val intent = Intent(itemView.context, CommunityFeedActivity::class.java)
                itemView.context.startActivity(intent)

            } else {
                Toast.makeText(itemView.context, "You can only click on $selectedConstituency", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
