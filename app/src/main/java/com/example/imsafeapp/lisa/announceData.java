package com.example.imsafeapp.lisa;

public class announceData {
    String name;
    String date;
    String time;
    String location;

    announceData(String name, String date, String time,String location)
    {
        this.name = name;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    //LATEST

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}







