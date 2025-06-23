package com.ofekinyo.myswimmingapp.models;

public class Schedule {
    private String id; // Assuming you have an ID for each schedule
    private String title;
    private SimpleDate date;
    private SimpleTime time;
    private String tutorName;
    private String swimmerName;

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

    public Schedule(String id, String title, SimpleDate date, SimpleTime time, String tutorName, String swimmerName) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.tutorName = tutorName;
        this.swimmerName = swimmerName;
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

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public String getSwimmerName() {
        return swimmerName;
    }

    public void setSwimmerName(String swimmerName) {
        this.swimmerName = swimmerName;
    }
}
