package application;

import application.MQTT_Client.Message;
import application.MQTT_Client.MqttClient;
import application.MYsql_Client.MySqlConnection;
import application.classes.FloatMesurement;
import application.classes.Planter;
import application.classes.User;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Application {
    private MqttClient mqttClient;
    private MySqlConnection mySqlConnection;
    private List<Planter> planters;

    public Application() throws MqttException{
        mqttClient = new MqttClient();
        mySqlConnection = new MySqlConnection();
        planters = new ArrayList<Planter>();
    }

    public void start()throws MqttException {
        mqttClient.connect();
        //mqttClient.subscribe();
        test();
    }

    public void resubscribe(){
        List<Planter> newList = mySqlConnection.getAllPlanters();
        int size = Math.max(planters.size(), newList.size());

        if (planters.size() == 0){
            for (Planter planter : newList) {
                mqttClient.subscribe(planter);
            }
        }else if (newList.size() == 0){
            for (Planter planter : newList) {
                mqttClient.unsubscribe(planter);
            }
        } else {

            int iOld = 0;
            int iNew = 0;
            int iSum = -1;
            while (iSum != iNew + iOld) {
                iSum = iNew + iOld;
                int compare = planters.get(iOld).compareTo(newList.get(iNew)); //+ -> this je vetsi
                if (compare == 0){
                    if (!planters.get(iOld).equals(newList.get(iNew))){
                        mqttClient.unsubscribe(planters.get(iOld));
                        mqttClient.subscribe(newList.get(iNew));
                    }
                    iOld += iOld < planters.size() -1 ? 1 : 0;
                    iNew += iNew < newList.size() -1 ? 1 : 0;
                } else if (compare < 0){
                    mqttClient.unsubscribe(planters.get(iOld));
                    if (iNew < newList.size() - 1){
                        iNew++;
                    }else if (iOld < planters.size() - 1){
                        iOld++;
                    }
                } else {
                    mqttClient.subscribe(newList.get(iNew));
                    if (iOld < planters.size() - 1){
                        iOld++;
                    }else if (iNew < newList.size() - 1){
                        iNew++;
                    }
                }
            }
        }
        planters = newList;
    }

    private void test(){
        mySqlConnection.makeJDBCConnection();
        for (User user : mySqlConnection.getAllUsers()){
            System.out.println(user.toString());
        }
    }

    private Planter getPlanter(int userId, String name){
        System.out.println(String.format("hledam %s (%d) ", name, userId));
        for (Planter planter : planters) {
            System.out.printf("porovnavam %s (%d) %n", planter.getName(), planter.getUserId());
            System.out.println(planter.getUserId() == userId);
            System.out.println(planter.getName().equals(name));
            if (planter.getUserId() == userId && planter.getName().equals(name)){
                System.out.println("nasel");
                return planter;
            }
        }
        return null;
    }
    
    public void checkMessages(){
        HashMap<String, Message> measurements = mqttClient.getMeasurements();

        for (Planter planter : planters) {
            String topic = planter.getAirHumiditySensorTopic();
            if (measurements.containsKey(topic)){
                Message message = measurements.get(topic);
                FloatMesurement measurement = new FloatMesurement(planter.getId(), new Timestamp(System.currentTimeMillis()), Float.parseFloat(message.getMessage()));
                mySqlConnection.addAirHumidity(measurement);
            }

            topic = planter.getAirTemperatureSensorTopic();
            if (measurements.containsKey(topic)){
                Message message = measurements.get(topic);
                FloatMesurement measurement = new FloatMesurement(planter.getId(), new Timestamp(System.currentTimeMillis()), Float.parseFloat(message.getMessage()));
                mySqlConnection.addAirTemperature(measurement);
            }

            topic = planter.getLightSensorTopic();
            if (measurements.containsKey(topic)){
                Message message = measurements.get(topic);
                FloatMesurement measurement = new FloatMesurement(planter.getId(), new Timestamp(System.currentTimeMillis()), Float.parseFloat(message.getMessage()));
                mySqlConnection.addLightLevel(measurement);
            }

            topic = planter.getSoilMoistureSensorTopic();
            if (measurements.containsKey(topic)){
                Message message = measurements.get(topic);
                FloatMesurement measurement = new FloatMesurement(planter.getId(), new Timestamp(System.currentTimeMillis()), Float.parseFloat(message.getMessage()));
                mySqlConnection.addSoilMoisture(measurement);
            }

            topic = planter.getWaterLevelSensorTopic();
            if (measurements.containsKey(topic)){
                Message message = measurements.get(topic);
                FloatMesurement measurement = new FloatMesurement(planter.getId(), new Timestamp(System.currentTimeMillis()), Float.parseFloat(message.getMessage()));
                mySqlConnection.addWaterLevel(measurement);
            }
        }

        mqttClient.clearMessages();
    }
}
