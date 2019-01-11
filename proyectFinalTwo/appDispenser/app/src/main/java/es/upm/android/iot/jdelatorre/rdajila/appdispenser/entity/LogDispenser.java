package es.upm.android.iot.jdelatorre.rdajila.appdispenser.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;


public class LogDispenser
{
    @SerializedName("LogId")
    @Expose
    private int LogId;

    @SerializedName("ThingId")
    @Expose
    private String ThingId;

    @SerializedName("ActionType")
    @Expose
    private String ActionType;

    @SerializedName("AmountDailyFood")
    @Expose
    private double AmountDailyFood;

    @SerializedName("AmountBowlFood")
    @Expose
    private double AmountBowlFood;

    @SerializedName("AmountDailyWater")
    @Expose
    private double AmountDailyWater;

    @SerializedName("AmountBowlWater")
    @Expose
    private int AmountBowlWater;

    @SerializedName("CurrentAmountFood")
    @Expose
    private double CurrentAmountFood;

    @SerializedName("AmountFoodDownloaded")
    @Expose
    private double AmountFoodDownloaded;

    @SerializedName("CurrentAmountWater")
    @Expose
    private double CurrentAmountWater;

    @SerializedName("AmountWaterDownloaded")
    @Expose
    private double AmountWaterDownloaded;

    @SerializedName("AngleServoFood")
    @Expose
    private int AngleServoFood;

    @SerializedName("OpeningSecondsFood")
    @Expose
    private int OpeningSecondsFood;

    @SerializedName("AngleServoWater")
    @Expose
    private int AngleServoWater;

    @SerializedName("OpeningSecondsWater")
    @Expose
    private int OpeningSecondsWater;

    @SerializedName("CurrentDateTime")
    @Expose
    private String CurrentDateTime;

    public int getLogId() {
        return LogId;
    }

    public void setLogId(int logId) {
        LogId = logId;
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

    public double getAmountDailyFood() {
        return AmountDailyFood;
    }

    public void setAmountDailyFood(double amountDailyFood) {
        AmountDailyFood = amountDailyFood;
    }

    public double getAmountBowlFood() {
        return AmountBowlFood;
    }

    public void setAmountBowlFood(double amountBowlFood) {
        AmountBowlFood = amountBowlFood;
    }

    public double getAmountDailyWater() {
        return AmountDailyWater;
    }

    public void setAmountDailyWater(double amountDailyWater) {
        AmountDailyWater = amountDailyWater;
    }

    public int getAmountBowlWater() {
        return AmountBowlWater;
    }

    public void setAmountBowlWater(int amountBowlWater) {
        AmountBowlWater = amountBowlWater;
    }

    public double getCurrentAmountFood() {
        return CurrentAmountFood;
    }

    public void setCurrentAmountFood(double currentAmountFood) {
        CurrentAmountFood = currentAmountFood;
    }

    public double getAmountFoodDownloaded() {
        return AmountFoodDownloaded;
    }

    public void setAmountFoodDownloaded(double amountFoodDownloaded) {
        AmountFoodDownloaded = amountFoodDownloaded;
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

    public int getAngleServoFood() {
        return AngleServoFood;
    }

    public void setAngleServoFood(int angleServoFood) {
        AngleServoFood = angleServoFood;
    }

    public int getOpeningSecondsFood() {
        return OpeningSecondsFood;
    }

    public void setOpeningSecondsFood(int openingSecondsFood) {
        OpeningSecondsFood = openingSecondsFood;
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