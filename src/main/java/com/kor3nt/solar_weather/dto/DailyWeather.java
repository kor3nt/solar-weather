package com.kor3nt.solar_weather.dto;

public class DailyWeather {
    private String date;
    private int weatherCode;
    private double minTemp;
    private double maxTemp;
    private double energyKwh;

    public DailyWeather() {}

    public DailyWeather(String date, int weatherCode, double minTemp, double maxTemp, double energyKwh) {
        this.date = date;
        this.weatherCode = weatherCode;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.energyKwh = energyKwh;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public int getWeatherCode() { return weatherCode; }
    public void setWeatherCode(int weatherCode) { this.weatherCode = weatherCode; }
    public double getMinTemp() { return minTemp; }
    public void setMinTemp(double minTemp) { this.minTemp = minTemp; }
    public double getMaxTemp() { return maxTemp; }
    public void setMaxTemp(double maxTemp) { this.maxTemp = maxTemp; }
    public double getEnergyKwh() { return energyKwh; }
    public void setEnergyKwh(double energyKwh) { this.energyKwh = energyKwh; }
}