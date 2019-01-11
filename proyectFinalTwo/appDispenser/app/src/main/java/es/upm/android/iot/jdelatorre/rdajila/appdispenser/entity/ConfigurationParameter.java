package es.upm.android.iot.jdelatorre.rdajila.appdispenser.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfigurationParameter
{
    @SerializedName("ConfigurationId")
    @Expose
    private int ConfigurationId;

    @SerializedName("AmountDailyFood")
    @Expose
    private double AmountDailyFood;

    @SerializedName("AmountBowlFoodWater")
    @Expose
    private double AmountBowlFoodWater;

    @SerializedName("AmountDailyWater")
    @Expose
    private double AmountDailyWater;

    @SerializedName("CurrentDateTime")
    @Expose
    private String CurrentDateTime;

    public int getConfigurationId() {
        return ConfigurationId;
    }

    public void setConfigurationId(int configurationId) {
        ConfigurationId = configurationId;
    }

    public double getAmountDailyFood() {
        return AmountDailyFood;
    }

    public void setAmountDailyFood(double amountDailyFood) {
        AmountDailyFood = amountDailyFood;
    }

    public double getAmountBowlFoodWater() {
        return AmountBowlFoodWater;
    }

    public void setAmountBowlFoodWater(double amountBowlFoodWater) {
        AmountBowlFoodWater = amountBowlFoodWater;
    }

    public double getAmountDailyWater() {
        return AmountDailyWater;
    }

    public void setAmountDailyWater(double amountDailyWater) {
        AmountDailyWater = amountDailyWater;
    }

    public String getCurrentDateTime() {
        return CurrentDateTime;
    }

    public void setCurrentDateTime(String currentDateTime) {
        CurrentDateTime = currentDateTime;
    }
}