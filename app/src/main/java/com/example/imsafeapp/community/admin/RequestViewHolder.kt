package com.example.imsafeapp.community.admin

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.imsafeapp.R

class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val constituencyName: TextView = itemView.findViewById(R.id.constituency_name)
    val approveButton: Button = itemView.findViewById(R.id.approve_button)
}
