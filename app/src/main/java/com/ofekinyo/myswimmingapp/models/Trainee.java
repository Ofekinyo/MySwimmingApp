package com.ofekinyo.myswimmingapp.models;

import java.io.Serializable;


public class Trainee extends User implements Serializable {
    double height,Weight;
    int age;

    User myCoach;
    public Trainee(String id, String Fname, String Lname, int phone, String email, String pass, String gender, String city, String password, double height, double weight, int age, User myCoach) {
        super(id, Fname, Lname, phone, email, age, gender, city ,password);
        this.height = height;
        this.Weight = weight;
        this.age = age;

        this.myCoach = myCoach;

    }

    public Trainee(String id, String Fname, String Lname, int phone, String email, String gender, String city, String password, double height, double weight, int age, User myCoach) {
        super(id, Fname, Lname, phone, email, age, gender, city ,password);  // Now passing all necessary parameters
        this.height = height;
        this.Weight = weight;
        this.age = age;
        this.myCoach = myCoach;
    }


    public Trainee(User user, double height, double weight, int age, User myCoach) {
        super(user);
        this.height = height;
        Weight = weight;
        this.age = age;
        this.myCoach = myCoach;
    }

    public Trainee(double height, double weight, int age, User myCoach) {
        this.height = height;
        Weight = weight;
        this.age = age;
        this.myCoach = myCoach;
    }

    public Trainee(Trainee trainee) {
        super(trainee);  // Calling the copy constructor of the User class
        this.height = trainee.height;
        this.Weight = trainee.Weight;  // Correcting the assignment to Weight
        this.age = trainee.age;

        // Check if the coach is not null and assign it
        if (trainee.myCoach != null) {
            this.myCoach = trainee.getCoach();  // Assuming you have a getter method for myCoach
        }
    }
    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public User getCoach() {
        return myCoach;
    }

    public void setCoach(User myCoach) {
        this.myCoach = myCoach;
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "height=" + height +
                ", Weight=" + Weight +
                ", age=" + age +
                ", myCoach=" + myCoach +
                ", id='" + id + '\'' +
                ", Fname='" + Fname + '\'' +
                ", Lname='" + Lname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", city='" + city + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}