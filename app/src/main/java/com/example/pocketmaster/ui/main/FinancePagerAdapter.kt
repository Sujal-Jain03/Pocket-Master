package com.example.pocketmaster.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pocketmaster.ui.dashboard.DashboardFragment
import com.example.pocketmaster.ui.transactions.TransactionFragment

class FinancePagerAdapter(activity: FragmentActivity):
    FragmentStateAdapter(activity){
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> DashboardFragment()
            1 -> TransactionFragment()
            else -> throw IllegalArgumentException("Invalid Position $position")
        }
    }

    override fun getItemCount(): Int =2

}