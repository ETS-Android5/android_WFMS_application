package com.wfms.nectar.Adapter

import androidx.fragment.app.FragmentPagerAdapter
import android.content.Context;

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.wfms.nectar.wfms.ApproveLeavesFragment
import com.wfms.nectar.wfms.PendingLeavesFragment
import com.wfms.nectar.wfms.RejectedLeavesFragment


class Leave_Adapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> {
                return PendingLeavesFragment()
            }
            1 -> {
                return ApproveLeavesFragment()
            }
            2 -> {
                return RejectedLeavesFragment()
            }
            else -> return null
        }
    }
    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}