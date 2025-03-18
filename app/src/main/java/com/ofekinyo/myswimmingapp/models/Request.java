package com.ofekinyo.myswimmingapp.models;

public class Request {
    private String trainerName;
    private String status;

    public Request(String trainerName, String status) {
        this.trainerName = trainerName;
        this.status = status;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
