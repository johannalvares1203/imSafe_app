package com.example.imsafeapp.community.admin

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imsafeapp.R

class RequestAdapter(private val requests: List<Request>) : RecyclerView.Adapter<RequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_request, parent, false)
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = requests[position]
        holder.constituencyName.text = request.constituency
        holder.approveButton.setOnClickListener {
            Log.d("AAA", "approve btn clicked")
        }
    }

    override fun getItemCount() = requests.size
}
