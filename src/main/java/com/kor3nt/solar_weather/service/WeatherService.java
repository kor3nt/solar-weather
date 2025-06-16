package com.kor3nt.solar_weather.service;

import com.kor3nt.solar_weather.dto.DailyWeather;
import com.kor3nt.solar_weather.dto.ForecastRequest;
import com.kor3nt.solar_weather.dto.WeeklySummaryResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class WeatherService {
    private static final double PANEL_POWER = 2.5; // kW
    private static final double EFFICIENCY = 0.2;

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<DailyWeather> getForecast(ForecastRequest req) {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.open-meteo.com/v1/forecast")
                .queryParam("latitude", req.getLatitude())
                .queryParam("longitude", req.getLongitude())
                .queryParam("daily", "weathercode,temperature_2m_min,temperature_2m_max,sunshine_duration")
                .queryParam("timezone", "auto")
                .toUriString();

        Map<?, ?> apiResponse;
        try {
            apiResponse = restTemplate.getForObject(url, Map.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalStateException("External API error: " + e.getStatusCode());
        }

        if (apiResponse == null || !apiResponse.containsKey("daily")) {
            return Collections.emptyList();
        }

        Map<String, Object> daily = (Map<String, Object>) apiResponse.get("daily");
        List<String> dates = (List<String>) daily.getOrDefault("time", Collections.emptyList());
        List<Integer> codes = (List<Integer>) daily.getOrDefault("weathercode", Collections.emptyList());
        List<Double> minT = (List<Double>) daily.getOrDefault("temperature_2m_min", Collections.emptyList());
        List<Double> maxT = (List<Double>) daily.getOrDefault("temperature_2m_max", Collections.emptyList());
        List<Double> sun = (List<Double>) daily.getOrDefault("sunshine_duration", Collections.emptyList());

        List<DailyWeather> result = new ArrayList<>();
        int size = dates.size();

        for (int i = 0; i < size; i++) {
            double sunshineSeconds = i < sun.size() ? sun.get(i) : 0.0;
            double hours = sunshineSeconds / 3600.0;
            double energy = Math.round(PANEL_POWER * hours * EFFICIENCY * 100.0) / 100.0;

            String date = i < dates.size() ? dates.get(i) : "";
            Integer code = i < codes.size() ? codes.get(i) : 0;
            Double minTemp = i < minT.size() ? minT.get(i) : 0.0;
            Double maxTemp = i < maxT.size() ? maxT.get(i) : 0.0;

            result.add(new DailyWeather(date, code, minTemp, maxTemp, energy));
        }
        return result;
    }

    public WeeklySummaryResponse getWeeklySummary(ForecastRequest request) {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.open-meteo.com/v1/forecast")
                .queryParam("latitude", request.getLatitude())
                .queryParam("longitude", request.getLongitude())
                .queryParam("daily", "sunshine_duration,temperature_2m_min,temperature_2m_max,weathercode")
                .queryParam("hourly", "pressure_msl")
                .queryParam("timezone", "auto")
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        if (response == null || !response.containsKey("daily") || !response.containsKey("hourly")) {
            return new WeeklySummaryResponse(0, 0, 0, 0, "Brak danych");
        }

        Map<String, Object> daily = (Map<String, Object>) response.get("daily");
        List<Double> sunshineList = safeCastList(daily.get("sunshine_duration"), Double.class);
        List<Double> tempMinList = safeCastList(daily.get("temperature_2m_min"), Double.class);
        List<Double> tempMaxList = safeCastList(daily.get("temperature_2m_max"), Double.class);
        List<Integer> weatherCodes = safeCastList(daily.get("weathercode"), Integer.class);

        Map<String, Object> hourly = (Map<String, Object>) response.get("hourly");
        List<Double> pressureHourlyList = safeCastList(hourly.get("pressure_msl"), Double.class);

        double avgPressure = pressureHourlyList.stream().mapToDouble(Double::doubleValue).average().orElse(0);

        double avgSunshine = sunshineList.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double minTemp = tempMinList.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        double maxTemp = tempMaxList.stream().mapToDouble(Double::doubleValue).max().orElse(0);

        long daysWithPrecipitation = weatherCodes.stream().filter(code -> code >= 50).count();
        String summary = (daysWithPrecipitation >= 4) ? "z opadami" : "bez opadów";

        return new WeeklySummaryResponse(avgPressure, avgSunshine, minTemp, maxTemp, summary);
    }

    private <T> List<T> safeCastList(Object obj, Class<T> clazz) {
        if (!(obj instanceof List<?>)) return Collections.emptyList();
        List<?> rawList = (List<?>) obj;
        List<T> result = new ArrayList<>();
        for (Object o : rawList) {
            if (clazz.isInstance(o)) {
                result.add(clazz.cast(o));
            } else if (o instanceof Number) {
                Number num = (Number) o;
                if (clazz == Double.class) {
                    result.add(clazz.cast(num.doubleValue()));
                } else if (clazz == Integer.class) {
                    result.add(clazz.cast(num.intValue()));
                }
            }
        }
        return result;
    }
}
