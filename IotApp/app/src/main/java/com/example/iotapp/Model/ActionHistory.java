package com.example.iotapp.Model;

public class ActionHistory {
    private int id;
    private String device, action, time;

    public ActionHistory(int id, String device, String action, String time) {
        this.id = id;
        this.device = device;
        this.action = action;
        this.time = time;
    }

    public ActionHistory(String device, String action, String time) {
        this.device = device;
        this.action = action;
        this.time = time;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
