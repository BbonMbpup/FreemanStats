package com.example.freemanstats.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.freemanstats.presentation.tableview.StatsFragment
import com.example.freemanstats.presentation.currentwarpage.CurrentWarFragment
import com.example.freemanstats.presentation.historypage.HistoryFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CurrentWarFragment()
            1 -> StatsFragment()
            2 -> HistoryFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}