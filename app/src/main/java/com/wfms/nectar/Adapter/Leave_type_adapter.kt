package com.wfms.nectar.Adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wfms.nectar.jsonModelResponses.Fuel.LeaveData

import com.wfms.nectar.wfms.R
import kotlinx.android.synthetic.main.leave_type_item.view.*

class Leave_type_adapter(val strings: ArrayList<LeaveData>, val context: Context) : RecyclerView.Adapter<Leave_type_adapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(strings[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.leave_type_item, parent, false))
    }
    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return strings.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(items: LeaveData) {
            itemView.tv_leave_type.text = "Leave type : "+items.leavetype_name
            itemView.tv_reason.text = "Reason :"+items.reason
            itemView.tv_fromdate.text = "From Date :"+items.from_date
            itemView.tv_todate.text ="To Date :"+ items.to_date
            itemView.tv_dayes.text = "Total Days : "+items.appliedleavescount

        }
    }
}