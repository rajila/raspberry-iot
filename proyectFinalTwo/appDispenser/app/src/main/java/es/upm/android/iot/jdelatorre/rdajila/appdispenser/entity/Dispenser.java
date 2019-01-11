package es.upm.android.iot.jdelatorre.rdajila.appdispenser.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dispenser
{
    @SerializedName("ListConfigurationParameter")
    @Expose
    private List<ConfigurationParameter> ListConfigurationParameter;

    @SerializedName("ListFoodDispenser")
    @Expose
    private List<FoodDispenser> ListFoodDispenser;

    @SerializedName("ListWaterDispenser")
    @Expose
    private List<WaterDispenser> ListWaterDispenser;

    @SerializedName("ListLastLog")
    @Expose
    private List<LogDispenser> ListLastLog;

    public List<ConfigurationParameter> getListConfigurationParameter() {
        return ListConfigurationParameter;
    }

    public void setListConfigurationParameter(List<ConfigurationParameter> listConfigurationParameter) {
        ListConfigurationParameter = listConfigurationParameter;
    }

    public List<FoodDispenser> getListFoodDispenser() {
        return ListFoodDispenser;
    }

    public void setListFoodDispenser(List<FoodDispenser> listFoodDispenser) {
        ListFoodDispenser = listFoodDispenser;
    }

    public List<WaterDispenser> getListWaterDispenser() {
        return ListWaterDispenser;
    }

    public void setListWaterDispenser(List<WaterDispenser> listWaterDispenser) {
        ListWaterDispenser = listWaterDispenser;
    }

    public List<LogDispenser> getListLastLog() {
        return ListLastLog;
    }

    public void setListLastLog(List<LogDispenser> listLastLog) {
        ListLastLog = listLastLog;
    }
}