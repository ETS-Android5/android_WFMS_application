package com.wfms.nectar.wfms

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.leave_summary_layout.*
import android.app.ProgressDialog
import android.content.Intent
import android.widget.Toast
import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImpl
import com.wfms.nectar.utils.AppConstants
import com.wfms.nectar.utils.NetworkUtil
import com.wfms.nectar.utils.PrefUtils
import com.wfms.nectar.viewstate.SignUpView


class LeaveSummaryActivity  : AppCompatActivity(),SignUpView {
    override fun onSignUpSuccess(signUpResponse: SignUpResponse?) {
        dialog.dismiss()
        if (signUpResponse != null) {
            total_leaves.text=signUpResponse.availableleave
            pending_leaves.text=signUpResponse.pendingleave
            approve_leaves.text=signUpResponse.approvedleave
            rejected_leaves.text=signUpResponse.rejectedleave
            leave=signUpResponse.availableleave
            pendingleave=signUpResponse.pendingleave
            approvedleave=signUpResponse.approvedleave
            rejectedleave=signUpResponse.rejectedleave

            piechart()
        }
    }

    override fun onSignUpFailure(msg: String?) {
        dialog.dismiss()
    }

    var entries: ArrayList<Entry>? = null
    var PieEntryLabels: ArrayList<String>? = null
    var pieDataSet: PieDataSet? = null
    var pieData: PieData? = null
    var leave :String= ""
    var pendingleave :String= ""
    var approvedleave :String= ""
    var rejectedleave :String= ""

    internal lateinit var dialog: ProgressDialog
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.leave_summary_layout)
      //  piechart()
        getleaves()
        back_layout_leaves.setOnClickListener { view ->
            finish()
        }

        leave_layout.setOnClickListener { view ->
            val i4 = Intent(this, LeaveActivity::class.java)
            startActivity(i4)
            finish()
        }
}

    private fun getleaves() {
        if (NetworkUtil.isOnline(getApplicationContext())) {
            dialog = ProgressDialog(this, R.style.AppCompatAlertDialogStyle)
            dialog.setMessage(resources.getString(R.string.loading))
            dialog.show()
              //initavailableleavesAPIResources("rekha.lokhande@nectarinfotel.com")
            initavailableleavesAPIResources(PrefUtils.getKey(this, AppConstants.Username),PrefUtils.getKey(this, AppConstants.Api_Token))
        }
        else
        {
            Toast.makeText(this,"Please check internet connection", Toast.LENGTH_SHORT).show()
        }
    }
    private fun initavailableleavesAPIResources(name: String,apitoken: String) {
        val loginPresenter = SignUpPresenterImpl(this)
        loginPresenter.callApi(AppConstants.Available_Leaves, name,apitoken)
    }
    private fun piechart() {
        entries = ArrayList<Entry>()

        PieEntryLabels = ArrayList()

        AddValuesToPIEENTRY()

        AddValuesToPieEntryLabels()

        pieDataSet = PieDataSet(entries, "")

        pieData = PieData(PieEntryLabels, pieDataSet)

        pieDataSet!!.setColors(ColorTemplate.COLORFUL_COLORS)

        pieChart.setData(pieData)
        pieChart.setDescription("")
        pieChart.getLegend().setEnabled(false);
        pieChart.animateY(3000)
    }
    fun AddValuesToPIEENTRY() {
       /* entries?.add(BarEntry(2f, 0))
        entries?.add(BarEntry(4f, 1))
        entries?.add(BarEntry(6f, 2))
        entries?.add(BarEntry(8f, 3))*/
        leave= total_leaves.text.toString()
        entries?.add(BarEntry(rejectedleave.toFloat(), 0))
        entries?.add(BarEntry(approvedleave.toFloat(), 1))
        entries?.add(BarEntry(pendingleave.toFloat(), 2))
        entries?.add(BarEntry(leave.toFloat(), 3))

    }
    fun AddValuesToPieEntryLabels() {
       /* PieEntryLabels?.add("Rejected Leaves")
        PieEntryLabels?.add("Approve Leaves")
        PieEntryLabels?.add("Pending Leaves")
        PieEntryLabels?.add("Total Balance")*/

        PieEntryLabels?.add("")
        PieEntryLabels?.add("")
        PieEntryLabels?.add("")
        PieEntryLabels?.add("")
    }
}