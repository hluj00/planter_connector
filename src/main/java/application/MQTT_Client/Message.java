package application.MQTT_Client;

import java.sql.Timestamp;
import java.util.Objects;

public class Message {
    private String topic;
    private String message;
    private Timestamp time;

    public Message(String topic, String message, Timestamp time){
        this.topic = topic;
        this.message = message;
        this.time = time;
    }

    public String getTopic() {
        return topic;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getTime() {
        return time;
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
