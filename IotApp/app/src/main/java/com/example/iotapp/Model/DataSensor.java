package com.example.iotapp.Model;

public class DataSensor {
    private int id;
    private double temp, humid, light;
    private String time;

    public DataSensor(int id, double temp, double humid, double light, String time) {
        this.id = id;
        this.temp = temp;
        this.humid = humid;
        this.light = light;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getHumid() {
        return humid;
    }

    public void setHumid(double humid) {
        this.humid = humid;
    }

    public double getLight() {
        return light;
    }

    public void setLight(double light) {
        this.light = light;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
