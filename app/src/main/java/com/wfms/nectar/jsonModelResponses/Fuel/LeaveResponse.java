
package com.wfms.nectar.jsonModelResponses.Fuel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LeaveResponse implements Parcelable
{
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("leave")
    @Expose
    private List<LeaveData> leaverData = null;
    @SerializedName("data")
    @Expose
    private List<LeaveData> geodataData = null;
    public final static Creator<LeaveResponse> CREATOR = new Creator<LeaveResponse>() {
        @SuppressWarnings({
                "unchecked"
        })
        public LeaveResponse createFromParcel(Parcel in) {
            return new LeaveResponse(in);
        }

        public LeaveResponse[] newArray(int size) {
            return (new LeaveResponse[size]);
        }

    }
            ;

    //what is most funny moment during jabriya jodi shooting
    // how much you enjoy bihari accent
    protected LeaveResponse(Parcel in) {
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.msg = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.leaverData, (LeaveData.class.getClassLoader()));
        in.readList(this.geodataData, (LeaveData.class.getClassLoader()));
    }

    public LeaveResponse() {
    }

    public String getStatus() {
        return status;
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

    public List<LeaveData> getGeodataData() {
        return geodataData;
    }

    public void setGeodataData(List<LeaveData> geodataData) {
        this.geodataData = geodataData;
    }

    public List<LeaveData> getLeaverData() {
        return leaverData;
    }

    public void setLeaverData(List<LeaveData> leaverData) {
        this.leaverData = leaverData;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(msg);
        dest.writeList(leaverData);
        dest.writeList(geodataData);
    }

    public int describeContents() {
        return  0;
    }

}
