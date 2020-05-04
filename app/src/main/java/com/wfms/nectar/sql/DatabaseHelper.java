package com.wfms.nectar.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wfms.nectar.jsonModelResponses.Fuel.EmployeData;
import com.wfms.nectar.jsonModelResponses.signup.SignUpResponse;

import java.util.ArrayList;
import java.util.List;

import static com.wfms.nectar.utils.AppConstants.ABSENTDAYS;
import static com.wfms.nectar.utils.AppConstants.ATTANDENCE_TABLE;
import static com.wfms.nectar.utils.AppConstants.DATABASE_NAME;
import static com.wfms.nectar.utils.AppConstants.DATABASE_VERSION;
import static com.wfms.nectar.utils.AppConstants.EMPLOYE_ATTANDENCE_ID;
import static com.wfms.nectar.utils.AppConstants.EMPLOYE_INDATE;
import static com.wfms.nectar.utils.AppConstants.EMPLOYE_INLOCATION;
import static com.wfms.nectar.utils.AppConstants.EMPLOYE_INLOCTIONURL;
import static com.wfms.nectar.utils.AppConstants.EMPLOYE_INTIME;
import static com.wfms.nectar.utils.AppConstants.EMPLOYE_OUTDATE;
import static com.wfms.nectar.utils.AppConstants.EMPLOYE_OUTLOCATION;
import static com.wfms.nectar.utils.AppConstants.EMPLOYE_OUTLOCTIONURL;
import static com.wfms.nectar.utils.AppConstants.EMPLOYE_OUTTIME;
import static com.wfms.nectar.utils.AppConstants.EMPLOYE_TIMEDURATION;
import static com.wfms.nectar.utils.AppConstants.EMPLOYE_USERNAME;
import static com.wfms.nectar.utils.AppConstants.EMPLOYE_USETID;
import static com.wfms.nectar.utils.AppConstants.EmployeRecords_TABLE;
import static com.wfms.nectar.utils.AppConstants.Image;
import static com.wfms.nectar.utils.AppConstants.Latitude;
import static com.wfms.nectar.utils.AppConstants.Longitude;
import static com.wfms.nectar.utils.AppConstants.MONTH_ID;
import static com.wfms.nectar.utils.AppConstants.PRESENTDAYS;
import static com.wfms.nectar.utils.AppConstants.SaveImage_TABLE;
import static com.wfms.nectar.utils.AppConstants.TIMEIN_INMAC_ADDRESS;
import static com.wfms.nectar.utils.AppConstants.TIMEIN_IsLogin;
import static com.wfms.nectar.utils.AppConstants.TIMEIN_IsUpload;
import static com.wfms.nectar.utils.AppConstants.TIMEIN_LOCATION;
import static com.wfms.nectar.utils.AppConstants.TIMEIN_LOCATIONURL;
import static com.wfms.nectar.utils.AppConstants.TIMEIN_TABLE;
import static com.wfms.nectar.utils.AppConstants.TIMEIN_TIME;
import static com.wfms.nectar.utils.AppConstants.TIMEIN_USETID;
import static com.wfms.nectar.utils.AppConstants.TIMEOut_INMAC_ADDRESS;
import static com.wfms.nectar.utils.AppConstants.TIMEOut_LOCATION;
import static com.wfms.nectar.utils.AppConstants.TIMEOut_LOCATIONURL;
import static com.wfms.nectar.utils.AppConstants.TIMEOut_TABLE;
import static com.wfms.nectar.utils.AppConstants.TIMEOut_TIME;
import static com.wfms.nectar.utils.AppConstants.TIMEOut_USETID;
import static com.wfms.nectar.utils.AppConstants.TOTALDAYS;
import static com.wfms.nectar.utils.AppConstants.USERID;
import static com.wfms.nectar.utils.AppConstants.USERNAME;
import static com.wfms.nectar.utils.AppConstants.USER_ID;
import static com.wfms.nectar.utils.AppConstants.USER_TABLE;
import static com.wfms.nectar.utils.AppConstants.UserId;
import static com.wfms.nectar.utils.AppConstants.WEEKENDDAYS;


