package es.upm.android.iot.jdelatorre.rdajila.appdispenser.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dispenser
{
    @SerializedName("ListConfiguration")
    @Expose
    private List<Configuration> ListConfiguration;

    @SerializedName("ListFoodHour")
    @Expose
    private List<FoodHour> ListFoodHour;

    @SerializedName("ListFoodDispenser")
    @Expose
    private List<FoodDispenser> ListFoodDispenser;

    @SerializedName("ListWaterDispenser")
    @Expose
    private List<WaterDispenser> ListWaterDispenser;

    @SerializedName("ListLastLog")
    @Expose
    private List<LogDispenser> ListLastLog;

    public List<Configuration> getListConfiguration() {
        return ListConfiguration;
    }

    public void setListConfiguration(List<Configuration> listConfiguration) {
        ListConfiguration = listConfiguration;
    }

    public List<FoodHour> getListFoodHour() {
        return ListFoodHour;
    }

    public void setListFoodHour(List<FoodHour> listFoodHour) {
        ListFoodHour = listFoodHour;
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