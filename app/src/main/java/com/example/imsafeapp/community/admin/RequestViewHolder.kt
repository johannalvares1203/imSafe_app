package com.example.imsafeapp.community.admin

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.imsafeapp.R

class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val userIdText: TextView = itemView.findViewById(R.id.user_id_text)
    val approveButton: Button = itemView.findViewById(R.id.approve_button)
    val btnCancal: ImageView = itemView.findViewById(R.id.btnCancal)
}
