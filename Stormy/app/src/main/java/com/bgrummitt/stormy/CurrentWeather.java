package com.bgrummitt.stormy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CurrentWeather {

    private String locationLabel;
    private String icon;
    private long time;
    private float temperature;
    private double humidity;
    private double precipChance;
    private String summary;
    private String timeZone;

    public CurrentWeather() {}

    public CurrentWeather(String locationLabel, String icon, long time, float temperature, double humidity, double precipChance, String summary, String timeZone) {
        this.locationLabel = locationLabel;
        this.icon = icon;
        this.time = time;
        this.temperature = Math.round(temperature * 10) / (float) 10;
        this.humidity = humidity;
        this.precipChance = precipChance;
        this.summary = summary;
        this.timeZone = timeZone;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getLocationLabel() {
        return locationLabel;
    }

    public void setLocationLabel(String locationLabel) {
        this.locationLabel = locationLabel;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getIconID(){
        //clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night

        int iconId = R.drawable.clear_day;

        switch (icon){
            case "clear-day":
                iconId = R.drawable.clear_day;
                break;
            case "clear-night":
                iconId = R.drawable.clear_night;
            case "rain":
                iconId = R.drawable.rain;
                break;
            case "snow":
                iconId = R.drawable.snow;
                break;
            case "sleet":
                iconId = R.drawable.sleet;
                break;
            case "wind":
                iconId = R.drawable.wind;
                break;
            case "fog":
                iconId = R.drawable.fog;
                break;
            case "cloudy":
                iconId = R.drawable.cloudy;
                break;
            case "partly-cloudy-day":
                iconId = R.drawable.partly_cloudy;
                break;
            case "partly-cloudy-night":
                iconId = R.drawable.cloudy_night;
        }

        return iconId;
    }

    public long getTime() {
        return time;
    }

    public String getFormatedTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");

        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));

        return formatter.format(new Date((time * 1000)));
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = Math.round(temperature * 10) / (float) 10;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPrecipChance() {
        return precipChance;
    }

    public void setPrecipChance(double precipChance) {
        this.precipChance = precipChance;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
