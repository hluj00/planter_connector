package application.MQTT_Client;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import application.classes.Planter;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


public class MqttClient {
    enum TopicType{
        measurement,
        action
    }

    private static MqttAsyncClient myClient;
    private static MyCallback myCallback;

    public boolean isConnected(){
        return myClient.isConnected();
    }


    public MqttClient() throws MqttException{
        //https://github.com/eclipse/paho.mqtt.java/issues/810
        MemoryPersistence lMemoryPersistence = new MemoryPersistence();

        myClient = new MqttAsyncClient("tcp://192.168.1.166:1883", UUID.randomUUID().toString(), lMemoryPersistence);
//        myClient = new MqttAsyncClient("tcp://192.168.2.199:1883", UUID.randomUUID().toString(), lMemoryPersistence);
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

    public void executeWaterPump(Planter planter){
        String topic = planter.getWaterPumpTopic();
        //https://en.wikipedia.org/wiki/List_of_Unicode_characters#Number_Forms
        char a = '\u0031'; //1
        byte b = (byte)a;
        byte[] payload = {b} ;
        MqttMessage msg = new MqttMessage(payload);
        try {
            myClient.publish(topic, msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
