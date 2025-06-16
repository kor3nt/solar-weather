package com.kor3nt.solar_weather.controller;

import com.kor3nt.solar_weather.dto.DailyWeather;
import com.kor3nt.solar_weather.dto.ForecastRequest;
import com.kor3nt.solar_weather.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    private final WeatherService weatherService;
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping("/forecast")
    public ResponseEntity<?> forecast(@Valid @RequestBody ForecastRequest req) {
        List<DailyWeather> data = weatherService.getForecast(req);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/weekly-summary")
    public ResponseEntity<?> getWeeklySummary(@Valid @RequestBody ForecastRequest request) {
        return ResponseEntity.ok(weatherService.getWeeklySummary(request));
    }
}