package com.wfms.nectar.wfms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.wfms.nectar.Adapter.User_Adapter;
import com.wfms.nectar.jsonModelResponses.Fuel.EmployeData;
import com.wfms.nectar.jsonModelResponses.Fuel.EmployeResponse;
import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse;
import com.wfms.nectar.presenter.presenterImpl.GetAlEmployePresenterImpl;
import com.wfms.nectar.presenter.presenterImpl.SignUpPresenterImpl;
import com.wfms.nectar.sql.DatabaseHelper;
import com.wfms.nectar.utils.AppConstants;
import com.wfms.nectar.utils.NetworkUtil;
import com.wfms.nectar.utils.PrefUtils;
import com.wfms.nectar.viewstate.EmployeView;
import com.wfms.nectar.viewstate.SignUpView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nectar on 26-06-2019.
 */

public class DashboardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SignUpView, EmployeView {
    public boolean isdate = false;
    PieChart pieChartAttendance;
    Button search;
    float totalAttendance;
    float presentCount;
    float weekendcount;
    float absentCount = 5;
    int yearid,yearid1;
    RelativeLayout back, user_listlayout;
    Spinner month_list, user_list, year_list;
    String year, currentmonth, monthname,selecedyear,username;
    String month_id, enddate;
    TextView pcount, acount, wcount;
    int absentdays, weekenddays;
    LinearLayout pa_layout;
    ProgressDialog dialog;
    int monthid, monthposition;
    DatabaseHelper databaseHelper;
    int previousmonth;
    ArrayList<EmployeData> employelist = new ArrayList();
    ArrayList<SignUpResponse> records = new ArrayList<>();
    String userid, startdate,selecteduserid;
    ProgressBar progressbar1;
    int Presentdays;
    String[] user_array = {"Select User", "Apeksha", "Rekha", "Rashmi", "Prasad", "vidhyadhar", "Mayuri"};
    String[] year_array = {"2020","2019"};
    String[] month = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dashboard_layout);
        pieChartAttendance = (PieChart) findViewById(R.id.pieChartAttendance);
        month_list = (Spinner) findViewById(R.id.month_list);
        user_list = (Spinner) findViewById(R.id.user_list);
        year_list = (Spinner) findViewById(R.id.year_list);
        back = (RelativeLayout) findViewById(R.id.back_layout);
        user_listlayout = (RelativeLayout) findViewById(R.id.user_listlayout);
        pa_layout = (LinearLayout) findViewById(R.id.pa_layout);
        pcount = (TextView) findViewById(R.id.pcount_text);
        acount = (TextView) findViewById(R.id.acount_text);
        wcount = (TextView) findViewById(R.id.wcount_text);
        progressbar1 = (ProgressBar) findViewById(R.id.progressbar1);
        search=(Button)findViewById(R.id.search);
        month_list.setOnItemSelectedListener(this);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PrefUtils.getKey(DashboardActivity.this, AppConstants.UserID).equalsIgnoreCase("1")) {
                    finish();
                } else {
                    Intent i = new Intent(DashboardActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PrefUtils.getKey(DashboardActivity.this, AppConstants.UserID).equals("1"))
                {
                if(username.equalsIgnoreCase("Select User"))
                {
                   // pieChartAttendance.setVisibility(View.GONE);
                    pa_layout.setVisibility(View.GONE);
                    progressbar1.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Please select user", Toast.LENGTH_SHORT).show();
                }
                else {


                    int newmonth = monthid;
                    Log.d("newmonth", "" + newmonth);
                    Log.d("previousmonth", "" + previousmonth);
                    Log.d("month_id", "" + month_id);
                    Log.d("selecedyear", "" + year);
                    if(yearid<yearid1)
                    {
                        //  pa_layout.setVisibility(View.VISIBLE);
                        month_id = "" + monthid;
                        if (NetworkUtil.isOnline(getApplicationContext())) {
                            initGetAttandenceAPIResources(PrefUtils.getKey(DashboardActivity.this, AppConstants.UserID), PrefUtils.getKey(DashboardActivity.this, AppConstants.Clientid), month_id, year, PrefUtils.getKey(DashboardActivity.this, AppConstants.Api_Token),selecteduserid);
                        } else {
                            senddataTimeInfromSqlite();
                            progressbar1.setVisibility(View.GONE);

                        }
                    }
                    else{
                        if (newmonth > previousmonth) {
                            pieChartAttendance.setVisibility(View.GONE);
                            pa_layout.setVisibility(View.GONE);
                            progressbar1.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Please select correct month", Toast.LENGTH_SHORT).show();
                        } else {
                            //  pa_layout.setVisibility(View.VISIBLE);
                            month_id = "" + monthid;
                        if (NetworkUtil.isOnline(getApplicationContext())) {
                            initGetAttandenceAPIResources(PrefUtils.getKey(DashboardActivity.this, AppConstants.UserID), PrefUtils.getKey(DashboardActivity.this, AppConstants.Clientid), month_id, year, PrefUtils.getKey(DashboardActivity.this, AppConstants.Api_Token),selecteduserid);
                        } else {
                            senddataTimeInfromSqlite();
                            progressbar1.setVisibility(View.GONE);

                        }
                        }}


                }}else

                {
                    int newmonth = monthid;
                    Log.d("newmonth", "" + newmonth);
                    Log.d("previousmonth", "" + previousmonth);
                    Log.d("month_id", "" + month_id);


                    Log.d("selecedyear", "" + year);
                    if(yearid<yearid1)
                    {
                        //  pa_layout.setVisibility(View.VISIBLE);

                        month_id = "" + monthid;
                        if (NetworkUtil.isOnline(getApplicationContext())) {
                            initGetAttandenceAPIResources(PrefUtils.getKey(DashboardActivity.this, AppConstants.UserID), PrefUtils.getKey(DashboardActivity.this, AppConstants.Clientid), month_id, year, PrefUtils.getKey(DashboardActivity.this, AppConstants.Api_Token),selecteduserid);
                        } else {
                            senddataTimeInfromSqlite();
                            progressbar1.setVisibility(View.GONE);

                        }
                    }
                    else{
                        if (newmonth > previousmonth) {
                            pieChartAttendance.setVisibility(View.GONE);
                            pa_layout.setVisibility(View.GONE);
                            progressbar1.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Please select correct month", Toast.LENGTH_SHORT).show();
                        } else {
                            //  pa_layout.setVisibility(View.VISIBLE);
                            month_id = "" + monthid;
                            if (NetworkUtil.isOnline(getApplicationContext())) {
                                initGetAttandenceAPIResources(PrefUtils.getKey(DashboardActivity.this, AppConstants.UserID), PrefUtils.getKey(DashboardActivity.this, AppConstants.Clientid), month_id, year, PrefUtils.getKey(DashboardActivity.this, AppConstants.Api_Token),selecteduserid);
                            } else {
                                senddataTimeInfromSqlite();
                                progressbar1.setVisibility(View.GONE);

                            }
                        }}



                }
            }
        });
        //Getting current date and year
        enddate = new SimpleDateFormat("dd-M-yyyy" +
                "" +
                "", Locale.US).format(new Date());
        String datetime[] = enddate.split("-");
        year = datetime[2];
        currentmonth = datetime[1];

        startdate = "1" + currentmonth + year;
        isdate = true;
        yearid1=2;
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.month_layout, month);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        month_list.setAdapter(aa);

        //set array for year
        ArrayAdapter yearadapter = new ArrayAdapter(this, R.layout.month_layout, year_array);
        yearadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        year_list.setAdapter(yearadapter);
     //   userid = PrefUtils.getKey(DashboardActivity.this, AppConstants.UserID);
        if (PrefUtils.getKey(DashboardActivity.this, AppConstants.UserID).equals("1")) {
            selecteduserid = "0";
            if (NetworkUtil.isOnline(getApplicationContext())) {
                dialog = new ProgressDialog(DashboardActivity.this, R.style.AppCompatAlertDialogStyle);
                dialog.setMessage(getResources().getString(R.string.loading));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                getEmployelist();
            } else {
                getuserdatafromSqlite();
            }


        } else {
            userid = PrefUtils.getKey(DashboardActivity.this, AppConstants.UserID);
            selecteduserid=PrefUtils.getKey(DashboardActivity.this, AppConstants.UserID);
        }
        if (PrefUtils.getKey(DashboardActivity.this, AppConstants.UserID).equals("1")) {
            // getEmployelist();
            user_listlayout.setVisibility(View.VISIBLE);
        } else {
            user_listlayout.setVisibility(View.GONE);
        }
        year_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year=year_array[i];
                Log.d("selecedyear", "" + year);
                if(year.equalsIgnoreCase("2019"))
                {
                    yearid=1;
                } else  if(year.equalsIgnoreCase("2020"))
                {
                    yearid=2;
                }

              //  pieChartAttendance.setVisibility(View.GONE);
                pa_layout.setVisibility(View.GONE);
                progressbar1.setVisibility(View.GONE);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        user_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 username = employelist.get(position).getUsername1();
                userid = employelist.get(position).getUser_id();
                selecteduserid= employelist.get(position).getUser_id();
                if (username.equalsIgnoreCase("Select User")) {

                } else {
                    // getdata(position);
                   // fetchdata();
                }
              //  pieChartAttendance.setVisibility(View.GONE);
                pa_layout.setVisibility(View.GONE);
                progressbar1.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fetchdata() {
        if (NetworkUtil.isOnline(getApplicationContext())) {
            initGetAttandenceAPIResources(PrefUtils.getKey(DashboardActivity.this, AppConstants.UserID), PrefUtils.getKey(DashboardActivity.this, AppConstants.Clientid), month_id, year,PrefUtils.getKey(DashboardActivity.this, AppConstants.Api_Token),selecteduserid);
        } else {
            senddataTimeInfromSqlite();
            progressbar1.setVisibility(View.GONE);
            // Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        monthname = month[position];

    //   if (!selecteduserid.equals("0")) {
         //   progressbar1.setVisibility(View.VISIBLE);
            getdata(position);
       // }
    }

   /* private void getweekend() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date d1 = dateFormat.parse(startdate);
            Date d2 = dateFormat.parse(enddate);
            Calendar c1 = Calendar.getInstance();
            c1.clear();
            c1.setTime(d1);

            Calendar c2 = Calendar.getInstance();
            c2.clear();
            c2.setTime(d2);
            int WeekEnds = 0;

            while(c2.after(c1)) {
                if(c1.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY||
                        c1.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY)
                    WeekEnds++;
                c1.add(Calendar.DATE,1);

            }
            weekenddays=WeekEnds;
            weekendcount=Float.parseFloat(WeekEnds);
            System.out.println("sundaysssssssss"+WeekEnds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }*/

    private void getdata(int position) {
        if (isdate == true) {
            if (Integer.parseInt(currentmonth) == 1) {
                monthposition = 0;
                monthid = monthposition + 1;
                previousmonth = monthid;
                month_id = "" + monthid;
            } else if (Integer.parseInt(currentmonth) == 2) {
                monthposition = 1;
                monthid = monthposition + 1;
                previousmonth = monthid;
                month_id = "" + monthid;
            } else if (Integer.parseInt(currentmonth) == 3) {
                monthposition = 2;
                monthid = monthposition + 1;
                previousmonth = monthid;
                month_id = "" + monthid;
            } else if (Integer.parseInt(currentmonth) == 4) {
                monthposition = 3;
                monthid = monthposition + 1;
                previousmonth = monthid;
                month_id = "" + monthid;
            } else if (Integer.parseInt(currentmonth) == 5) {
                monthposition = 4;
                monthid = monthposition + 1;
                previousmonth = monthid;
                month_id = "" + monthid;
            } else if (Integer.parseInt(currentmonth) == 6) {
                monthposition = 5;
                monthid = monthposition + 1;
                previousmonth = monthid;
                month_id = "" + monthid;
            } else if (Integer.parseInt(currentmonth) == 7) {
                monthposition = 6;
                monthid = monthposition + 1;
                previousmonth = monthid;
                month_id = "" + monthid;
            } else if (Integer.parseInt(currentmonth) == 8) {
                monthposition = 7;
                monthid = monthposition + 1;
                previousmonth = monthid;
                month_id = "" + monthid;
            } else if (Integer.parseInt(currentmonth) == 9) {
                monthposition = 8;
                monthid = monthposition + 1;
                previousmonth = monthid;
                month_id = "" + monthid;
            } else if (Integer.parseInt(currentmonth) == 10) {
                monthposition = 9;
                monthid = monthposition + 1;
                previousmonth = monthid;
                month_id = "" + monthid;
            } else if (Integer.parseInt(currentmonth) == 11) {
                monthposition = 10;
                monthid = monthposition + 1;
                previousmonth = monthid;
                month_id = "" + monthid;
            } else if (Integer.parseInt(currentmonth) == 12) {
                monthposition = 11;
                monthid = monthposition + 1;
                previousmonth = monthid;
                month_id = "" + monthid;
            }
            setid();
            isdate = false;

         /*   if (NetworkUtil.isOnline(getApplicationContext())) {
                initGetAttandenceAPIResources(PrefUtils.getKey(DashboardActivity.this, AppConstants.UserID), PrefUtils.getKey(DashboardActivity.this, AppConstants.Clientid), month_id, year, PrefUtils.getKey(DashboardActivity.this, AppConstants.Api_Token),selecteduserid);
            } else {
                senddataTimeInfromSqlite();
                progressbar1.setVisibility(View.GONE);
            }*/
        } else {
            monthid = position + 1;
            int newmonth = monthid;
            Log.d("position", "" + position);
            Log.d("newmonth", "" + newmonth);
            Log.d("previousmonth", "" + previousmonth);
            Log.d("month_id", "" + month_id);

           /* if (newmonth > previousmonth) {
                pieChartAttendance.setVisibility(View.GONE);
                pa_layout.setVisibility(View.GONE);
                progressbar1.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Please select correct month", Toast.LENGTH_SHORT).show();
            } else {
               pa_layout.setVisibility(View.VISIBLE);
                month_id = "" + monthid;
                if (NetworkUtil.isOnline(getApplicationContext())) {
                    initGetAttandenceAPIResources(PrefUtils.getKey(DashboardActivity.this, AppConstants.UserID), PrefUtils.getKey(DashboardActivity.this, AppConstants.Clientid), month_id, year, PrefUtils.getKey(DashboardActivity.this, AppConstants.Api_Token),selecteduserid);
                } else {
                    senddataTimeInfromSqlite();
                    progressbar1.setVisibility(View.GONE);

                }
            }*/

           // pieChartAttendance.setVisibility(View.GONE);
            pa_layout.setVisibility(View.GONE);
            progressbar1.setVisibility(View.GONE);
        }
    }

    private void initGetAttandenceAPIResources(String userid, String clientid, String monthid, String year,String Apitoken,String selecteduserid) {
        Log.d("uuuuuserid",userid);
        Log.d("selecteduserid",selecteduserid);
        Log.d("monthid",monthid);
        Log.d("year",year);
        SignUpPresenterImpl attandencePresenter = new SignUpPresenterImpl(DashboardActivity.this);
        attandencePresenter.callApi(AppConstants.Attandence, userid, clientid, monthid, year,Apitoken,selecteduserid);
    }

    private void setid() {
        month_list.setSelection(monthposition);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onSignUpSuccess(SignUpResponse signUpResponse) {
        records = new ArrayList<>();
        progressbar1.setVisibility(View.GONE);
        totalAttendance = Float.parseFloat(signUpResponse.getMonthdays());
        if (signUpResponse.getPresentdays().equals("8")) {
            presentCount = 0;
            Presentdays = 0;
        } else {
            presentCount = Float.parseFloat(signUpResponse.getPresentdays());
            Presentdays = Integer.parseInt(signUpResponse.getPresentdays());
        }
        weekendcount = Float.parseFloat(signUpResponse.getWeekend());
        absentCount = Float.parseFloat(signUpResponse.getAbsentdays());
        int total = Integer.parseInt(signUpResponse.getMonthdays());
        int absentdays = Integer.parseInt(signUpResponse.getAbsentdays());
        int weekenddays = Integer.parseInt(signUpResponse.getWeekend());

        SignUpResponse response = new SignUpResponse();
        response.setAbsentdays(signUpResponse.getAbsentdays());
        response.setPresentdays(signUpResponse.getPresentdays());
        response.setMonthdays(signUpResponse.getMonthdays());
        response.setWeekend(signUpResponse.getWeekend());
        response.setMonth_id(month_id);
        response.setUserId(userid);
        records.add(response);

        pcount.setText("" + Presentdays);
        acount.setText("" + absentdays);
        wcount.setText("" + weekenddays);
        drawpiechart();
        pa_layout.setVisibility(View.VISIBLE);
        if (PrefUtils.getKey(DashboardActivity.this, AppConstants.iSattandence).equalsIgnoreCase("false")) {
            storelocaldatabase();
        } else {
            databaseHelper = new DatabaseHelper(DashboardActivity.this);
            SignUpResponse response1 = new SignUpResponse();
            for (int i = 0; i < records.size(); i++) {
                boolean isHead = databaseHelper.hasObject1(records.get(i).getMonth_id());

                if (isHead == true) {
                    response1.setAbsentdays(records.get(i).getAbsentdays());
                    response1.setPresentdays(records.get(i).getPresentdays());
                    response1.setMonthdays(records.get(i).getMonthdays());
                    response1.setUserId(records.get(i).getUserId());
                    response1.setMonth_id(records.get(i).getMonth_id());
                    response1.setWeekend(records.get(i).getWeekend());
                    databaseHelper.UserAttandenceupdate(response1);
                } else {
                    storelocaldatabase();
                }
            }
        }
    }

    @Override
    public void onSignUpFailure(String msg) {
        // dialog.dismiss();
        progressbar1.setVisibility(View.GONE);
    }

    private void drawpiechart() {
        pieChartAttendance.setVisibility(View.VISIBLE);
        pieChartAttendance.setPressed(false);
        pieChartAttendance.setUsePercentValues(true);
        pieChartAttendance.setRotationEnabled(false);
        pieChartAttendance.setHoleRadius(0);
        // pieChartAttendance.getDescription().setText("  ");
        pieChartAttendance.animateXY(1000, 1000);
        pieChartAttendance.setDrawHoleEnabled(false);
        pieChartAttendance.invalidate();
        Legend pieLegend = pieChartAttendance.getLegend();
        pieLegend.setEnabled(false);
        ArrayList<Entry> arrayList = new ArrayList<Entry>();
        ArrayList<String> arrayListXAxis = new ArrayList<String>();
        ArrayList NoOfEmp = new ArrayList();
        //Percentage of Attendance
        float presentPercentage = (presentCount * 100) / totalAttendance;
        float absentPercentage = (absentCount * 100) / totalAttendance;
        float weekendPercentage = (weekendcount * 100) / totalAttendance;

        if (presentPercentage != 0.0) {
            NoOfEmp.add(new BarEntry(presentPercentage, 0));
        }

        NoOfEmp.add(new BarEntry(absentPercentage, 1));
        NoOfEmp.add(new BarEntry(weekendPercentage, 2));

        arrayListXAxis.add("");
        arrayListXAxis.add("");
        arrayListXAxis.add("");
        PieDataSet dataSet = new PieDataSet(NoOfEmp, "Number Of Employees");
        // PieDataSet dataSet = new PieDataSet(arrayList, "");
        dataSet.setSliceSpace(2);
        dataSet.setSelectionShift(2);

        //Color Array
        ArrayList<Integer> colors = new ArrayList<Integer>();
        if (presentPercentage != 0.0) {
            colors.add(Color.rgb(155, 192, 28));
            colors.add(Color.rgb(250, 103, 117));
            colors.add(Color.rgb(51, 204, 204));
        } else {
            colors.add(Color.rgb(250, 103, 117));
            colors.add(Color.rgb(51, 204, 204));
        }
        dataSet.setColors(colors);

        PieData piedata = new PieData(arrayListXAxis, dataSet);
        piedata.setValueFormatter(new PercentFormatter());
        piedata.setValueTextSize(11f);
        piedata.setValueTextColor(Color.WHITE);
        pieChartAttendance.setData(piedata);
    }

    public void storeuserlocaldatabase() {
        databaseHelper = new DatabaseHelper(DashboardActivity.this);
        EmployeData data = new EmployeData();
        try {
            for (int i = 0; i < employelist.size(); i++) {
                data.setUser_id(employelist.get(i).getUser_id());
                data.setUsername1(employelist.get(i).getUsername1());
                databaseHelper.USER(data);
            }
            PrefUtils.storeKey(getApplicationContext(), AppConstants.isuserentry, "true");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void storelocaldatabase() {
        databaseHelper = new DatabaseHelper(DashboardActivity.this);
        SignUpResponse response = new SignUpResponse();
        try {
            for (int i = 0; i < records.size(); i++) {
                response.setAbsentdays(records.get(i).getAbsentdays());
                response.setPresentdays(records.get(i).getPresentdays());
                response.setWeekend(records.get(i).getWeekend());
                response.setMonthdays(records.get(i).getMonthdays());
                response.setUserId(records.get(i).getUserId());
                response.setMonth_id(records.get(i).getMonth_id());
                response.setWeekend(records.get(i).getWeekend());
                databaseHelper.UserAttandence(response);
            }
            PrefUtils.storeKey(getApplicationContext(), AppConstants.iSattandence, "true");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void senddataTimeInfromSqlite() {
        databaseHelper = new DatabaseHelper(DashboardActivity.this);
        List<SignUpResponse> data = new ArrayList<>();
        data = databaseHelper.getAllAttandenceData();
        for (int i = 0; i < data.size(); i++) {
            String userid = data.get(i).getUserId();
            String monthid = data.get(i).getMonth_id();
            if (month_id.equals(monthid)) {
                absentCount = Float.parseFloat(data.get(i).getAbsentdays());
                // String a=data.get(i).getPresentdays();
                presentCount = Float.parseFloat(data.get(i).getPresentdays());
                totalAttendance = Float.parseFloat(data.get(i).getMonthdays());
                int Presentdays = Integer.parseInt(data.get(i).getPresentdays());
                int absentdays = Integer.parseInt(data.get(i).getAbsentdays());
                int wekendays = Integer.parseInt(data.get(i).getWeekend());
                weekendcount = Float.parseFloat(data.get(i).getWeekend());
                pcount.setText("" + Presentdays);
                acount.setText("" + absentdays);
                wcount.setText("" + wekendays);
                drawpiechart();
                pa_layout.setVisibility(View.VISIBLE);
                progressbar1.setVisibility(View.GONE);
                break;
            } else {
                pieChartAttendance.setVisibility(View.GONE);
                pa_layout.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void getuserdatafromSqlite() {
        databaseHelper = new DatabaseHelper(DashboardActivity.this);
        List<EmployeData> data = new ArrayList<>();
        data = databaseHelper.getAlUserData();
        employelist.addAll(data);
        if (employelist.size() > 0) {
            User_Adapter useradapter = new User_Adapter(getApplicationContext(), employelist);
            user_list.setAdapter(useradapter);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void getEmployelist() {
        if (NetworkUtil.isOnline(getApplicationContext())) {
            initGetEmployeeListAPIResources(PrefUtils.getKey(DashboardActivity.this, AppConstants.Clientid),PrefUtils.getKey(DashboardActivity.this, AppConstants.Api_Token));

        } else {

            Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    private void initGetEmployeeListAPIResources(String clientid,String apitoken) {
        GetAlEmployePresenterImpl getAlluserPresenter = new GetAlEmployePresenterImpl(DashboardActivity.this);
        getAlluserPresenter.callApi(AppConstants.User_List, clientid,apitoken);
    }

    @Override
    public void onGetEmployeListSuccess(EmployeResponse employeResponse) {
        try {
            if (employelist == null) {
                employelist = new ArrayList<>();
            }
            dialog.dismiss();
            int count = employelist.size();
            EmployeData userData = new EmployeData();
            userData.setUsername1("Select User");
            userData.setUser_id("0");
            employelist.add(userData);
            employelist.addAll(employeResponse.getEmployerData());
            User_Adapter useradapter = new User_Adapter(getApplicationContext(), employelist);
            user_list.setAdapter(useradapter);
            if (PrefUtils.getKey(DashboardActivity.this, AppConstants.isuserentry).equals("false")) {
                storeuserlocaldatabase();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetEmployeListListFailure(String msg) {
        dialog.dismiss();
    }
}
