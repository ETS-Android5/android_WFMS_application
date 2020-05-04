
package com.wfms.nectar.jsonModelResponses.Fuel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class LocationResponse implements Parcelable
{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose

    public Map<String, String> mStringNumberMap;

    public Map<String, String> getmStringNumberMap() {
        return mStringNumberMap;
    }

    public void setmStringNumberMap(Map<String, String> mStringNumberMap) {
        this.mStringNumberMap = mStringNumberMap;
    }

    // private List<String> data = null;
    private List<LocationData> supplierData = null;
    public final static Creator<LocationResponse> CREATOR = new Creator<LocationResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LocationResponse createFromParcel(Parcel in) {
            return new LocationResponse(in);
        }

        public LocationResponse[] newArray(int size) {
            return (new LocationResponse[size]);
        }

    }
            ;

    protected LocationResponse(Parcel in) {
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.msg = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.supplierData, (EmployeData.class.getClassLoader()));
       // in.readList(this.data, (EmployeData.class.getClassLoader()));
    }

    public LocationResponse() {
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

   /* public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
*/
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<LocationData> getSupplierData() {
        return supplierData;
    }

    public void setSupplierData(List<LocationData> supplierData) {
        this.supplierData = supplierData;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(msg);
        dest.writeList(supplierData);
    }

    public int describeContents() {
        return  0;
    }

}
