package com.example.weatherapp.weather_app.Service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;




@Service
@Slf4j
public class WeatherService {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    public String fetchWeather(String location) {
        ResponseEntity<String> response = null;
        String url = String.format("%s?q=%s&appid=%s&units=metric", apiUrl, location, apiKey);
        try {
             response = new RestTemplate().getForEntity(url, String.class);
            if (response == null) {
                return null;
            }
            return response.getBody();
        }
        catch (Exception e){
            log.error("Something happened while getting response "+e);

                return null;


        }
    }

    public String formatEmailBody(String location, JsonNode weatherData) {
        String temp = weatherData.path("main").path("temp").asText();
        String humidity = weatherData.path("main").path("humidity").asText();
        String condition = weatherData.path("weather").get(0).path("description").asText();

        return String.format("""
                Dear User,
                
                Here's the current weather report for %s:

                Temperature: %s°C
                Humidity: %s%%
                Condition: %s

                Regards,
                Weather Service ☀️
                """, location, temp, humidity, condition);
    }
}

