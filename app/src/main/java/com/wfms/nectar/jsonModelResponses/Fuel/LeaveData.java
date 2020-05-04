
package com.wfms.nectar.jsonModelResponses.Fuel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveData implements Parcelable
{



    @SerializedName("leavetype_name")
    @Expose
    private String leavetype_name;

    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("from_date")
    @Expose
    private String from_date;
    @SerializedName("to_date")
    @Expose
    private String to_date;
    @SerializedName("leavestatus")
    @Expose
    private String leavestatus;
    @SerializedName("appliedleavescount")
    @Expose
    private String appliedleavescount;
    @SerializedName("modifieddate")
    @Expose
    private String modifieddate;
    @SerializedName("leaveid")
    @Expose
    private String leaveid;
    @SerializedName("geofencename")
    @Expose
    private String geofencename;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;


    public final static Creator<LeaveData> CREATOR = new Creator<LeaveData>() {


        @SuppressWarnings({
            "unchecked"
        })
        public LeaveData createFromParcel(Parcel in) {
            return new LeaveData(in);
        }

        public LeaveData[] newArray(int size) {
            return (new LeaveData[size]);
        }

    }
    ;

    public LeaveData(Parcel in) {
        this.leavetype_name = ((String) in.readValue((String.class.getClassLoader())));
        this.reason = ((String) in.readValue((String.class.getClassLoader())));
        this.from_date = ((String) in.readValue((String.class.getClassLoader())));
        this.to_date = ((String) in.readValue((String.class.getClassLoader())));
        this.leavestatus = ((String) in.readValue((String.class.getClassLoader())));
        this.appliedleavescount = ((String) in.readValue((String.class.getClassLoader())));
        this.modifieddate = ((String) in.readValue((String.class.getClassLoader())));
        this.leaveid = ((String) in.readValue((String.class.getClassLoader())));
        this.geofencename = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.address = ((String) in.readValue((String.class.getClassLoader())));

    }




    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(leavetype_name);
        dest.writeValue(reason);
        dest.writeValue(from_date);
        dest.writeValue(to_date);
        dest.writeValue(leavestatus);
        dest.writeValue(appliedleavescount);
        dest.writeValue(modifieddate);
        dest.writeValue(leaveid);
        dest.writeValue(geofencename);
        dest.writeValue(name);
        dest.writeValue(type);
        dest.writeValue(address);
    }

    public String getLeaveid() {
        return leaveid;
    }

    public void setLeaveid(String leaveid) {
        this.leaveid = leaveid;
    }

    public String getLeavetype_name() {
        return leavetype_name;
    }

    public void setLeavetype_name(String leavetype_name) {
        this.leavetype_name = leavetype_name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getLeavestatus() {
        return leavestatus;
    }

    public void setLeavestatus(String leavestatus) {
        this.leavestatus = leavestatus;
    }

    public String getAppliedleavescount() {
        return appliedleavescount;
    }

    public void setAppliedleavescount(String appliedleavescount) {
        this.appliedleavescount = appliedleavescount;
    }

    public String getGeofencename() {
        return geofencename;
    }

    public void setGeofencename(String geofencename) {
        this.geofencename = geofencename;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModifieddate() {
        return modifieddate;
    }

    public void setModifieddate(String modifieddate) {
        this.modifieddate = modifieddate;
    }

    public static Creator<LeaveData> getCREATOR() {
        return CREATOR;
    }

    public int describeContents() {
        return  0;
    }

}
