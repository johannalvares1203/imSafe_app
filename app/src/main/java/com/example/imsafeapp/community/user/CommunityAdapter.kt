package com.example.imsafeapp.community.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imsafeapp.R

class CommunityAdapter(private val communities: List<String>, private val selectedConstituency: String) : RecyclerView.Adapter<CommunityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_community, parent, false)
        return CommunityViewHolder(view, communities, selectedConstituency)
    }

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        val community = communities[position]
        holder.communityName.text = community
    }

    override fun getItemCount() = communities.size
}
