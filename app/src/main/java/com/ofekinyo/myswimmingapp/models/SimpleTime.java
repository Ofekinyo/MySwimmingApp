package com.ofekinyo.myswimmingapp.models;

import androidx.annotation.NonNull;

import java.util.Calendar;

public class SimpleTime implements Comparable<SimpleTime> {
    private int hour;   // 0-23
    private int minute; // 0-59
    private int second; // 0-59

    public SimpleTime(int hour, int minute, int second) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Hour must be between 0 and 23");
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Minute must be between 0 and 59");
        }
        if (second < 0 || second > 59) {
            throw new IllegalArgumentException("Second must be between 0 and 59");
        }
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    @Override
    public int compareTo(SimpleTime other) {
        if (this.hour != other.hour) {
            return Integer.compare(this.hour, other.hour);
        }
        if (this.minute != other.minute) {
            return Integer.compare(this.minute, other.minute);
        }
        return Integer.compare(this.second, other.second);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleTime that = (SimpleTime) o;
        return hour == that.hour && minute == that.minute && second == that.second;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + hour;
        result = 31 * result + minute;
        result = 31 * result + second;
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    // Helper method to create a SimpleTime from a string in format "HH:mm:ss"
    public static SimpleTime fromString(String timeStr) {
        String[] parts = timeStr.split(":");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Time string must be in format HH:mm:ss");
        }
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        int second = Integer.parseInt(parts[2]);
        return new SimpleTime(hour, minute, second);
    }

    // Get current time
    public static SimpleTime now() {
        Calendar calendar = Calendar.getInstance();
        return new SimpleTime(
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND)
        );
    }
} 