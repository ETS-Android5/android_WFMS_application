
package com.wfms.nectar.jsonModelResponses.Fuel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmployeResponse implements Parcelable
{
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<EmployeData> supplierData = null;
    @SerializedName("assign_geofence")
    @Expose
    private List<EmployeData> GeofencedatarData = null;
    @SerializedName("user_id")
    @Expose
    private List<EmployeData> employerData = null;

    public final static Creator<EmployeResponse> CREATOR = new Creator<EmployeResponse>() {
        @SuppressWarnings({
                "unchecked"
        })
        public EmployeResponse createFromParcel(Parcel in) {
            return new EmployeResponse(in);
        }

        public EmployeResponse[] newArray(int size) {
            return (new EmployeResponse[size]);
        }

    }
            ;

    //what is most funny moment during jabriya jodi shooting
    // how much you enjoy bihari accent
    protected EmployeResponse(Parcel in) {
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.msg = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.supplierData, (EmployeData.class.getClassLoader()));
        in.readList(this.employerData, (EmployeData.class.getClassLoader()));
        in.readList(this.GeofencedatarData, (EmployeData.class.getClassLoader()));
    }

    public EmployeResponse() {
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

    public List<EmployeData> getGeofencedatarData() {
        return GeofencedatarData;
    }

    public void setGeofencedatarData(List<EmployeData> geofencedatarData) {
        GeofencedatarData = geofencedatarData;
    }

    public List<EmployeData> getSupplierData() {
        return supplierData;
    }

    public void setSupplierData(List<EmployeData> supplierData) {
        this.supplierData = supplierData;
    }

    public List<EmployeData> getEmployerData() {
        return employerData;
    }

    public void setEmployerData(List<EmployeData> employerData) {
        this.employerData = employerData;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(msg);
        dest.writeList(supplierData);
        dest.writeList(GeofencedatarData);
    }

    public int describeContents() {
        return  0;
    }

}
