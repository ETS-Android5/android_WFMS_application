
package com.wfms.nectar.jsonModelResponses.signup;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpResponse implements Parcelable
{
    @SerializedName("client_id")
    @Expose
    private String client_id;
    @SerializedName("api_token")
    @Expose
    private String api_token;
    @SerializedName("weekend")
    @Expose
    private String weekend;
    private String month_id;
    @SerializedName("presentdays")
    @Expose
    private String presentdays;
    @SerializedName("absentdays")
    @Expose
    private String absentdays;
    @SerializedName("monthdays")
    @Expose
    private String monthdays;
    @SerializedName("client_intime")
    @Expose
    private String client_intime;
    @SerializedName("auth")
    @Expose
    private String auth;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String msg;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("GSTIN")
    @Expose
    private String gSTIN;
    @SerializedName("emailid")
    @Expose
    private String emailid;
    @SerializedName("contactno")
    @Expose
    private String contactno;
    @SerializedName("user_type")
    @Expose
    private String usertype;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("OTP")
    @Expose
    private String otp;
    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("attendance_status")
    @Expose
    private String attendance_status;
    @SerializedName("leave")
    @Expose
    private String leave;
    @SerializedName("rejectedleave")
    @Expose
    private String rejectedleave;
    @SerializedName("approvedleave")
    @Expose
    private String approvedleave;
    @SerializedName("pendingleave")
    @Expose
    private String pendingleave;
    @SerializedName("canceledleaves")
    @Expose
    private String canceledleaves;
    @SerializedName("availableleave")
    @Expose
    private String availableleave;
    @SerializedName("last_insert_id")
    @Expose
    private String last_insert_id;
    @SerializedName("entry")
    @Expose
    private String entry;
    public final static Creator<SignUpResponse> CREATOR = new Creator<SignUpResponse>() {

        @SuppressWarnings({
                "unchecked"
        })
        public SignUpResponse createFromParcel(Parcel in) {
            return new SignUpResponse(in);
        }

        public SignUpResponse[] newArray(int size) {
            return (new SignUpResponse[size]);
        }

    }
            ;

    protected SignUpResponse(Parcel in) {
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.msg = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.username = ((String) in.readValue((String.class.getClassLoader())));
        this.gSTIN = ((String) in.readValue((String.class.getClassLoader())));
        this.emailid = ((String) in.readValue((String.class.getClassLoader())));
        this.contactno = ((String) in.readValue((String.class.getClassLoader())));
        this.password = ((String) in.readValue((String.class.getClassLoader())));
        this.address = ((String) in.readValue((String.class.getClassLoader())));
        this.state = ((String) in.readValue((String.class.getClassLoader())));
        this.pincode = ((String) in.readValue((String.class.getClassLoader())));
        this.usertype = ((String) in.readValue((String.class.getClassLoader())));
        this.otp = ((String) in.readValue((String.class.getClassLoader())));
        this.auth = ((String) in.readValue((String.class.getClassLoader())));
        this.flag = ((String) in.readValue((String.class.getClassLoader())));
        this.attendance_status = ((String) in.readValue((String.class.getClassLoader())));
        this.client_id = ((String) in.readValue((String.class.getClassLoader())));
        this.client_intime = ((String) in.readValue((String.class.getClassLoader())));
        this.presentdays = ((String) in.readValue((String.class.getClassLoader())));
        this.monthdays = ((String) in.readValue((String.class.getClassLoader())));
        this.absentdays = ((String) in.readValue((String.class.getClassLoader())));
        this.weekend = ((String) in.readValue((String.class.getClassLoader())));
        this.leave = ((String) in.readValue((String.class.getClassLoader())));
        this.approvedleave = ((String) in.readValue((String.class.getClassLoader())));
        this.pendingleave = ((String) in.readValue((String.class.getClassLoader())));
        this.rejectedleave = ((String) in.readValue((String.class.getClassLoader())));
        this.canceledleaves = ((String) in.readValue((String.class.getClassLoader())));
        this.availableleave = ((String) in.readValue((String.class.getClassLoader())));
        this.last_insert_id = ((String) in.readValue((String.class.getClassLoader())));
        this.entry = ((String) in.readValue((String.class.getClassLoader())));
        this.api_token = ((String) in.readValue((String.class.getClassLoader())));
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getWeekend() {
        return weekend;
    }

    public void setWeekend(String weekend) {
        this.weekend = weekend;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public SignUpResponse() {
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public String getLast_insert_id() {
        return last_insert_id;
    }

    public void setLast_insert_id(String last_insert_id) {
        this.last_insert_id = last_insert_id;
    }

    public String getMonth_id() {
        return month_id;
    }

    public void setMonth_id(String month_id) {
        this.month_id = month_id;
    }

    public String getAbsentdays() {
        return absentdays;
    }

    public void setAbsentdays(String absentdays) {
        this.absentdays = absentdays;
    }

    public String getPresentdays() {
        return presentdays;
    }

    public void setPresentdays(String presentdays) {
        this.presentdays = presentdays;
    }

    public String getMonthdays() {
        return monthdays;
    }

    public void setMonthdays(String monthdays) {
        this.monthdays = monthdays;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_intime() {
        return client_intime;
    }

    public void setClient_intime(String client_intime) {
        this.client_intime = client_intime;
    }

    public String getStatus() {
        return status;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUserId() {
        return userId;
    }

    public String getLeave() {
        return leave;
    }

    public void setLeave(String leave) {
        this.leave = leave;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGSTIN() {
        return gSTIN;
    }

    public void setGSTIN(String gSTIN) {
        this.gSTIN = gSTIN;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getAttendance_status() {
        return attendance_status;
    }

    public String getRejectedleave() {
        return rejectedleave;
    }

    public void setRejectedleave(String rejectedleave) {
        this.rejectedleave = rejectedleave;
    }

    public String getApprovedleave() {
        return approvedleave;
    }

    public void setApprovedleave(String approvedleave) {
        this.approvedleave = approvedleave;
    }

    public String getPendingleave() {
        return pendingleave;
    }

    public void setPendingleave(String pendingleave) {
        this.pendingleave = pendingleave;
    }

    public String getCanceledleaves() {
        return canceledleaves;
    }

    public void setCanceledleaves(String canceledleaves) {
        this.canceledleaves = canceledleaves;
    }

    public String getAvailableleave() {
        return availableleave;
    }

    public void setAvailableleave(String availableleave) {
        this.availableleave = availableleave;
    }

    public void setAttendance_status(String attendance_status) {
        this.attendance_status = attendance_status;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(msg);
        dest.writeValue(userId);
        dest.writeValue(name);
        dest.writeValue(username);
        dest.writeValue(gSTIN);
        dest.writeValue(emailid);
        dest.writeValue(contactno);
        dest.writeValue(password);
        dest.writeValue(address);
        dest.writeValue(state);
        dest.writeValue(pincode);
        dest.writeValue(usertype);
        dest.writeValue(auth);
        dest.writeValue(flag);
        dest.writeValue(attendance_status);
        dest.writeValue(client_id);
        dest.writeValue(client_intime);
        dest.writeValue(presentdays);
        dest.writeValue(monthdays);
        dest.writeValue(absentdays);
        dest.writeValue(leave);
        dest.writeValue(pendingleave);
        dest.writeValue(approvedleave);
        dest.writeValue(rejectedleave);
        dest.writeValue(canceledleaves);
        dest.writeValue(availableleave);
        dest.writeValue(last_insert_id);
        dest.writeValue(entry);
        dest.writeValue(api_token);
    }

    public int describeContents() {
        return  0;
    }

}
