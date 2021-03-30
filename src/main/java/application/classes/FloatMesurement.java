package application.classes;

import org.joda.time.DateTime;

import java.sql.Timestamp;


public class FloatMesurement {

    int planterId;
    Timestamp dateTime;
    float mesurement;

    public FloatMesurement(int planterId, Timestamp dateTime, float mesurement) {
        this.planterId = planterId;
        this.dateTime = dateTime;
        this.mesurement = mesurement;
    }

    public int getPlanterId() {
        return planterId;
    }

    public void setPlanterId(int planterId) {
        this.planterId = planterId;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public float getMesurement() {
        return mesurement;
    }

    public void setMesurement(float mesurement) {
        this.mesurement = mesurement;
    }
}
