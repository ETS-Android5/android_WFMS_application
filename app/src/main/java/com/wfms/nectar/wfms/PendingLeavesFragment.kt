package com.wfms.nectar.wfms

import androidx.fragment.app.Fragment
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.wfms.nectar.Adapter.Pending_Leavetypeadapter
import com.wfms.nectar.jsonModelResponses.Fuel.LeaveData
import com.wfms.nectar.jsonModelResponses.Fuel.LeaveResponse
import com.wfms.nectar.presenter.presenterImpl.GetAlLeavePresenterImpl
import com.wfms.nectar.utils.AppConstants
import com.wfms.nectar.utils.NetworkUtil
import com.wfms.nectar.utils.PrefUtils
import com.wfms.nectar.viewstate.LeaveView
import kotlinx.android.synthetic.main.leave_item_layout.*
import kotlinx.android.synthetic.main.leave_item_layout.view.*
import java.util.*
import kotlin.collections.ArrayList


class PendingLeavesFragment : Fragment(), LeaveView {
    override fun onGeLeaveListSuccess(leaveResponse: LeaveResponse?) {
       progressbar_leave.visibility== View.GONE
        pullToRefresh_leave.isRefreshing=false
        try{
            if (leaveResponse != null) {
                arrayList = ArrayList<LeaveData>()
                arrayList.addAll(leaveResponse.leaverData)

                arrayList1.addAll(leaveResponse.leaverData)
                Collections.reverse(arrayList);
            }
                  Log.d("arralist",""+arrayList.size)
                  leave_list.adapter = activity?.applicationContext?.let { Pending_Leavetypeadapter(arrayList, it) }
                  leave_list.setItemAnimator(DefaultItemAnimator())
        }catch(e: Exception){

        }
    }

    override fun onGetLeaveListListFailure(msg: String?) {
        progressbar_leave.visibility== View.GONE
        pullToRefresh_leave.isRefreshing=false
        Toast.makeText(activity,"Please try again",Toast.LENGTH_SHORT).show()
    }
    companion object {
        val  arrayList1 = ArrayList<LeaveData>()
    }
    var arrayList = ArrayList<LeaveData>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the leave_item_layout for this fragment
        var view : View  = inflater.inflate(R.layout.leave_item_layout, container, false)
        view. leave_list.layoutManager = LinearLayoutManager(activity)
        // You can use GridLayoutManager if you want multiple columns. Enter the number of columns as a parameter.
        // Access the RecyclerView Adapter and load the data into it
        //get list for pending

        if(arrayList1.size>0)
        {
            arrayList1.clear()
        }

        view. pullToRefresh_leave.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            // Your code to refresh the list here.
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            if(arrayList1.size>0)
            {
                arrayList1.clear()
            }

            pullToRefresh_leave.isRefreshing=true
            getpendinglist()
        })

      view.progressbar_leave.visibility== View.VISIBLE
         getpendinglist()
        return view
    }

    private fun getpendinglist() {
        if (NetworkUtil.isOnline(activity))
        {
          // initgetpendinglist("rekhalokhande504@gmail.com")
            initgetpendinglist(PrefUtils.getKey(activity,AppConstants.Username),PrefUtils.getKey(activity,AppConstants.Api_Token))
        }
        else
        {
            Toast.makeText(activity,"Please check internet connection", Toast.LENGTH_SHORT).show()
        }
}

    private fun initgetpendinglist(email: String,apitoken: String) {

        val getAllleavePresenter = GetAlLeavePresenterImpl(this)

        getAllleavePresenter.callApi(AppConstants.Pending_List, email,apitoken)

       /* val getAllleavePresenter1 = GetAlLeavePresenterImpl(this)
        getAllleavePresenter1.callApi(AppConstants.Pending_List1, email)*/
    }
}





