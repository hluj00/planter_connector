package application.classes;

//class SortById implements Comparator<Planter>
//{
//    // Used for sorting in ascending order of
//    // roll number
//    public int compare(Planter a, Planter b)
//    {
//        return a.getId() - b.getId();
//    }
//}

import java.util.ArrayList;

public class Planter implements Comparable<Planter>{
    private int id;
    private String name;
    private int settingsId;
    private int userId;
    private String color;
    private String waterPumpTopic;
    private String lightSensorTopic;
    private String waterLevelSensorTopic;
    private String soilMoistureSensorTopic;
    private String airHumiditySensorTopic;
    private String airTemperatureSensorTopic;

    public Planter(int id, String name, int settingsId, int userId, String color) {
        this.id = id;
        this.name = name;
        this.settingsId = settingsId;
        this.userId = userId;
        this.color = color;



        waterPumpTopic = String.format("%d/%s/%s", userId, name, "waterPump");
        lightSensorTopic = String.format("%d/%s/%s", userId, name, "lightSensor");
        waterLevelSensorTopic = String.format("%d/%s/%s", userId, name, "waterLevelSensor");
        soilMoistureSensorTopic = String.format("%d/%s/%s", userId, name, "soilMoistureSensor");
        airHumiditySensorTopic = String.format("%d/%s/%s", userId, name, "airHumiditySensor");
        airTemperatureSensorTopic = String.format("%d/%s/%s", userId, name, "airTemperatureSensor");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSettingsId() {
        return settingsId;
    }

    public void setSettingsId(int settingsId) {
        this.settingsId = settingsId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ArrayList<String> getSensorTopics(){
        ArrayList<String> result = new ArrayList<>();
        result.add(this.lightSensorTopic);
        result.add(this.waterLevelSensorTopic);
        result.add(this.soilMoistureSensorTopic);
        result.add(this.airHumiditySensorTopic);
        result.add(this.airTemperatureSensorTopic);

        return result;
    }

    public String getColor() {
        return color;
    }

    public ArrayList<String> getAllTopics(){
        ArrayList<String> result = this.getSensorTopics();
        result.add(this.getWaterPumpTopic());
        return result;
    }

    public String getWaterPumpTopic() {
        return waterPumpTopic;
    }

    public String getLightSensorTopic() {
        return lightSensorTopic;
    }

    public String getWaterLevelSensorTopic() {
        return waterLevelSensorTopic;
    }

    public String getSoilMoistureSensorTopic() {
        return soilMoistureSensorTopic;
    }

    public String getAirHumiditySensorTopic() {
        return airHumiditySensorTopic;
    }

    public String getAirTemperatureSensorTopic() {
        return airTemperatureSensorTopic;
    }

    @Override
    public int compareTo(Planter p) {
        return this.getId() - p.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Planter)) {
            return false;
        }
        Planter other = (Planter) o;
        return (other.getId() == this.getId() && other.getUserId() == this.getUserId() && other.getName().equals(this.getName()));
    }
}
