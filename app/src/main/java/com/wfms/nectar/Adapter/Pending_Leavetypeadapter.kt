package com.wfms.nectar.Adapter

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.wfms.nectar.jsonModelResponses.Fuel.LeaveData
import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImpl
import com.wfms.nectar.utils.AppConstants
import com.wfms.nectar.utils.NetworkUtil
import com.wfms.nectar.viewstate.SignUpView
import com.wfms.nectar.wfms.LeaveActivity

import com.wfms.nectar.wfms.R
import kotlinx.android.synthetic.main.leave_type_item.view.*
import android.app.Activity
import com.wfms.nectar.utils.PrefUtils


class Pending_Leavetypeadapter(val strings: ArrayList<LeaveData>, val context: Context) : RecyclerView.Adapter<Pending_Leavetypeadapter.ViewHolder>(), SignUpView {
    internal lateinit var dialog: ProgressDialog
    var position1:Int = 0
    override fun onSignUpSuccess(signUpResponse: SignUpResponse?) {
        Toast.makeText(context, "Leave Successfully cancel", Toast.LENGTH_SHORT).show()
       // strings.removeAt(position1)
       // notifyDataSetChanged()

        val i = Intent(context, LeaveActivity::class.java)
        context.startActivity(i)
        (context as Activity).finish()
    }

    override fun onSignUpFailure(msg: String?) {
    }

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
            itemView.cancel_layout.visibility=View.VISIBLE
            itemView.tv_leave_type.text = "Leave type : "+items.leavetype_name
            itemView.tv_reason.text = "Reason :"+items.reason
            itemView.tv_fromdate.text = "From Date :"+items.from_date
            itemView.tv_todate.text ="To Date :"+ items.to_date
            itemView.tv_dayes.text = "Total Days : "+items.appliedleavescount
            itemView.cancel_layout.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {
                    if (NetworkUtil.isOnline(context)) run {
                     /*   dialog = ProgressDialog(appli, R.style.AppCompatAlertDialogStyle)
                        dialog.setMessage(context.getResources().getString(R.string.loading))
                        dialog.show()*/
                      position1=position
                        Log.d("Leaveid",""+position1)
                       initcancelAPIResources(items.leaveid)
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show()
                    }
                }})
        }
    }

    private fun initcancelAPIResources(leaveid: String) {
        val loginPresenter = SignUpPresenterImpl(this)
        loginPresenter.callApi(AppConstants.Leave_Cancel,leaveid, PrefUtils.getKey(context, AppConstants.Api_Token),PrefUtils.getKey(context, AppConstants.Username))
    }
}