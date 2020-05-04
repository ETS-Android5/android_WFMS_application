package com.wfms.nectar.utils;

/**
 * Created by Abhishek on 4/8/2018.
 */

public class AppConstants {

public static final String TAG = AppConstants.class.getSimpleName();

    //**********************************************************************************************//*
    //*----------------------------Local Database Constant Variables------------------------------------------*//*
    //***************************************Strat***********************************************//*
    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "WFMS1.db";

    public static final String  TIMEIN_TABLE = "TIMEIN_TABLE";
    public static final String  TIMEIN_USETID = "user_id";
    public static final String  TIMEIN_LOCATION = "in_location";
    public static final String  TIMEIN_TIME = "in_time";
    public static final String  TIMEIN_LOCATIONURL = "in_location_url";
    public static final String  TIMEIN_INMAC_ADDRESS = "in_mac_address";
    public static final String  TIMEIN_IsUpload = "IsUpload";
    public static final String  TIMEIN_IsLogin = "IsLogin";
    public static final String Isnotification = "Isnotification";
    public static final String Isgeofencenotification = "Isgeofencenotification";

    public static final String  TIMEOut_TABLE = "TIMEOut_TABLE";
    public static final String  TIMEOut_USETID = "user_id";
    public static final String  TIMEOut_LOCATION = "out_location";
    public static final String  TIMEOut_TIME = "out_time";
    public static final String  TIMEOut_LOCATIONURL = "out_location_url";
    public static final String  TIMEOut_INMAC_ADDRESS = "out_mac_address";
    public static final String Clientid = "Clientid";
    public static final String ClientName = "ClientName";
    public static final String ClientInTime = "ClientInTime";

    public static final String  ATTANDENCE_TABLE = "ATTANDENCE_TABLE";
    public static final String  USER_ID = "USER_ID";
    public static final String  PRESENTDAYS = "PRESENTDAYS";
    public static final String  ABSENTDAYS = "ABSENTDAYS";
    public static final String  WEEKENDDAYS = "WEEKENDDAYS";
    public static final String  TOTALDAYS = "TOTALDAYS";
    public static final String  MONTH_ID = "MONTH_ID";

    public static final String  EmployeRecords_TABLE = "EmployeRecords_TABLE";
    public static final String  EMPLOYE_USETID = "userid";
    public static final String  EMPLOYE_USERNAME = "username";
    public static final String  EMPLOYE_ATTANDENCE_ID = "attandenece_id";
    public static final String  EMPLOYE_INDATE = "in_date";
    public static final String  EMPLOYE_INTIME = "intime";
    public static String  EMPLOYE_OUTDATE = "out_date";
    public static final String  EMPLOYE_OUTTIME = "out_time";
    public static final String  EMPLOYE_INLOCATION = "in_location";
    public static final String  EMPLOYE_OUTLOCATION = "out_location";
    public static final String  EMPLOYE_TIMEDURATION = "time_duration";
    public static final String  EMPLOYE_INLOCTIONURL = "in_location_url";
    public static final String  EMPLOYE_OUTLOCTIONURL = "out_location_url";

    public static final String  SaveImage_TABLE = "SaveImage_TABLE";
    public static final String  UserId = "userid";
    public static final String  Latitude = "Latitude";
    public static final String  Longitude = "Longitude";
    public static final String  Image = "image";

    public static final String  USER_TABLE = "USER_TABLE";
    public static final String  USERID = "USERID";
    public static final String  USERNAME = "USERNAME";


    public static final String COLUMN_TRANS_ID = "transporter_id";
    public static final String COLUMN_ID_TRANSPORTER = "idTrans";

    public static final String UserID = "userid";
    public static final String LogoutUserID = "LogoutUserID";
    public static final String Ischeck = "false";
    public static final String OldUserID = "olduserid";
    public static final String Username = "username";
    public static final String Api_Token = "Api_Token";
    public static final String Password = "password";
    public static final String deviceID = "deviceID";
    public static final String Name = "name";
    public static final String Iservice = "false";
    public static final String entrytime = "entrytime";
    public static final String Attandenceid = "Attandenceid";

    //***************************************End***********************************************//
   public static final String TEST_URL = "http://wfms.timesheet.nectarinfotel.com/";

   // public static final String TEST_URL = "http://192.168.168.73/TFS/wfmsuat_timesheet/public/";
  // public static final String TEST_URL = "http://wfmsuat.timesheet.nectarinfotel.com/";

    public static final String updated = "updated";
    public static final String LOGIN = "login";

    public static final String CreateGeofence = "CreateGeofence";
    public static final String GeofenceNotification = "GeofenceNotification";
    public static final String SingleGeofenceNotification = "SingleGeofenceNotification";
    public static final String EditGeofence = "EditGeofence";
    public static final String RemoveGeofence = "RemoveGeofence";
    public static final String AssignGeofence = "AssignGeofence";
    public static final String DAssignGeofence = "DAssignGeofence";
    public static final String Available_Leaves = "Available_Leaves";
    public static final String Available_Leaves1 = "Available_Leaves1";
    public static final String Applyleaves = "Applyleaves";
    public static final String Applyleaves1 = "Applyleaves1";
    public static final String Attandence = "Attandence";
    public static final String GeofenceHistory = "GeofenceHistory";
    public static final String NotificationEnable = "NotificationEnable";
    public static final String GeofenceNotificationEnable = "GeofenceNotificationEnable";
    public static final String Geofenceenable = "Geofenceenable";
    public static final String Client = "Client";
    public static final String TimeIN = "TimeIN";
    public static final String TimeOut = "TimeOut";
    public static final String Employee_List = "Employee_List";
    public static final String Geofence_List = "Geofence_List";
    public static final String Employe_Geofence_List = "Employe_Geofence_List";
    public static final String Pending_List = "Pending_List";
    public static final String Pending_List1 = "Pending_List1";
    public static final String Approved_List = "Approved_List";
    public static final String Approved_List1 = "Approved_List1";
    public static final String Cancel_List = "Cancel_List";
    public static final String Cancel_List1 = "Cancel_List1";
    public static final String Rejected_List = "Rejected_List";
    public static final String Rejected_List1 = "Rejected_List1";
    public static final String User_List = "User_List";
    public static final String Filter_Employee_List = "Filter_Employee_List";

    public static final String IsLogin = "IsLogin";

    public static final String Isupdated = "false";
    public static final String iSattandence = "false";
    public static final String isuserentry = "false";
    public static final String TokenID = "tokenid";
    public static final String Notification_status = "Notification_status";
    public static final String Reset_Pin = "Reset_Pin";
    public static final String Notification = "notification";
    public static final String Logout = "Logout";
    public static final String Leave_Cancel = "Leave_Cancel";
    public static final String Leave_Cancel1 = "Leave_Cancel1";
    public static final String AdminNotification = "adminnotification";
    public static final String BASE_URL = TEST_URL;

    /*---------- Request Code ---------------------------------------*/
    public static final int REQUEST_OPEN_DOCUMENTS_FOLDER = 1100;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1200;

    public static final String ANDROID_DOCUMENTS_ACTIVITY = "com.android.documentsui.DocumentsActivity";
    public static final String APPLICATION_DIR_NAME = "Nectar";
    public static final String IMAGES_DIR = "/Images/";

}