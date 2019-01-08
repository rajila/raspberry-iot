package es.upm.android.iot.jdelatorre.rdajila.appdispenser.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaterDispenser
{
    @SerializedName("WaterDispenserId")
    @Expose
    private int WaterDispenserId;

    @SerializedName("ThingId")
    @Expose
    private String ThingId;

    @SerializedName("ActionType")
    @Expose
    private String ActionType;

    @SerializedName("MilliLiterWater")
    @Expose
    private double MilliLiterWater;

    @SerializedName("MinPercentWater")
    @Expose
    private int MinPercentWater;

    @SerializedName("CurrentMilliLiterWater")
    @Expose
    private int CurrentMilliLiterWater;

    @SerializedName("MilliLiterWaterDownloaded")
    @Expose
    private double MilliLiterWaterDownloaded;

    @SerializedName("AngleServoWater")
    @Expose
    private int AngleServoWater;

    @SerializedName("OpeningSecondsWater")
    @Expose
    private int OpeningSecondsWater;

    @SerializedName("CurrentDateTime")
    @Expose
    private String CurrentDateTime;

    public int getWaterDispenserId() {
        return WaterDispenserId;
    }

    public void setWaterDispenserId(int waterDispenserId) {
        WaterDispenserId = waterDispenserId;
    }

    public String getThingId() {
        return ThingId;
    }

    public void setThingId(String thingId) {
        ThingId = thingId;
    }

    public String getActionType() {
        return ActionType;
    }

    public void setActionType(String actionType) {
        ActionType = actionType;
    }

    public double getMilliLiterWater() {
        return MilliLiterWater;
    }

    public void setMilliLiterWater(double milliLiterWater) {
        MilliLiterWater = milliLiterWater;
    }

    public int getMinPercentWater() {
        return MinPercentWater;
    }

    public void setMinPercentWater(int minPercentWater) {
        MinPercentWater = minPercentWater;
    }

    public int getCurrentMilliLiterWater() {
        return CurrentMilliLiterWater;
    }

    public void setCurrentMilliLiterWater(int currentMilliLiterWater) {
        CurrentMilliLiterWater = currentMilliLiterWater;
    }

    public double getMilliLiterWaterDownloaded() {
        return MilliLiterWaterDownloaded;
    }

    public void setMilliLiterWaterDownloaded(double milliLiterWaterDownloaded) {
        MilliLiterWaterDownloaded = milliLiterWaterDownloaded;
    }

    public int getAngleServoWater() {
        return AngleServoWater;
    }

    public void setAngleServoWater(int angleServoWater) {
        AngleServoWater = angleServoWater;
    }

    public int getOpeningSecondsWater() {
        return OpeningSecondsWater;
    }

    public void setOpeningSecondsWater(int openingSecondsWater) {
        OpeningSecondsWater = openingSecondsWater;
    }

    public String getCurrentDateTime() {
        return CurrentDateTime;
    }

    public void setCurrentDateTime(String currentDateTime) {
        CurrentDateTime = currentDateTime;
    }
}