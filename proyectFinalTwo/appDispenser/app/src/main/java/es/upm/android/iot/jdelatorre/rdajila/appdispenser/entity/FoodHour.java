package es.upm.android.iot.jdelatorre.rdajila.appdispenser.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FoodHour
{
    @SerializedName("FoodHourId")
    @Expose
    private int FoodHourId;

    @SerializedName("FoodHourType")
    @Expose
    private String FoodHourType;

    @SerializedName("Hour")
    @Expose
    private String Hour;

    public int getFoodHourId() {
        return FoodHourId;
    }

    public void setFoodHourId(int foodHourId) {
        FoodHourId = foodHourId;
    }

    public String getFoodHourType() {
        return FoodHourType;
    }

    public void setFoodHourType(String foodHourType) {
        FoodHourType = foodHourType;
    }

    public String getHour() {
        return Hour;
    }

    public void setHour(String hour) {
        Hour = hour;
    }
}
