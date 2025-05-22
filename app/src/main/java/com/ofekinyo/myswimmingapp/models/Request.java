package com.ofekinyo.myswimmingapp.models;

import java.util.Date;

public class Request {
    Trainee trainee;
    Trainer trainer;


    Date date;
    String goals;
    String details;

    String status;


    public Request(Trainee trainee, Trainer trainer, Date date, String goals, String details, String status) {
        this.trainee = trainee;
        this.trainer = trainer;
        this.date = date;
        this.goals = goals;
        this.details = details;
        this.status = status;
    }
    public Request(Trainee trainee, Trainer trainer, Date date, String goals, String details) {
        this.trainee = trainee;
        this.trainer = trainer;
        this.date = date;
        this.goals = goals;
        this.details = details;
        this.status = "new";
    }


    public Request() {
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Request{" +
                "trainee=" + trainee +
                ", trainer=" + trainer +
                ", date=" + date +
                ", goals='" + goals + '\'' +
                ", details='" + details + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
