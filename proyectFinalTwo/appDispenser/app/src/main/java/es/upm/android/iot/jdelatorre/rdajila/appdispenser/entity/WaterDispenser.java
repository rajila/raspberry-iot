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

    @SerializedName("AmountDailyWater")
    @Expose
    private double AmountDailyWater;

    @SerializedName("AmountBowlWater")
    @Expose
    private double AmountBowlWater;

    @SerializedName("CurrentAmountWater")
    @Expose
    private double CurrentAmountWater;

    @SerializedName("AmountWaterDownloaded")
    @Expose
    private double AmountWaterDownloaded;

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

    public double getAmountDailyWater() {
        return AmountDailyWater;
    }

    public void setAmountDailyWater(double amountDailyWater) {
        AmountDailyWater = amountDailyWater;
    }

    public double getAmountBowlWater() {
        return AmountBowlWater;
    }

    public void setAmountBowlWater(double amountBowlWater) {
        AmountBowlWater = amountBowlWater;
    }

    public double getCurrentAmountWater() {
        return CurrentAmountWater;
    }

    public void setCurrentAmountWater(double currentAmountWater) {
        CurrentAmountWater = currentAmountWater;
    }

    public double getAmountWaterDownloaded() {
        return AmountWaterDownloaded;
    }

    public void setAmountWaterDownloaded(double amountWaterDownloaded) {
        AmountWaterDownloaded = amountWaterDownloaded;
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