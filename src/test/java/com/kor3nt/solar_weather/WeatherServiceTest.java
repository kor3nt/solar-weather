package com.kor3nt.solar_weather;

import com.kor3nt.solar_weather.dto.DailyWeather;
import com.kor3nt.solar_weather.dto.ForecastRequest;
import com.kor3nt.solar_weather.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;


class WeatherServiceTest {

    private WeatherService weatherService;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        weatherService = new WeatherService(restTemplate);

        Map<String, Object> daily = new HashMap<>();
        daily.put("time", List.of("2025-06-16", "2025-06-17"));
        daily.put("temperature_2m_max", List.of(25.0, 26.5));
        daily.put("temperature_2m_min", List.of(14.0, 13.5));
        daily.put("weather_code", List.of(1, 2));
        daily.put("sunshine_duration", List.of(18000.0, 20000.0));

        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("daily", daily);

        Mockito.when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenReturn(mockResponse);
    }
}
