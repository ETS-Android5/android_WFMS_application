
package com.wfms.nectar.jsonModelResponses.Fuel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeData implements Parcelable
{
    private String islogin;
    private String isupload;
    private String password;
    private String latitudee;
    private String longtitude;
    private String Image;
    private String assigngeofence;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getIslogin() {
        return islogin;
    }

    public void setIslogin(String islogin) {
        this.islogin = islogin;
    }

    public String getIsupload() {
        return isupload;
    }

    public void setIsupload(String isupload) {
        this.isupload = isupload;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLatitudee() {
        return latitudee;
    }

    public void setLatitudee(String latitude) {
        this.latitudee = latitudee;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getGeofencename() {
        return geofencename;
    }

    public void setGeofencename(String geofencename) {
        this.geofencename = geofencename;
    }

    public String getAssigngeofence() {
        return assigngeofence;
    }

    public void setAssigngeofence(String assigngeofence) {
        this.assigngeofence = assigngeofence;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("geofence_id")
    @Expose
    private String geofence_id;
    @SerializedName("username")
    @Expose

    private String username1;
    @SerializedName("attendance_id")
    @Expose
    private String attendance_id;
    @SerializedName("name")
    @Expose
    private String username;
    @SerializedName("in_date")
    @Expose
    private String in_date;
    @SerializedName("in_time")
    @Expose
    private String in_time;
    @SerializedName("out_date")
    @Expose
    private String out_date;
    @SerializedName("out_time")
    @Expose
    private String out_time;
    @SerializedName("isactive")
    @Expose
    private String isactive;
    @SerializedName("isgeonotification")
    @Expose
    private String isgeonotification;
    @SerializedName("user_name")
    @Expose
    private String user_name;
    public String getIn_location_url() {
        return in_location_url;
    }

    public void setIn_location_url(String in_location_url) {
        this.in_location_url = in_location_url;
    }

    public String getGeofence_id() {
        return geofence_id;
    }

    public void setGeofence_id(String geofence_id) {
        this.geofence_id = geofence_id;
    }

    public String getOut_location_url() {
        return out_location_url;
    }

    public void setOut_location_url(String out_location_url) {
        this.out_location_url = out_location_url;
    }
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("dest_address")
    @Expose
    private String end_geofence_address;
    @SerializedName("dest_latitude")
    @Expose
    private String end_latitude;
    @SerializedName("dest_longitude")
    @Expose
    private String end_longitude;
    @SerializedName("geofencename")
    @Expose
    private String geofencename;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("longitude")
    @Expose
    private String geofence_longitude;
    @SerializedName("latitude")
    @Expose
    private String geofence_latitude;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("radius")
    @Expose
    private String radius;

    @SerializedName("in_location")
    @Expose
    private String in_location;
    @SerializedName("in_location_url")
    @Expose
    private String in_location_url;
    @SerializedName("out_location_url")
    @Expose
    private String out_location_url;

    public String getIn_mac_address() {
        return in_mac_address;
    }

    public void setIn_mac_address(String in_mac_address) {
        this.in_mac_address = in_mac_address;
    }

    public String getOut_mac_address() {
        return out_mac_address;
    }

    public void setOut_mac_address(String out_mac_address) {
        this.out_mac_address = out_mac_address;
    }

    public String getIsgeonotification() {
        return isgeonotification;
    }

    public void setIsgeonotification(String isgeonotification) {
        this.isgeonotification = isgeonotification;
    }

    @SerializedName("in_mac_address")

    @Expose
    private String in_mac_address;
    @SerializedName("out_mac_address")
    @Expose
    private String out_mac_address;
    public String getTime_duration() {
        return time_duration;
    }

    public void setTime_duration(String time_duration) {
        this.time_duration = time_duration;
    }

    @SerializedName("out_location")
    @Expose

    private String out_location;
    @SerializedName("time_duration")
    @Expose
    private String time_duration;
    private String company;

    public String getIn_location() {
        return in_location;
    }

    public void setIn_location(String in_location) {
        this.in_location = in_location;
    }

    public String getOut_location() {
        return out_location;
    }

    public void setOut_location(String out_location) {
        this.out_location = out_location;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public EmployeData() {

    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getVehicle_type_image() {
        return vehicle_type_image;
    }

    public void setVehicle_type_image(String vehicle_type_image) {
        this.vehicle_type_image = vehicle_type_image;
    }

    @SerializedName("vehicle_type_image")
    @Expose
    private String vehicle_type_image;

    public final static Creator<EmployeData> CREATOR = new Creator<EmployeData>() {


        @SuppressWarnings({
            "unchecked"
        })
        public com.wfms.nectar.jsonModelResponses.Fuel.EmployeData createFromParcel(Parcel in) {
            return new com.wfms.nectar.jsonModelResponses.Fuel.EmployeData(in);
        }

        public com.wfms.nectar.jsonModelResponses.Fuel.EmployeData[] newArray(int size) {
            return (new com.wfms.nectar.jsonModelResponses.Fuel.EmployeData[size]);
        }

    }
    ;

    public String getEnd_latitude() {
        return end_latitude;
    }

    public void setEnd_latitude(String end_latitude) {
        this.end_latitude = end_latitude;
    }

    public String getEnd_longitude() {
        return end_longitude;
    }

    public void setEnd_longitude(String end_longitude) {
        this.end_longitude = end_longitude;
    }

    public String getEnd_geofence_address() {
        return end_geofence_address;
    }

    public void setEnd_geofence_address(String end_geofence_address) {
        this.end_geofence_address = end_geofence_address;
    }

    public EmployeData(Parcel in) {
        this.user_id = ((String) in.readValue((String.class.getClassLoader())));
        this.attendance_id = ((String) in.readValue((String.class.getClassLoader())));
        this.username = ((String) in.readValue((String.class.getClassLoader())));
        this.in_date = ((String) in.readValue((String.class.getClassLoader())));
        this.in_time = ((String) in.readValue((String.class.getClassLoader())));
        this.out_date = ((String) in.readValue((String.class.getClassLoader())));
        this.in_location = ((String) in.readValue((String.class.getClassLoader())));
        this.out_location = ((String) in.readValue((String.class.getClassLoader())));
        this.out_time = ((String) in.readValue((String.class.getClassLoader())));
        this.time_duration = ((String) in.readValue((String.class.getClassLoader())));
        this.in_location_url = ((String) in.readValue((String.class.getClassLoader())));
        this.out_location_url = ((String) in.readValue((String.class.getClassLoader())));
        this.in_mac_address = ((String) in.readValue((String.class.getClassLoader())));
        this.out_mac_address = ((String) in.readValue((String.class.getClassLoader())));
        this.username1 = ((String) in.readValue((String.class.getClassLoader())));
        this.geofencename = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.address = ((String) in.readValue((String.class.getClassLoader())));
        this.geofence_latitude = ((String) in.readValue((String.class.getClassLoader())));
        this.geofence_longitude = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.radius = ((String) in.readValue((String.class.getClassLoader())));
        this.isactive = ((String) in.readValue((String.class.getClassLoader())));
        this.end_latitude = ((String) in.readValue((String.class.getClassLoader())));
        this.end_longitude = ((String) in.readValue((String.class.getClassLoader())));
        this.end_geofence_address = ((String) in.readValue((String.class.getClassLoader())));
        this.isgeonotification = ((String) in.readValue((String.class.getClassLoader())));
        this.geofence_id = ((String) in.readValue((String.class.getClassLoader())));
        this.geofence_id = ((String) in.readValue((String.class.getClassLoader())));
    }

    public String getUsername1() {
        return username1;
    }

    public void setUsername1(String username1) {
        this.username1 = username1;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAttendance_id() {
        return attendance_id;
    }

    public void setAttendance_id(String attendance_id) {
        this.attendance_id = attendance_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIn_date() {
        return in_date;
    }

    public void setIn_date(String in_date) {
        this.in_date = in_date;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getOut_date() {
        return out_date;
    }

    public void setOut_date(String out_date) {
        this.out_date = out_date;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public String getGeofence_longitude() {
        return geofence_longitude;
    }

    public void setGeofence_longitude(String geofence_longitude) {
        this.geofence_longitude = geofence_longitude;
    }

   public String getGeofence_latitude() {
        return geofence_latitude;
    }

    public void setGeofence_latitude(String geofence_latitude) {
        this.geofence_latitude = geofence_latitude;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(user_id);
        dest.writeValue(username);
        dest.writeValue(in_date);
        dest.writeValue(in_time);
        dest.writeValue(out_date);
        dest.writeValue(out_time);
        dest.writeValue(in_location);
        dest.writeValue(out_location);
        dest.writeValue(time_duration);
        dest.writeValue(in_location_url);
        dest.writeValue(out_location_url);
        dest.writeValue(in_mac_address);
        dest.writeValue(out_mac_address);
        dest.writeValue(username1);
        dest.writeValue(geofencename);
        dest.writeValue(id);
        dest.writeValue(geofence_latitude);
        dest.writeValue(geofence_longitude);
        dest.writeValue(radius);
        dest.writeValue(username1);
        dest.writeValue(geofencename);
        dest.writeValue(isactive);
        dest.writeValue(end_latitude);
        dest.writeValue(end_longitude);
        dest.writeValue(end_geofence_address);
        dest.writeValue(isgeonotification);
        dest.writeValue(geofence_id);
    }

    public int describeContents() {
        return  0;
    }

}
