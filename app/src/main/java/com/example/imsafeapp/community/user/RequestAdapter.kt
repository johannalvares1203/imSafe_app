package com.example.imsafeapp.community.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.imsafeapp.R

class RequestAdapter(private val requestList: List<Request>) : RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

    inner class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNameTextView: TextView = itemView.findViewById(R.id.userNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_request_user, parent, false)
        return RequestViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val currentItem = requestList[position]
        holder.userNameTextView.text = "Name: ${currentItem.userName}"
    }

    override fun getItemCount() = requestList.size
}