package com.wfms.nectar.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.wfms.nectar.jsonModelResponses.Fuel.EmployeData;
import com.wfms.nectar.utils.AppConstants;
import com.wfms.nectar.utils.PrefUtils;
import com.wfms.nectar.wfms.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Nectar on 03-09-2018.
 */

public class Employe_Adapter extends RecyclerView.Adapter<Employe_Adapter.ViewHolder> {
    long elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds;
    Context con;
    ArrayList<EmployeData> employelist;
    EmployeData data;

    public Employe_Adapter(AppCompatActivity activity, ArrayList<EmployeData> employelist) {
        this.con = activity;
        this.employelist = employelist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employe_layout_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmployeData employeData = employelist.get(position);
        holder.username.setText(employeData.getUsername());


        if (employeData.getIn_date() != null) {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
            String inputDateStr = employeData.getIn_date();

            Date date1 = null;
            try {
                date1 = inputFormat.parse(inputDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String date = outputFormat.format(date1);

            holder.InDateTime.setVisibility(View.VISIBLE);

            String timearray[] = employeData.getIn_time().split(":");
            String time = timearray[0] + ":" + timearray[1];

            holder.InDateTime.setText(Html.fromHtml("<b>InDate :" + date + " || InTime :" + time + "</b>"));
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            String time1 = PrefUtils.getKey(con, AppConstants.ClientInTime);
            Log.d("hello", "hello");

            try {
                Date intime = formatter.parse(time);
                Date intime1 = formatter.parse(time1);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(intime1);
                calendar.add(Calendar.MINUTE, 30);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(intime1);
                calendar1.add(Calendar.HOUR, 1);

                String CDate = formatter.format(calendar.getTime());
                String LDate = formatter.format(calendar1.getTime());

                Date correcttime = formatter.parse(CDate);
                Date latetime = formatter.parse(LDate);

                if (intime.compareTo(correcttime) == 0) {
                    holder.icon.setBackgroundResource(R.drawable.icon_g);
                } else if (intime.before(correcttime)) {
                    holder.icon.setBackgroundResource(R.drawable.icon_g);
                } else if (intime.after(latetime)) {
                    holder.icon.setBackgroundResource(R.drawable.icon_r);
                } else if (intime.after(correcttime) && intime.before(latetime) || intime.compareTo(latetime) == 0) {
                    holder.icon.setBackgroundResource(R.drawable.icon_y);
                } else {
                    holder.icon.setBackgroundResource(R.drawable.icon_r);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {
            holder.InDateTime.setVisibility(View.GONE);
        }


        if (employeData.getOut_date() != null) {
            if (!employeData.getOut_date().equalsIgnoreCase("null")) {

                String in = employeData.getIn_date() + " " + employeData.getIn_time();
                String out = employeData.getOut_date() + " " + employeData.getOut_time();

                gettotaltime(in, out, holder.time_duration);
                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                String inputDateStr = employeData.getOut_date();

                Date date1 = null;
                try {
                    date1 = inputFormat.parse(inputDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String date = outputFormat.format(date1);

                holder.OutDateTime.setVisibility(View.VISIBLE);
                String timearray[] = employeData.getOut_time().split(":");
                String time = timearray[0] + ":" + timearray[1];
                holder.OutDateTime.setVisibility(View.VISIBLE);
                holder.OutDateTime.setText(Html.fromHtml("<b>OutDate :" + date + " || OutTime :" + time + "</b>"));

            } else {
                holder.OutDateTime.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {

        return employelist.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * ButterKnife Code
         **/

        @BindView(R.id.username)
        TextView username;
        @BindView(R.id.InDateTime)
        TextView InDateTime;
        @BindView(R.id.OutDateTime)
        TextView OutDateTime;
        @BindView(R.id.in_location)
        TextView in_location;
        @BindView(R.id.out_location)
        TextView out_location;
        @BindView(R.id.time_duration)
        TextView time_duration;
        @BindView(R.id.In_location_url)
        TextView In_location_url;
        @BindView(R.id.Out_location_url)
        TextView Out_location_url;
        @BindView(R.id.icon)
        ImageView icon;
        /**
         * ButterKnife Code
         **/

        Dialog dialogview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


    }

    public void gettotaltime(String in, String out, TextView time_duration) {

        String toyBornTime = "2014-06-18 12:56:50";
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        try {

            Date oldDate = dateFormat.parse(in);
            Date oldDate1 = dateFormat.parse(out);
            System.out.println(oldDate);

            Date currentDate = new Date();

            long diff = oldDate1.getTime() - oldDate.getTime();

            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            elapsedDays = days;
            elapsedHours = hours;
            elapsedMinutes = minutes;
            elapsedSeconds = seconds;

            if (days > 0) {
                elapsedDays = diff / days;
                diff = diff % days;
            }

            if (hours > 0) {
                elapsedHours = diff / hours;
                diff = diff % hours;
            }

            if (minutes > 0) {
                elapsedMinutes = diff / minutes;
                diff = diff % minutes;
            }

            if (seconds > 0) {
                elapsedSeconds = diff / seconds;
            }


            long different = oldDate1.getTime() - oldDate.getTime();

            System.out.println("startDate : " + oldDate1);
            System.out.println("endDate : " + oldDate);
            System.out.println("different : " + different);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            System.out.printf(
                    "%d days, %d hours, %d minutes, %d seconds%n",
                    elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
            if (oldDate.before(currentDate)) {

                Log.e("oldDate", "is previous date");
                Log.e("Difference: ", " seconds: " + seconds + " minutes: " + minutes
                        + " hours: " + hours + " days: " + days);

                System.out.printf(
                        "%d days, %d hours, %d minutes, %d seconds%n",
                        days, hours, minutes, seconds);
                if (elapsedDays > 0) {
                    time_duration.setText(elapsedDays + " d " + elapsedHours + " h :" + elapsedMinutes + " m");
                } else if (elapsedHours > 0) {
                    time_duration.setText(elapsedHours + " h :" + elapsedMinutes + " m");
                } else if (elapsedMinutes > 0) {
                    time_duration.setText(elapsedMinutes + " m");
                } else if (elapsedSeconds > 0) {
                    time_duration.setText(elapsedSeconds + " s");
                }
            }
        } catch (ParseException e) {

            e.printStackTrace();
        }
    }

}
