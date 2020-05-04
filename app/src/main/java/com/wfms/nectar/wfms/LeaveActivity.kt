package com.wfms.nectar.wfms

import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import com.wfms.nectar.Adapter.Leave_Adapter
import kotlinx.android.synthetic.main.leave_layout.*

class LeaveActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.leave_layout)
       //tabLayout!!.addTab(tabLayout!!.newTab().setText("Available Leaves"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Pending Leaves"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Approve Leaves"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Rejected Leaves"))

        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = Leave_Adapter(this, supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        fab.setOnClickListener { view ->
            intent = Intent(applicationContext, LeaveRequestActivity::class.java)
            startActivity(intent)
            finish()
        }
        back_layout1.setOnClickListener { view ->
            intent = Intent(applicationContext, LeaveSummaryActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        intent = Intent(applicationContext, LeaveSummaryActivity::class.java)
        startActivity(intent)
        finish()

    }
}