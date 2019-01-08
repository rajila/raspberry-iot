package es.upm.android.iot.jdelatorre.rdajila.appdispenser.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Configuration
{
    @SerializedName("ConfigurationId")
    @Expose
    private int ConfigurationId;

    @SerializedName("AmountDailyFood")
    @Expose
    private double AmountDailyFood;

    @SerializedName("MilliLiterWater")
    @Expose
    private double MilliLiterWater;

    @SerializedName("MinPercentWater")
    @Expose
    private int MinPercentWater;

    @SerializedName("CurrentDateTime")
    @Expose
    public String CurrentDateTime;

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

    public String getCurrentDateTime() {
        return CurrentDateTime;
    }

    public void setCurrentDateTime(String currentDateTime) {
        CurrentDateTime = currentDateTime;
    }
}