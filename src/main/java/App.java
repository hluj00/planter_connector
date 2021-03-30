import application.Application;
import application.MYsql_Client.MySqlConnection;
import application.classes.FloatMesurement;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.sql.Timestamp;

public class App
{


    public static void main( String[] args )throws MqttException
    {
        Application a = new Application();
        a.start();
        long lastSec = -1;
        while(true){
            long sec = System.currentTimeMillis() / 1000;
            if (sec >= lastSec+5 || sec < lastSec) {
                System.out.println("tada");
                a.resubscribe();
                //todo send alert
                lastSec = sec;
                a.checkMessages();
            }//If():
        }//While


    }
}