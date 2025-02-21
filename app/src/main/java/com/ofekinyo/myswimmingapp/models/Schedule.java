package com.ofekinyo.myswimmingapp.models;

public class Schedule {
    private String id; // Assuming you have an ID for each schedule
    private String title;
    private String date;
    private String time;

    // Default constructor required for calls to DataSnapshot.getValue(Schedule.class)
    public Schedule() {
    }

    public Schedule(String id, String title, String date, String time) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
