package com.example.covidtracker;

public class AxisData {

    String healthStatus;
    int numbers;

    public AxisData(String healthStatus, int numbers) {
        this.healthStatus = healthStatus;
        this.numbers = numbers;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }
}
