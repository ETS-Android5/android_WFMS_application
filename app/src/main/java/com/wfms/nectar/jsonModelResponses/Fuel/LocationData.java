
package com.wfms.nectar.jsonModelResponses.Fuel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class LocationData implements Parcelable
{

    @SerializedName("fuel_id")
    @Expose
    private String FuelId;
    @SerializedName("filled_date")
    @Expose
    private String filled_date;

    public String getFilled_date() {
        return filled_date;
    }

    public void setFilled_date(String filled_date) {
        this.filled_date = filled_date;
    }

    @SerializedName("bill")
    @Expose
    private String bill;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("filled_time")
    @Expose
    private String filled_time;
    @SerializedName("filled_at")
    @Expose
    private String filled_at;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("vehicle_id")
    @Expose
    private String vehicle_id;
    @SerializedName("vehicle_name")
    @Expose
    private String vehicle_name;
    @SerializedName("vehicle_no")
    @Expose
    private String vehicle_no;

    public Map<String, String> mStringNumberMap;

    public Map<String, String> getmStringNumberMap() {
        return mStringNumberMap;
    }

    public void setmStringNumberMap(Map<String, String> mStringNumberMap) {
        this.mStringNumberMap = mStringNumberMap;
    }

    public final static Creator<LocationData> CREATOR = new Creator<LocationData>() {


        @SuppressWarnings({
            "unchecked"
        })
        public LocationData createFromParcel(Parcel in) {
            return new LocationData(in);
        }

        public LocationData[] newArray(int size) {
            return (new LocationData[size]);
        }

    }
    ;

    protected LocationData(Parcel in) {
        this.FuelId = ((String) in.readValue((String.class.getClassLoader())));
        this.filled_at = ((String) in.readValue((String.class.getClassLoader())));
        this.vehicle_id = ((String) in.readValue((String.class.getClassLoader())));
        this.quantity = ((String) in.readValue((String.class.getClassLoader())));
        this.amount = ((String) in.readValue((String.class.getClassLoader())));
        this.bill = ((String) in.readValue((String.class.getClassLoader())));
        this.location = ((String) in.readValue((String.class.getClassLoader())));
        this.filled_time = ((String) in.readValue((String.class.getClassLoader())));
        this.filled_date = ((String) in.readValue((String.class.getClassLoader())));
    }

    public LocationData() {
    }

    public String getFuelId() {
        return FuelId;
    }

    public void setFuelId(String fuelId) {
        FuelId = fuelId;
    }

    public String getFilled_at() {
        return filled_at;
    }

    public void setFilled_at(String filled_at) {
        this.filled_at = filled_at;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFilled_time() {
        return filled_time;
    }

    public void setFilled_time(String filled_time) {
        this.filled_time = filled_time;
    }





    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(FuelId);
        dest.writeValue(filled_at);
        dest.writeValue(vehicle_id);
        dest.writeValue(quantity);
        dest.writeValue(amount);
        dest.writeValue(bill);
        dest.writeValue(location);
        dest.writeValue(filled_time);
    }

    public int describeContents() {
        return  0;
    }

}
