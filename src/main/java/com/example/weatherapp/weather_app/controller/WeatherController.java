package com.example.weatherapp.weather_app.controller;

import com.example.weatherapp.weather_app.Service.EmailService;
import com.example.weatherapp.weather_app.Service.WeatherRequest;
import com.example.weatherapp.weather_app.Service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@Slf4j
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("weatherRequest", new WeatherRequest());
        return "index";
    }

    @GetMapping ("/weather/send-email")
    public String sendWeatherReport(@ModelAttribute @Valid WeatherRequest request,
                                    BindingResult result,
                                    Model model) throws JsonProcessingException {

        if ((request.getCity() == null || request.getCity().isBlank()) &&
                (request.getState() == null || request.getState().isBlank()) &&
                (request.getCountry() == null || request.getCountry().isBlank())) {
            model.addAttribute("customError", "At least one location field (city/state/country) is required.");
            //result.rejectValue("city", "custom.location.required", "At least one location field (city/state/country) is required.");

            return "index";
        }

        if (result.hasErrors()) {
            log.info("Some errrors are there ");
            return "index";
        }

        String location = Stream.of(request.getCity(), request.getState(), request.getCountry())
                .filter(s -> s != null && !s.isBlank())
                .collect(Collectors.joining(", "));

        String json = weatherService.fetchWeather(location);
        JsonNode weatherData = new ObjectMapper().readTree(json);
        String body = weatherService.formatEmailBody(location, weatherData);

        try {
            log.info("No errors ");
            emailService.sendWeatherEmail(request.getEmail(), "Your Weather Report 🌤️", body);
            model.addAttribute("location", location);
            model.addAttribute("email", request.getEmail());
            return "success"; // show success.html
        } catch (MessagingException e) {
            log.error("Not able to send email " +e);
            model.addAttribute("customError", "❌ Failed to send email. Try again.");
            return "error";
        }
    }
}
