package com.example.weatherapp.weather_app.Service;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class WeatherRequest {
    @NotEmpty(message = "Email is required")
    private String email;
    private String city;
    private String state;
    private String country;

    // Getters & Setters
}