/**
 * Created by Nectar on 28-09-2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public String CREATE_TIMEIN_TABLE=  "CREATE TABLE " + TIMEIN_TABLE + " ( "
                                                        + TIMEIN_USETID + " INTEGER, "
                                                        + TIMEIN_LOCATION  + " TEXT, "
                                                        + TIMEIN_TIME + " DATETIME, "
                                                        + TIMEIN_LOCATIONURL + " TEXT, "
                                                        + TIMEIN_INMAC_ADDRESS + " TEXT, "
                                                        + TIMEIN_IsUpload + " TEXT, "
                                                        + TIMEIN_IsLogin + " TEXT"+ ")";


    public String CREATE_USER_RECORDS_TABLE="CREATE TABLE  " + EmployeRecords_TABLE + " ( "
                                                                + EMPLOYE_USETID + " INTEGER, "
                                                                + EMPLOYE_USERNAME  + " TEXT, "
                                                                + TIMEIN_TIME + " DATETIME, "
                                                                + EMPLOYE_ATTANDENCE_ID + " TEXT, "
                                                                + EMPLOYE_INDATE + " TEXT, "
                                                                + EMPLOYE_INTIME + " TEXT, "
                                                                + EMPLOYE_OUTDATE + " TEXT, "
                                                                + EMPLOYE_OUTTIME + " TEXT, "
                                                                + EMPLOYE_INLOCATION + " TEXT, "
                                                                + EMPLOYE_OUTLOCATION + " TEXT, "
                                                                + EMPLOYE_TIMEDURATION + " TEXT, "
                                                                + EMPLOYE_INLOCTIONURL + " TEXT, "
                                                                + EMPLOYE_OUTLOCTIONURL + " TEXT"+ ")";



    public String CREATE_Saveimage_TABLE=  "CREATE TABLE " + SaveImage_TABLE + " ( "
                                                        + UserId + " INTEGER, "
                                                        + Latitude  + " TEXT, "
                                                        + Longitude + " TEXT, "
                                                        + Image + " BLOB"+ ")";



    public String CREATE_TIMEOut_TABLE=  "CREATE TABLE " + TIMEOut_TABLE + " ( "
            + TIMEOut_USETID + " INTEGER, "
            + TIMEOut_LOCATION  + " TEXT, "
            + TIMEOut_TIME + " DATETIME, "
            + TIMEOut_LOCATIONURL + " TEXT, "
            + TIMEOut_INMAC_ADDRESS + " TEXT"+ ")";


    public String CREATE_Attandence_TABLE=  "CREATE TABLE " + ATTANDENCE_TABLE + " ( "
            + USER_ID + " INTEGER, "
            + PRESENTDAYS  + " TEXT, "
            + ABSENTDAYS + " TEXT, "
            + WEEKENDDAYS + " TEXT, "
            + TOTALDAYS + " TEXT, "
            + MONTH_ID + " INTEGER"+ ")";


    public String CREATE_User_TABLE=  "CREATE TABLE " + USER_TABLE + " ( "
            + USERID + " INTEGER, "
            + USERNAME + " TEXT"+ ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TIMEIN_TABLE);
        sqLiteDatabase.execSQL(CREATE_TIMEOut_TABLE);
        sqLiteDatabase.execSQL(CREATE_USER_RECORDS_TABLE);
        sqLiteDatabase.execSQL(CREATE_Saveimage_TABLE);
        sqLiteDatabase.execSQL(CREATE_Attandence_TABLE);
        sqLiteDatabase.execSQL(CREATE_User_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void TimeIn(EmployeData data)

    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TIMEIN_USETID,data.getUser_id());
        values.put(TIMEIN_LOCATION,data.getIn_location());
        values.put(TIMEIN_TIME,data.getIn_time());
        values.put(TIMEIN_LOCATIONURL,data.getIn_location_url());
        values.put(TIMEIN_INMAC_ADDRESS,data.getIn_mac_address());
        values.put(TIMEIN_IsUpload,data.getIsupload());
        values.put(TIMEIN_IsLogin,data.getIslogin());
        db.insert(TIMEIN_TABLE, null, values);
        db.close();
    }

    public void USER(EmployeData data)

    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USERID,data.getUser_id());
        values.put(USERNAME,data.getUsername1());
        db.insert(USER_TABLE, null, values);
        db.close();
    }
    public void UserRecords(EmployeData data)

    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EMPLOYE_USETID,data.getUser_id());
        values.put(EMPLOYE_USERNAME,data.getUsername());
        values.put(EMPLOYE_ATTANDENCE_ID,data.getAttendance_id());
        values.put(EMPLOYE_INDATE,data.getIn_date());
        values.put(EMPLOYE_INTIME,data.getIn_time());
        values.put(EMPLOYE_OUTDATE,data.getOut_date());
        values.put(EMPLOYE_OUTTIME,data.getOut_time());
        values.put(EMPLOYE_INLOCATION,data.getIn_location());
        values.put(EMPLOYE_OUTLOCATION,data.getOut_location());
        values.put(EMPLOYE_TIMEDURATION,data.getTime_duration());
        values.put(EMPLOYE_INLOCTIONURL,data.getIn_location_url());
        values.put(EMPLOYE_OUTLOCTIONURL,data.getOut_location_url());
        db.insert(EmployeRecords_TABLE, null, values);
        db.close();
    }
    public void UserRecordsupdate(EmployeData data)

    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EMPLOYE_USETID,data.getUser_id());
        values.put(EMPLOYE_USERNAME,data.getUsername());
        values.put(EMPLOYE_ATTANDENCE_ID,data.getAttendance_id());
        values.put(EMPLOYE_INDATE,data.getIn_date());
        values.put(EMPLOYE_INTIME,data.getIn_time());
        values.put(EMPLOYE_OUTDATE,data.getOut_date());
        values.put(EMPLOYE_OUTTIME,data.getOut_time());
        values.put(EMPLOYE_INLOCATION,data.getIn_location());
        values.put(EMPLOYE_OUTLOCATION,data.getOut_location());
        values.put(EMPLOYE_TIMEDURATION,data.getTime_duration());
        values.put(EMPLOYE_INLOCTIONURL,data.getIn_location_url());
        values.put(EMPLOYE_OUTLOCTIONURL,data.getOut_location_url());
      //  db.insert(EmployeRecords_TABLE, null, values);
        db.update(EmployeRecords_TABLE, values, EMPLOYE_OUTDATE + " = ?",
                new String[]{"null"});
        db.close();
    }

    public void UserupdateRecords(EmployeData data)

    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EMPLOYE_USETID,data.getUser_id());
        values.put(EMPLOYE_OUTDATE,data.getOut_date());
        values.put(EMPLOYE_OUTTIME,data.getOut_time());
        values.put(EMPLOYE_OUTLOCATION,data.getOut_location());
        values.put(EMPLOYE_OUTLOCTIONURL,data.getOut_location_url());
       /* db.update(EmployeRecords_TABLE, values, EMPLOYE_USETID + " = ?",
                new String[]{String.valueOf(data.getUser_id())});*/
        db.update(EmployeRecords_TABLE, values, EMPLOYE_OUTDATE + " = ?",
                new String[]{"null"});
        db.close();
    }

    public void UserAttandenceupdate(SignUpResponse data)
    {
        String sql = "UPDATE "+ATTANDENCE_TABLE +" SET " + PRESENTDAYS+ " = '"+data.getPresentdays()+"' WHERE "+MONTH_ID+ " = "+data.getMonth_id();
        Log.d("sqlupdate",sql);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRESENTDAYS,data.getPresentdays());
        values.put(ABSENTDAYS,data.getAbsentdays());
        values.put(MONTH_ID,data.getMonth_id());
        values.put(WEEKENDDAYS,data.getWeekend());
        values.put(USER_ID,data.getUserId());
        /*db.update(ATTANDENCE_TABLE, values, MONTH_ID + " = ?",
                new String[] {data.getMonth_id()});*/

        db.update(ATTANDENCE_TABLE, values, MONTH_ID + " = ? AND " + USER_ID + " = ?",
                new String[] {data.getMonth_id(),data.getUserId()});
        db.close();
    }

    public void UserAttandence(SignUpResponse data)

    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_ID,data.getUserId());
        values.put(PRESENTDAYS,data.getPresentdays());
        values.put(ABSENTDAYS,data.getAbsentdays());
        values.put(TOTALDAYS,data.getMonthdays());
        values.put(WEEKENDDAYS,data.getWeekend());
        values.put(MONTH_ID,data.getMonth_id());
        db.insert(ATTANDENCE_TABLE, null, values);
        db.close();
    }
    public void SaveImage(EmployeData data)

    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserId,data.getUser_id());
        values.put(Latitude,data.getLatitudee());
        values.put(Longitude,data.getLongtitude());
        values.put(Image,data.getImage());
        db.insert(SaveImage_TABLE, null, values);
        db.close();
    }

    public List<EmployeData> getAllUserRecordsData() {
        // array of columns to fetch
        String[] columns = {
                EMPLOYE_USETID,
                EMPLOYE_USERNAME,
                EMPLOYE_ATTANDENCE_ID,
                EMPLOYE_INDATE,
                EMPLOYE_INTIME,
                EMPLOYE_OUTDATE,
                EMPLOYE_OUTTIME,
                EMPLOYE_INLOCATION,
                EMPLOYE_OUTLOCATION,
                EMPLOYE_TIMEDURATION,
                EMPLOYE_INLOCTIONURL,
                EMPLOYE_OUTLOCTIONURL,

        };
        // sorting orders

        List<EmployeData> TimeInuserList = new ArrayList<EmployeData>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(EmployeRecords_TABLE, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                EMPLOYE_ATTANDENCE_ID+ " ASC"); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToLast()) {
            do {
                EmployeData data = new EmployeData();
                data.setUser_id(cursor.getString(cursor.getColumnIndex(EMPLOYE_USETID)));
                data.setUsername(cursor.getString(cursor.getColumnIndex(EMPLOYE_USERNAME)));
                data.setAttendance_id(cursor.getString(cursor.getColumnIndex(EMPLOYE_ATTANDENCE_ID)));
                data.setIn_date(cursor.getString(cursor.getColumnIndex(EMPLOYE_INDATE)));
                data.setIn_time(cursor.getString(cursor.getColumnIndex(EMPLOYE_INTIME)));
                data.setOut_date(cursor.getString(cursor.getColumnIndex(EMPLOYE_OUTDATE)));
                data.setOut_time(cursor.getString(cursor.getColumnIndex(EMPLOYE_OUTTIME)));
                data.setIn_location(cursor.getString(cursor.getColumnIndex(EMPLOYE_INLOCATION)));
                data.setOut_location(cursor.getString(cursor.getColumnIndex(EMPLOYE_OUTLOCATION)));
                data.setIn_location_url(cursor.getString(cursor.getColumnIndex(EMPLOYE_INLOCTIONURL)));
                data.setOut_location_url(cursor.getString(cursor.getColumnIndex(EMPLOYE_OUTLOCTIONURL)));
                data.setTime_duration(cursor.getString(cursor.getColumnIndex(EMPLOYE_TIMEDURATION)));
                TimeInuserList.add(data);
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        db.close();

        // return user list
        return TimeInuserList;
    }
    public List<EmployeData> getAllTimeinData() {
        // array of columns to fetch
        String[] columns = {
                TIMEIN_USETID,
                TIMEIN_LOCATION,
                TIMEIN_TIME,
                TIMEIN_LOCATIONURL,
                TIMEIN_INMAC_ADDRESS,
                TIMEIN_IsUpload,
                TIMEIN_IsLogin

        };
        // sorting orders

        List<EmployeData> TimeInuserList = new ArrayList<EmployeData>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TIMEIN_TABLE, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERTIMEIN_TABLEE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                EmployeData data = new EmployeData();
                data.setUser_id(cursor.getString(cursor.getColumnIndex(TIMEIN_USETID)));
                data.setIn_location(cursor.getString(cursor.getColumnIndex(TIMEIN_LOCATION)));
                data.setIn_time(cursor.getString(cursor.getColumnIndex(TIMEIN_TIME)));
                data.setIn_location_url(cursor.getString(cursor.getColumnIndex(TIMEIN_LOCATIONURL)));
                data.setIn_mac_address(cursor.getString(cursor.getColumnIndex(TIMEIN_INMAC_ADDRESS)));
                data.setIsupload(cursor.getString(cursor.getColumnIndex(TIMEIN_IsUpload)));
                data.setIslogin(cursor.getString(cursor.getColumnIndex(TIMEIN_IsLogin)));
                TimeInuserList.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return TimeInuserList;
    }

    public List<SignUpResponse> getAllAttandenceData() {
        // array of columns to fetch
        String[] columns = {
                USER_ID,
                ABSENTDAYS,
                PRESENTDAYS,
                TOTALDAYS,
                MONTH_ID,
                WEEKENDDAYS,
        };
        // sorting orders

        List<SignUpResponse> attandence_list = new ArrayList<SignUpResponse>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(ATTANDENCE_TABLE, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SignUpResponse data = new SignUpResponse();
                data.setUserId(cursor.getString(cursor.getColumnIndex(USER_ID)));
                data.setMonthdays(cursor.getString(cursor.getColumnIndex(TOTALDAYS)));
                String b1=cursor.getString(cursor.getColumnIndex(ABSENTDAYS));
                String b=cursor.getString(cursor.getColumnIndex(PRESENTDAYS));
                data.setPresentdays(cursor.getString(cursor.getColumnIndex(PRESENTDAYS)));
                data.setAbsentdays(cursor.getString(cursor.getColumnIndex(ABSENTDAYS)));
                data.setMonth_id(cursor.getString(cursor.getColumnIndex(MONTH_ID)));
                data.setWeekend(cursor.getString(cursor.getColumnIndex(WEEKENDDAYS)));
                attandence_list.add(data);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return attandence_list;
    }


    public List<EmployeData> getAlUserData() {
        // array of columns to fetch
        String[] columns = {
                USERID,
                USERNAME,
        };
        // sorting orders

        List<EmployeData> user_list = new ArrayList<EmployeData>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(USER_TABLE, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                EmployeData data = new EmployeData();
                data.setUser_id(cursor.getString(cursor.getColumnIndex(USERID)));
                data.setUsername1(cursor.getString(cursor.getColumnIndex(USERNAME)));
                user_list.add(data);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return user_list;
    }
    public List<EmployeData> getAllTimeoutData() {
        // array of columns to fetch
        String[] columns = {
                TIMEOut_USETID,
                TIMEOut_LOCATION,
                TIMEOut_TIME,
                TIMEOut_LOCATIONURL,
                TIMEOut_INMAC_ADDRESS
        };
        // sorting orders

        List<EmployeData> TimeInuserList = new ArrayList<EmployeData>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TIMEOut_TABLE, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                EmployeData data = new EmployeData();
                data.setUser_id(cursor.getString(cursor.getColumnIndex(TIMEIN_USETID)));
                data.setIn_location(cursor.getString(cursor.getColumnIndex(TIMEIN_LOCATION)));
                data.setIn_time(cursor.getString(cursor.getColumnIndex(TIMEIN_TIME)));
                data.setIn_location_url(cursor.getString(cursor.getColumnIndex(TIMEIN_LOCATIONURL)));
                data.setIn_mac_address(cursor.getString(cursor.getColumnIndex(TIMEIN_INMAC_ADDRESS)));


                TimeInuserList.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return TimeInuserList;
    }

    public void deletetimetable(){
        SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM "+ TIMEIN_TABLE); //delete all rows in a table
            db.close();

    }
    public void deleteimagetable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ SaveImage_TABLE); //delete all rows in a table
        db.close();

    }
    public void deleterecordtable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ EmployeRecords_TABLE); //delete all rows in a table
      //  db.execSQL("DROP TABLE IF EXISTS '" + EmployeRecords_TABLE + "'");
        db.close();
    }


    public List<EmployeData> getAllsaveImageData() {
        // array of columns to fetch
        String[] columns = {
                UserId,
                Latitude,
                Longitude,
                Image,

        };
        // sorting orders

        List<EmployeData> saveimage = new ArrayList<EmployeData>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(SaveImage_TABLE, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                EmployeData data = new EmployeData();
                data.setUser_id(cursor.getString(cursor.getColumnIndex(UserId)));
                data.setLatitudee(cursor.getString(cursor.getColumnIndex(Latitude)));
                data.setLongtitude(cursor.getString(cursor.getColumnIndex(Longitude)));
                data.setImage(cursor.getString(cursor.getColumnIndex(Image)));
                saveimage.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return saveimage;
    }
    public boolean hasObject(String id) {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor=db.rawQuery("SELECT * FROM EmployeRecords_TABLE WHERE attandenece_id='"+id+"'", null);
        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;

            //region if you had multiple records to check for, use this region.

            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }
            //here, count is records found
            Log.d("tag", String.format("%d records found", count));

            //endregion

        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }

    public boolean hasObject1(String id) {
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor=db.rawQuery("SELECT * FROM ATTANDENCE_TABLE WHERE MONTH_ID='"+id+"'", null);
        boolean hasObject = false;
        if(cursor.moveToFirst()){
            hasObject = true;

            //region if you had multiple records to check for, use this region.

            int count = 0;
            while(cursor.moveToNext()){
                count++;
            }
            //here, count is records found
            Log.d("tag", String.format("%d records found", count));

            //endregion

        }

        cursor.close();          // Dont forget to close your cursor
        db.close();              //AND your Database!
        return hasObject;
    }
}
