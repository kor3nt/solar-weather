package com.kor3nt.solar_weather.dto;

public class WeeklySummaryResponse {

    private double avgPressure;
    private double avgSunshineDuration;
    private double minTemperature;
    private double maxTemperature;
    private String summary;

    public WeeklySummaryResponse() {}

    public WeeklySummaryResponse(double avgPressure, double avgSunshineDuration, double minTemperature, double maxTemperature, String summary) {
        this.avgPressure = avgPressure;
        this.avgSunshineDuration = avgSunshineDuration;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.summary = summary;
    }

    public double getAvgPressure() {
        return avgPressure;
    }

    public void setAvgPressure(double avgPressure) {
        this.avgPressure = avgPressure;
    }

    public double getAvgSunshineDuration() {
        return avgSunshineDuration;
    }

    public void setAvgSunshineDuration(double avgSunshineDuration) {
        this.avgSunshineDuration = avgSunshineDuration;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
