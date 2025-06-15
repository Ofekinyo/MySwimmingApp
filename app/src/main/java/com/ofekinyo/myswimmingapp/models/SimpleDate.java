package com.ofekinyo.myswimmingapp.models;

import androidx.annotation.NonNull;

import java.util.Calendar;

public class SimpleDate implements Comparable<SimpleDate> {
    private int year;
    private int month; // 0-11
    private int day;

    public SimpleDate(int year, int month, int day) {
        if (month < 0 || month > 11) {
            throw new IllegalArgumentException("Month must be between 0 and 11");
        }
        if (day < 1 || day > getDaysInMonth(year, month)) {
            throw new IllegalArgumentException("Invalid day for the given month and year");
        }
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    private int getDaysInMonth(int year, int month) {
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (month == 1 && isLeapYear(year)) { // February in leap year
            return 29;
        }
        return daysInMonth[month];
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    @Override
    public int compareTo(SimpleDate other) {
        if (this.year != other.year) {
            return Integer.compare(this.year, other.year);
        }
        if (this.month != other.month) {
            return Integer.compare(this.month, other.month);
        }
        return Integer.compare(this.day, other.day);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleDate that = (SimpleDate) o;
        return year == that.year && month == that.month && day == that.day;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + year;
        result = 31 * result + month;
        result = 31 * result + day;
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%04d-%02d-%02d", year, month + 1, day);
    }

    // Helper method to create a SimpleDate from a string in format "YYYY-MM-DD"
    public static SimpleDate fromString(String dateStr) {
        String[] parts = dateStr.split("/");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Date string must be in format DD/MM/YYYY");
        }
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]) - 1; // Convert to 0-based (0 = January)
        int year = Integer.parseInt(parts[2]);
        return new SimpleDate(year, month, day);
    }

    // Get current date
    public static SimpleDate now() {
        Calendar calendar = Calendar.getInstance();
        return new SimpleDate(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH), // Calendar.MONTH is already 0-11
            calendar.get(Calendar.DAY_OF_MONTH)
        );
    }
} 