package com.example.weatherapp.weather_app.Service;

import lombok.Data;

@Data
public class WeatherResponse {
    private Weather[] weather;
    private Main main;

    @Data
    public static class Weather {
        private String main;
        private String description;
    }

    @Data
    public static class Main {
        private double temp;
        private int humidity;
    }
}

