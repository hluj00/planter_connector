package application;

import application.MQTT_Client.Message;
import application.MQTT_Client.MqttClient;
import application.MYsql_Client.MySqlConnection;
import application.classes.FloatMesurement;
import application.classes.Planter;
import application.classes.PlanterAction;
import application.classes.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class Application {
    private MqttClient mqttClient;
    private final MySqlConnection mySqlConnection;
    private List<Planter> planters;
    private HashMap<Integer, User> users;

    public Application(){
        try {
            mqttClient = new MqttClient();
        }catch (Exception ignored){
        }

        mySqlConnection = new MySqlConnection();
        planters = new ArrayList<>();
        users = new HashMap<>();
    }

    public boolean isRunning(){
        return true;
    }

    public void start(){
        mqttClient.connect();
        //mqttClient.subscribe();
        test();
    }

    public boolean isMqttConnected(){
        return mqttClient.isConnected();
    }

    public void updateUsers(){
        for (User user : mySqlConnection.getAllUsers()){
            if (users.containsKey(user.getId())){
                users.put(user.getId(),user);
            }
        }
    }

    public void resubscribe(){
//        updateUsers();
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
                int compare = planters.get(iOld).compareTo(newList.get(iNew)); //+ -> old is bigger
                if (compare == 0){
                    if (!planters.get(iOld).equals(newList.get(iNew))){
                        mqttClient.unsubscribe(planters.get(iOld));
                        mqttClient.subscribe(newList.get(iNew));
                    }
                    iOld += iOld < planters.size() -1 ? 1 : 0;
                    iNew += iNew < newList.size() -1 ? 1 : 0;
                } else if (compare > 0){
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

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        return pattern.matcher(strNum).matches();
    }
    
    public void saveNewMessages(){
        HashMap<String, Message> measurements = mqttClient.getMeasurements();

        for (Planter planter : planters) {
            String topic = planter.getAirHumiditySensorTopic();
            if (measurements.containsKey(topic)){
                Message message = measurements.get(topic);
                if (isNumeric(message.getMessage())){
                    float value = Float.parseFloat(message.getMessage());
                    FloatMesurement measurement = new FloatMesurement(planter.getId(), message.getTime(), value);
                    mySqlConnection.addAirHumidity(measurement);
                }
            }

            topic = planter.getAirTemperatureSensorTopic();
            if (measurements.containsKey(topic)){
                Message message = measurements.get(topic);
                if (isNumeric(message.getMessage())) {
                    float value = Float.parseFloat(message.getMessage());
                    FloatMesurement measurement = new FloatMesurement(planter.getId(), message.getTime(), value);
                    mySqlConnection.addAirTemperature(measurement);
                }
            }

            topic = planter.getLightSensorTopic();
            if (measurements.containsKey(topic)){
                Message message = measurements.get(topic);
                if (isNumeric(message.getMessage())) {
                    float value = Float.parseFloat(message.getMessage());
                    FloatMesurement measurement = new FloatMesurement(planter.getId(), message.getTime(), value);
                    mySqlConnection.addLightLevel(measurement);
                }
            }

            topic = planter.getSoilMoistureSensorTopic();
            if (measurements.containsKey(topic)){
                Message message = measurements.get(topic);
                if (isNumeric(message.getMessage())) {
                    float value = Float.parseFloat(message.getMessage());
                    FloatMesurement measurement = new FloatMesurement(planter.getId(), message.getTime(), value);
                    mySqlConnection.addSoilMoisture(measurement);
                }
            }

            topic = planter.getWaterLevelSensorTopic();
            if (measurements.containsKey(topic)){
                Message message = measurements.get(topic);
                if (isNumeric(message.getMessage())) {
                    float value = Float.parseFloat(message.getMessage());
                    FloatMesurement measurement = new FloatMesurement(planter.getId(), message.getTime(), value);
                    mySqlConnection.addWaterLevel(measurement);
                }
            }
        }

        mqttClient.clearMessages();
    }

    public void executeActions(){
        for (Planter planter : planters) {
            List<PlanterAction> actions = mySqlConnection.findUnexecutedActions(planter);

            for (PlanterAction action:actions) {
                if (action.getType() == 1){  // 1 = run-pump
                    mqttClient.executeWaterPump(planter);
                    action.setExecuted(true);
                    action.setExecutedAt(new Timestamp(System.currentTimeMillis()));
                    mySqlConnection.updateActionExecuted(action);
                }
            }
        }
    }
}
