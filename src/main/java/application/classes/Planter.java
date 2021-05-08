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
    private final String color;
    private final String waterPumpTopic;
    private final String lightSensorTopic;
    private final String waterLevelSensorTopic;
    private final String soilMoistureSensorTopic;
    private final String airHumiditySensorTopic;
    private final String airTemperatureSensorTopic;

    public Planter(int id, String name, int settingsId, int userId, String color, String userHash) {
        this.id = id;
        this.name = name;
        this.settingsId = settingsId;
        this.userId = userId;
        this.color = color;



        waterPumpTopic = String.format("%s/planter%d/%s", userHash, id, "waterPump");
        lightSensorTopic = String.format("%s/planter%d/%s", userHash, id, "lightSensor");
        waterLevelSensorTopic = String.format("%s/planter%d/%s", userHash, id, "waterLevelSensor");
        soilMoistureSensorTopic = String.format("%s/planter%d/%s", userHash, id, "soilMoistureSensor");
        airHumiditySensorTopic = String.format("%s/planter%d/%s", userHash, id, "airHumiditySensor");
        airTemperatureSensorTopic = String.format("%s/planter%d/%s", userHash, id, "airTemperatureSensor");
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
