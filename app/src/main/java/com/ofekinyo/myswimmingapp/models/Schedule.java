package com.ofekinyo.myswimmingapp.models;

public class Schedule {
    private String id; // Assuming you have an ID for each schedule
    private String title;
    private SimpleDate date;
    private SimpleTime time;

    // Default constructor required for calls to DataSnapshot.getValue(ScheduleActivity.class)
    public Schedule() {
        this.date = SimpleDate.now();
        this.time = SimpleTime.now();
    }

    public Schedule(String id, String title, SimpleDate date, SimpleTime time) {
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

    public SimpleDate getDate() {
        return date;
    }

    public void setDate(SimpleDate date) {
        this.date = date;
    }

    public SimpleTime getTime() {
        return time;
    }

    public void setTime(SimpleTime time) {
        this.time = time;
    }
}
