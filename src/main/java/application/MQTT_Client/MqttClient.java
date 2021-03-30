package application.MQTT_Client;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import application.classes.Planter;
import org.eclipse.paho.client.mqttv3.*;


public class MqttClient {
    enum TopicType{
        measurement,
        action
    }

    private static MqttAsyncClient myClient;
    private static MyCallback myCallback;


    public MqttClient() throws MqttException{
        myClient = new MqttAsyncClient("tcp://192.168.1.166:1883", UUID.randomUUID().toString());
        myCallback = new MyCallback();
        myClient.setCallback(myCallback);
    }

    public boolean connect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("test");
        options.setPassword("raspberry".toCharArray());

        try {
            IMqttToken token = myClient.connect(options);
            token.waitForCompletion();
        }catch (MqttException $e){
            //todo handle exception
            return false;
        }
        System.out.println("pripojeno");
        return true;
    }

    public void subscribe(String topic, TopicType topicType){
            try {
                myClient.subscribe(topic, 0);
                myCallback.addTopic(topic,topicType);
                System.out.println("sub to " + topic);
            } catch (MqttException e){
                //todo log e
            }
    }

    public void unsubscribe(String topic){
        try {
            myClient.unsubscribe(topic);
            myCallback.removeTopic(topic);
            System.out.println("unSub to " + topic);
        }catch (MqttException e){
            //todo log e
        }
    }

    public void subscribe(Planter planter){
        for (String topic: planter.getSensorTopics()) {
            subscribe(topic, TopicType.measurement);
        }

        subscribe(planter.getWaterPumpTopic(), TopicType.action);
    }

    public void unsubscribe(Planter planter){


        for (String topic: planter.getAllTopics()) {
            unsubscribe(topic);
        }
    }

    public void publish(String topic, MqttMessage message) throws Exception {
        myClient.publish(topic, message.getPayload(), 0, false);
    }

    public HashMap<String, Message> getMeasurements() {
        return myCallback.getMeasurements();
    }

    public void clearMessages() {
        myCallback.clearMeasurements();
    }
}
