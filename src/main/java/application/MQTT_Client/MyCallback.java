package application.MQTT_Client;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.sql.Timestamp;
import java.util.HashMap;

public class MyCallback implements MqttCallback
{
    private HashMap<String, Message> measurements;
    private HashMap<String, MqttClient.TopicType> topicTypes;

    public MyCallback(){
        measurements = new HashMap<>();
        topicTypes = new HashMap<>();
    }

    public void messageArrived(String topic, MqttMessage message){
        if (topicTypes.get(topic) == MqttClient.TopicType.measurement){
            measurements.put(topic, new Message(topic, message.toString(), new Timestamp(System.currentTimeMillis())));
        }
    }

    public void addTopic(String topic, MqttClient.TopicType type){
        topicTypes.put(topic, type);
    }

    public void removeTopic(String topic){
        topicTypes.remove(topic);
    }

    public HashMap<String, Message> getMeasurements(){
        return measurements;
    }

    public void clearMeasurements(){
        measurements.clear();
    }

    public void connectionLost(Throwable arg0) {

    }

    public void deliveryComplete(IMqttDeliveryToken arg0) {

    }

}

