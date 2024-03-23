package com.example.imsafeapp.chat.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.imsafeapp.chat.fragment.OfflineFragment
import com.example.imsafeapp.chat.fragment.OnlineFragment

class MyViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnlineFragment()
            1 -> OfflineFragment()
            else -> OnlineFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}