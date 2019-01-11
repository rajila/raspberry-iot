package es.upm.android.iot.jdelatorre.rdajila.appdispenser.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FoodDispenser
{
    @SerializedName("FoodDispenserId")
    @Expose
    private int FoodDispenserId;

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

    @SerializedName("CurrentAmountFood")
    @Expose
    private double CurrentAmountFood;

    @SerializedName("AmountFoodDownloaded")
    @Expose
    private double AmountFoodDownloaded;

    @SerializedName("AngleServoFood")
    @Expose
    private int AngleServoFood;

    @SerializedName("OpeningSecondsFood")
    @Expose
    private int OpeningSecondsFood;

    @SerializedName("CurrentDateTime")
    @Expose
    private String CurrentDateTime;

    public int getFoodDispenserId() {
        return FoodDispenserId;
    }

    public void setFoodDispenserId(int foodDispenserId) {
        FoodDispenserId = foodDispenserId;
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

    public String getCurrentDateTime() {
        return CurrentDateTime;
    }

    public void setCurrentDateTime(String currentDateTime) {
        CurrentDateTime = currentDateTime;
    }
}