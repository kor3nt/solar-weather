package com.kor3nt.solar_weather;

import com.kor3nt.solar_weather.controller.WeatherController;
import com.kor3nt.solar_weather.dto.DailyWeather;
import com.kor3nt.solar_weather.dto.ForecastRequest;
import com.kor3nt.solar_weather.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class SolarWeatherApplicationTests {

	@Autowired
	private WeatherService weatherService;

	@Autowired
	private WeatherController weatherController;

	@Test
	void contextLoads() {
		assertThat(weatherService).isNotNull();
		assertThat(weatherController).isNotNull();
	}

	@Test
	void serviceShouldReturnForecastData() {
		ForecastRequest request = new ForecastRequest(50.0647, 19.9450); // Kraków

		List<DailyWeather> result = weatherService.getForecast(request);

		assertThat(result).isNotNull();
		assertThat(result.size()).isGreaterThan(0);
	}
}