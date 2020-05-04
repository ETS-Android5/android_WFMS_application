package com.wfms.nectar.wfms





import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.leave_request_layout.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.EditText
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wfms.nectar.jsonModelResponses.Fuel.LeaveData


import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImpl
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImplOut
import com.wfms.nectar.utils.AppConstants

import com.wfms.nectar.utils.NetworkUtil
import com.wfms.nectar.utils.PrefUtils
import com.wfms.nectar.viewstate.SignUpView
import com.wfms.nectar.viewstate.SignUpViewOut
import kotlinx.android.synthetic.main.calanderview.*
import java.time.LocalDate
import kotlin.collections.ArrayList

class LeaveRequestActivity :AppCompatActivity(), SignUpView, SignUpViewOut, DatePickerDialog.OnDateSetListener {
    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
       // ed_fdate.setText("" + year + "-" + (year+1) + "-" + dayOfMonth)
    }


    override fun onSignUpSuccessOut(signUpResponse: SignUpResponse?) {
        dialog.dismiss()
        if (signUpResponse?.msg.equals("hr is not configured yet")) {
            Toast.makeText(this, "hr is not configured yet", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this, "Leaves successfully applied", Toast.LENGTH_SHORT).show()
            intent = Intent(applicationContext, LeaveActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onSignUpFailureOut(msg: String?) {
        dialog.dismiss()
        Toast.makeText(this,"Please try again",Toast.LENGTH_SHORT).show()
    }
    val ONE_DAY = 24 * 60 * 60 * 1000L
    val leave_types = arrayOf("Select","casual", "sick", "Compoff","LWP")
    val leave_for = arrayOf("Select","Full day", "Half day")
    val DATE_FORMAT = "dd-MM-yyyy";
    var leavetype=""
    var leavefor=""
    var availableleaves=""
    var count=0
    var count1=0
    var ed_fdate: EditText? = null
    var ed_edate: EditText? = null
    val arrayList = ArrayList<LocalDate>()
    var datelist: ArrayList<String> = ArrayList()
    var datelist1: ArrayList<String> = ArrayList()
    var numberOfDays: Long = 0
    var numberOfDays1: Int = 0
    var numberOfDays2: Int = 0
    var isapply: Boolean=false;
    var isweekend: Boolean=false;
    var isweek: Boolean? =null;
    var list1: ArrayList<String> = ArrayList()
    internal lateinit var dialog: ProgressDialog
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.leave_request_layout)
        //set array for leavetype
        ed_fdate = findViewById(R.id.ed_fdate) as EditText
        ed_edate = findViewById(R.id.ed_edate) as EditText

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, leave_types)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ed_leavetype.adapter = arrayAdapter

        //set array for leavefor
        val arrayAdapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, leave_for)
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ed_leavefor.adapter = arrayAdapter1


        back_layout.setOnClickListener { view ->
            finish()
        }

        select_date_layout!!.setOnClickListener {

            showDialog()


        }
        ed_leavetype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                leavetype=leave_types[position]
                Log.d("leavetype",leavetype)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }

        ed_leavefor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                leavefor=leave_for[position]
               /* ed_fdate?.setText("")
                ed_edate?.setText("")
                days?.setText("0")
                date_layout.visibility=View.GONE
                Log.d("leavefor",leavefor)*/
                if(leavefor.equals("Half day"))
                {
                    if(list1.size>0)
                    {
                        days?.setText("0.5")
                        ed_fdate?.setText((list1.get(0).toString()))
                        ed_edate?.setText(list1.get(0).toString())
                    }
                } else
                {
                    if(datelist1.size>0)
                    {
                        days?.setText(""+count)
                        ed_fdate?.setText((list1.get(0).toString()))
                        ed_edate?.setText(list1.get(list1.size-1).toString())
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }

        getavailableleaves();

        apply_button.setOnClickListener {


            if (leavetype.equals("Select"))
            {
                Toast.makeText(this,"Please select leave type",Toast.LENGTH_SHORT).show()
            }
            else if (leave_for.equals("Select"))
            {
                Toast.makeText(this,"Please select leave for",Toast.LENGTH_SHORT).show()
            } else  if (ed_fdate!!.text.toString().length==0)
        {
            Toast.makeText(this,"Please enter leave date",Toast.LENGTH_SHORT).show()
        }
            else if (reason.text.toString().length==0)
            {
                Toast.makeText(this,"Please enter reason",Toast.LENGTH_SHORT).show()
            }
            else
            {
                applyleave()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)

    private fun showDialog() {
        isapply=false
        isweekend=false
        val dialog = Dialog(this)
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE)
       // dialog .setCancelable(false)
        dialog .setContentView(R.layout.calanderview)
        dialog.calendarView.selectedDates = getSelectedDays()
       dialog.calendarView.setDisabledDays(getSelectedDays())
        count=0
        count1=0
        var list: ArrayList<String> = ArrayList()

        datelist1 = arrayListOf<String>()
        list1= arrayListOf<String>()

        dialog .getDateButton.setOnClickListener {
            for (calendar in dialog.calendarView.selectedDates) {
                println(calendar.time.toString())
                val postFormater = SimpleDateFormat("yyyy-MM-dd")
                val newDateStr = postFormater.format( calendar.time)

                list.add(newDateStr)
                numberOfDays2=0
                val aTimefrom = list.get(0).toString()
                val aTimeto = list.get(list.size - 1).toString()
                Log.d("from",""+aTimefrom)
                Log.d("aTimeto",""+aTimeto)
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


                val date1 = sdf.parse(aTimefrom)
                val date2 = sdf.parse(aTimeto)
                val cal1 = Calendar.getInstance()
                val cal2 = Calendar.getInstance()
                cal1.time = date1
                cal2.time = date2

                while (cal1.before(cal2)) {
                    numberOfDays2++
                    cal1.add(Calendar.DATE, 1)

                    //  calendars.add(cal1)
                }
                try {
                    val cal = Calendar.getInstance()
                    for (i in 0..numberOfDays2) {
                        val dt = list.get(0).toString()  // Start date
                        val sdf = SimpleDateFormat("yyyy-MM-dd")
                        val c = Calendar.getInstance()
                        val c1 = Calendar.getInstance()
                        try {
                            c.time = sdf.parse(dt)
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                        c.add(Calendar.DATE, i)
                        Log.d("fdgfdgfh",""+sdf.format(c.getTime()))
                        datelist1.add(sdf.format(c.getTime()))




                    }
                } catch (e: ParseException) {
                    e.printStackTrace()
                }


                for (item  in datelist1.indices)
                {
                    val date1 =datelist1.get(item)
                    for (item  in datelist.indices)
                    {
                        val date =datelist.get(item)
                        Log.d("date",""+date)
                        if(date1.equals(date))
                        {
                            isapply=true
                        }
                    }


                }

                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY||calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

                    isweekend=true
                    isweek=false
                }else
                {
                    list1.add(newDateStr)
                    isweek=true
                    count++

                }
            }
            if(isweek==true)
            {
            if(isapply==false)
            {
                Log.d("count1111111",""+count)
            if(count<=2) {
                if (list1.size > 0) {
                    date_layout.visibility= View.VISIBLE
                 //   getDatesBetween(list.get(0).toString(),list.get(list.size - 1).toString());

                    if(leavefor.equals("Half day"))
                    {
                        ed_fdate?.setText((list1.get(0).toString()))
                        ed_edate?.setText(list1.get(0).toString())
                        days.setText("0.5")
                    }
                    else
                    {
                        ed_fdate?.setText((list1.get(0).toString()))
                        ed_edate?.setText(list1.get(list1.size - 1).toString())
                        days.setText("" + count)
                    }

                    dialog.dismiss()
                }
                else
                {
                    date_layout.visibility= View.GONE
                    Toast.makeText(applicationContext,
                            "You can not apply leave on weekends",
                            Toast.LENGTH_SHORT).show()
                    dialog .dismiss()
                    ed_fdate?.setText("")
                    ed_edate?.setText("")
                    days.setText("0")
                }
            }
            else
            {
                date_layout.visibility= View.GONE
                Toast.makeText(applicationContext,
                        "You can apply only two leaves",
                        Toast.LENGTH_SHORT).show()
                dialog .dismiss()
                ed_fdate?.setText("")
                ed_edate?.setText("")
                days.setText("0")

            }}
            else
            {
                date_layout.visibility= View.GONE
                Toast.makeText(applicationContext,
                        "Please select proper date",
                        Toast.LENGTH_SHORT).show()
                dialog .dismiss()
                ed_fdate?.setText("")
                ed_edate?.setText("")
                days.setText("0")
            }

        }    else if(isweek==false)
            {
              /*  Log.d("count1",""+count1)
                if(count1==3)
                {
                    date_layout.visibility= View.VISIBLE
                    ed_fdate?.setText((datelist1.get(0).toString()))
                    ed_edate?.setText(datelist1.get(4).toString())
                    days.setText("" + 2)
                    dialog .dismiss()
                }
                else
                {*/

                    date_layout.visibility = View.GONE
                    Toast.makeText(applicationContext,
                            "You can not apply leave on weekends",
                            Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    ed_fdate?.setText("")
                    ed_edate?.setText("")
                    days.setText("")
                   /* if(numberOfDays2>2)
                    {
                        date_layout.visibility= View.GONE
                        Toast.makeText(applicationContext,
                                "You can apply only two leaves",
                                Toast.LENGTH_SHORT).show()
                        dialog .dismiss()
                        ed_fdate?.setText("")
                        ed_edate?.setText("")
                        days.setText("0")
                    }
                    else {
                        date_layout.visibility = View.GONE
                        Toast.makeText(applicationContext,
                                "You can not apply leave on weekends",
                                Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        ed_fdate?.setText("")
                        ed_edate?.setText("")
                        days.setText("")
                    }*/
               // }

            }else
            {
                date_layout.visibility = View.GONE
                Toast.makeText(applicationContext,
                        "Please select date",
                        Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                ed_fdate?.setText("")
                ed_edate?.setText("")
                days.setText("")
            }}


        dialog .show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getSelectedDays(): List<Calendar> {
        val calendars = ArrayList<Calendar>()
        datelist = arrayListOf<String>()
        val  arrayList3 = ArrayList<LeaveData>()
        if(PendingLeavesFragment.arrayList1.size>0)
        {
            arrayList3.addAll(PendingLeavesFragment.arrayList1)
        }
        if(ApproveLeavesFragment.arrayList2.size>0)
        {
            arrayList3.addAll(ApproveLeavesFragment.arrayList2)
        }
        for (item  in arrayList3.indices) {
            numberOfDays1=0
            val aTimefrom = arrayList3.get(item).from_date
            val aTimeto = arrayList3.get(item).to_date
            Log.d("from1111111",""+aTimefrom)
            Log.d("aTimeto11111",""+aTimeto)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
           // getDaysBetweenDates(aTimefrom,aTimeto)

          //  Log.d("dfgdfgdfgfdgfd",""+numberOfDays1)

            val date1 = sdf.parse(aTimefrom)
            val date2 = sdf.parse(aTimeto)
            val cal1 = Calendar.getInstance()
            val cal2 = Calendar.getInstance()
            cal1.time = date1
            cal2.time = date2

            while (cal1.before(cal2)) {
                    numberOfDays1++
                cal1.add(Calendar.DATE, 1)

              //  calendars.add(cal1)
            }

            try {
                val cal = Calendar.getInstance()

                for (i in 0..numberOfDays1) {

                    val dt = aTimefrom.toString()  // Start date
                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    val c = Calendar.getInstance()
                    try {
                        c.time = sdf.parse(dt)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }

                    c.add(Calendar.DATE, i)
                    calendars.add(c)
                    Log.d("fdgfdgfh",""+sdf.format(c.getTime()))
                    datelist.add(sdf.format(c.getTime()))

                }
               // cal.time = sdf.parse(aTimefrom.toString())
              //  calendars.add(cal)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

        }

        return calendars
    }
    private fun applyleave() {
        if (NetworkUtil.isOnline(getApplicationContext())) {
            dialog = ProgressDialog(this, R.style.AppCompatAlertDialogStyle)
            dialog.setMessage(resources.getString(R.string.loading))
            dialog.show()
            Log.d("leavefor111",leavefor)
            initapplyleavesAPIResources(PrefUtils.getKey(this,AppConstants.Username),availableleaves,leavetype,reason.text.toString(), ed_fdate?.text.toString(), ed_edate?.text.toString(),leavefor,"2","2",days.text.toString())
          // initapplyleavesAPIResources("rekhalokhande504@gmail.com",availableleaves,leavetype,reason.text.toString(),ed_fdate.text.toString(),ed_edate.text.toString(),leavefor,"2","2",days.text.toString())

        }
        else
        {
            Toast.makeText(this,"Please check internet connection",Toast.LENGTH_SHORT).show()
        }
    }
    private fun getavailableleaves() {
        if (NetworkUtil.isOnline(getApplicationContext())) {
            dialog = ProgressDialog(this, R.style.AppCompatAlertDialogStyle)
            dialog.setMessage(resources.getString(R.string.loading))
            dialog.show()
           initavailableleavesAPIResources(PrefUtils.getKey(this,AppConstants.Username),PrefUtils.getKey(this,AppConstants.Api_Token))
        }
        else
        {
            Toast.makeText(this,"Please check internet connection",Toast.LENGTH_SHORT).show()
        }
    }
    private fun initavailableleavesAPIResources(name: String,apitoken: String) {
        val loginPresenter = SignUpPresenterImpl(this)
        loginPresenter.callApi(AppConstants.Available_Leaves, name,apitoken)
    }
    private fun initapplyleavesAPIResources(email: String, availableleaves: String, leaveTypes: String, reason: String, startdate: String, enddate: String, leaveFor: String, day: String, reporintmanager: String, totalcount: String) {
        val signupPresenter = SignUpPresenterImplOut(this)

        signupPresenter.callApi(AppConstants.Applyleaves, email.trim(),availableleaves,leaveTypes,reason.trim(),startdate,enddate,leaveFor.trim(),day,reporintmanager,totalcount,PrefUtils.getKey(this,AppConstants.Api_Token))

    }



    override fun onSignUpSuccess(signUpResponse: SignUpResponse?) {
        dialog.dismiss()
        if (signUpResponse != null) {
            available_leaves.text=signUpResponse.availableleave
            availableleaves=signUpResponse.availableleave
        }
    }

    override fun onSignUpFailure(msg: String?) {

    }

    override fun onBackPressed() {
        intent = Intent(applicationContext, LeaveActivity::class.java)
        startActivity(intent)
        finish()
    }

}



