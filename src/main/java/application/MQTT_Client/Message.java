package application.MQTT_Client;

import application.classes.Planter;

import java.util.Objects;

public class Message {
    private String topic;
    private String message;

    public Message(String topic, String message){
        this.topic = topic;
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic,message);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Message)) {
            return false;
        }
        Message other = (Message) obj;
        return (this.topic.equals(other.getTopic()) && this.message.equals(other.getMessage()));
    }
}
