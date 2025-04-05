package com.example.weatherapp.weather_app.Service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;


//@Service
//public class WeatherService {
//
//    @Value("${openweathermap.api.key}")
//    private String apiKey;
//
//    @Value("${openweathermap.api.url}")
//    private String apiUrl;
//
//    @Autowired
//    private RestTemplate restTemplate;
//    @Autowired
//    private JavaMailSender mailSender;
//
//    public WeatherResponse getWeather(String city) {
//        String url = String.format("%s?q=%s&appid=%s&units=metric", apiUrl, city, apiKey);
//        RestTemplate restTemplate = new RestTemplate();
//        return restTemplate.getForObject(url, WeatherResponse.class);
//    }
//
//    public void sendEmail(String to, String city, WeatherResponse weather) {
//        String subject = "Weather Report for " + city;
//        String body = String.format("Temperature: %.2f°C\nCondition: %s\nHumidity: %d%%",
//                weather.getMain().getTemp(), weather.getWeather()[0].getDescription(), weather.getMain().getHumidity());
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(body);
//        mailSender.send(message);
//    }
//}

@Service
public class WeatherService {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    public String fetchWeather(String location) {
        String url = String.format("%s?q=%s&appid=%s&units=metric", apiUrl, location, apiKey);
        ResponseEntity<String> response = new RestTemplate().getForEntity(url, String.class);
        return response.getBody();
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

